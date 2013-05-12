/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.model.BinaryRelationModel;
import com.appspot.relaxe.model.BooleanModel;
import com.appspot.relaxe.model.ConstantBooleanModel;
import com.appspot.relaxe.model.DefaultConstantValueModel;
import com.appspot.relaxe.model.DefaultMutableValueModel;
import com.appspot.relaxe.model.ImmutableValueModel;
import com.appspot.relaxe.model.LongAdditionModel;
import com.appspot.relaxe.model.MutableIntegerModel;
import com.appspot.relaxe.model.MutableStringModel;
import com.appspot.relaxe.model.MutableValueModel;
import com.appspot.relaxe.model.NotNullableModel;
import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.ui.action.AbstractAction;
import com.appspot.relaxe.ui.action.Action;


public abstract class DefaultPagerModel<	
	Q,
	R extends ResultPage,
	P extends SimplePagerModel<R, P>,
	F extends Fetcher<Q, R, Receiver<R>>
>
	extends AbstractSimplePager<R, P> {
	
	private F fetcher;	
	private NotNullableModel<Q> template;
	private R result;	
	private PageSizeModel pageSize;
	
	private Map<SimplePagerModel.Command, Action> actionMap;
	
	private BooleanModel hasPreviousPage;
	private BooleanModel hasNextPage;
	
	private MutableValueModel<Integer> currentPageSize;
	private MutableValueModel<Long> currentOffset;	
	private MutableValueModel<Long> availableModel;
	private ImmutableValueModel<Long> available;
	
	private ImmutableValueModel<Long> currentPageOffset;

	
	public DefaultPagerModel(Q template, F fetcher, Map<SimplePagerModel.Command, String> nm, int initialPageSize) {
		this(template, fetcher, initialPageSize, createNameModelMap(nm));		
	}

	public static Map<Command, ValueModel<String>> createNameModelMap(Map<Command, String> nm) {
		EnumMap<Command, ValueModel<String>> nmm = new EnumMap<Command, ValueModel<String>>(Command.class);
		
		if (nm == null) {
			nm = Collections.emptyMap();
		}
		
		for (Command c : Command.values()) {
			String n = nm.get(c);
			nmm.put(c, new MutableStringModel(n));
		}
		
		return nmm;
	}

	public DefaultPagerModel(Q template, F fetcher, int initialPageSize, Map<SimplePagerModel.Command, ValueModel<String>> nmm) {
		super();
		
		if (template == null) {
			throw new NullPointerException("DefaultPager(): template");
		}
		
		if (fetcher == null) {
			throw new NullPointerException("DefaultPager(): fetcher");
		}
		
		this.template = new DefaultMutableValueModel.NotNullable<Q>(template);
		
		
		this.fetcher = fetcher;
		this.pageSize = new PageSizeModel(Integer.valueOf(initialPageSize));		

		this.currentPageSize = new MutableIntegerModel(0);
		this.currentOffset = new DefaultMutableValueModel<Long>(null);
		this.currentPageOffset = currentOffset.asImmutable();
		
		this.availableModel = new DefaultMutableValueModel<Long>(null);		
		this.available = availableModel.asImmutable(); 
		
		this.actionMap = createActionMap(nmm);				
	}


	private Map<Command, Action> createActionMap(Map<Command, ValueModel<String>> nmm) {
		EnumMap<Command, Action> am = new EnumMap<Command, Action>(Command.class);
						
		for (Command c : Command.values()) {
			Action pa = createPagingAction(c, nmm);
			am.put(c, pa);
		}
		
		return am;
	}	
	
	protected Action createPagingAction(final Command pc, Map<Command, ValueModel<String>> nmm) {
		ValueModel<String> nm = nmm.get(pc);
		
		BooleanModel em = ConstantBooleanModel.TRUE;
		
		switch (pc) {
		case FIRST:
		case PREVIOUS:		
			em = hasPreviousPage();
			break;
		case NEXT:
		case LAST:		
			em = hasNextPage();
			break;
		default:
			break;
		}		
		
		return new PagingAction(em, nm, pc);		
	}
	

	@Override
	public Action getAction(com.appspot.relaxe.paging.SimplePagerModel.Command command) {
		return this.actionMap.get(command);
	}
	
	protected FetchOptions getOptionsFor(final SimplePagerModel.Command command) {
		FetchOptions opts = null;
		final int ps = pageSize();
		final long co = currentOffset();
		long off = 0;
		
		switch (command) {
		case FIRST:		
			off = 0;
			break;			
		case PREVIOUS:
			long no = co - ps;
			off = no < 0 ? 0 : no;
			break;			
		case CURRENT:
			off = co;
			break;			
		case NEXT:
			off = co + ps;
			break;
		case LAST:
			off = -ps;
			break;
		default:
			break;
		}
		
		opts = new FetchOptions(ps, off);
		
		return opts;
	}
	
	protected void fetch(final SimplePagerModel.Command command) {
		FetchOptions opts = getOptionsFor(command);
		fetch(command, opts);		
	}
	
	public void fetchFirst() {
		fetch(Command.FIRST);
	}
	
	
	protected void fetch(final SimplePagerModel.Command command, FetchOptions opts) {
		Receiver<R> rr = new Receiver<R>() {
			@Override
			public void receive(R result) {
				received(result, command);
			}
		};
		
		this.fetcher.fetch(template.get(), opts, rr, null);
	}
	
	protected void received(R result, Command command) {
		this.result = result;
		
		currentOffset.set(Long.valueOf(result.getOffset()));
		currentPageSize.set(Integer.valueOf(result.size()));				
		availableModel.set(result.available());		
		
		fireEvent(new PagerModelEvent<R, P, Command>(self(), command));
	}

	@Override
	public R getCurrentPage() {
		return result;
	}

	private final class PagingAction extends AbstractAction {
		private final Command pc;

		private PagingAction(BooleanModel em, ValueModel<String> nameModel, Command pc) {
			super(em, nameModel);
			this.pc = pc;
		}

		@Override
		protected void run() {
			FetchOptions fo = getOptionsFor(pc);
			fetch(pc, fo);					
		}
	}

	private static class PageSizeModel extends DefaultMutableValueModel<Integer> {
		private PageSizeModel(Integer initialValue) {
			super(initialValue);
		}

		@Override
		public void set(Integer newPageSize) {
			if (newPageSize == null) {
				throw new NullPointerException("newPageSize");
			}
			
			if (newPageSize.intValue() < 1) {
				throw new IllegalArgumentException("invalid page-size: " + newPageSize);
			}
							
			super.set(newPageSize);
		}
	}
	
	public PageSizeModel getPageSizeModel() {
		return this.pageSize;
	}
	
	
	@Override
	public int getPageSize() {
		return getPageSizeModel().get().intValue();
	}

	@Override
	public BooleanModel hasPreviousPage() {
		if (hasPreviousPage == null) {
			hasPreviousPage = new BinaryRelationModel.Gt<Long>(getCurrentOffset(), DefaultConstantValueModel.valueOf(0L));			
		}

		return hasPreviousPage;	
	}
	
	@Override
	public BooleanModel hasNextPage() {
		if (hasNextPage == null) {
			LongAdditionModel<Long, Integer> pm = new LongAdditionModel<Long, Integer>(getCurrentOffset(), getPageSizeModel());						
			this.hasNextPage = new BinaryRelationModel.Lt<Long>(pm, this.availableModel);			
		}

		return hasNextPage;
	}
	
	private long currentOffset() {
		Long o = getCurrentOffset().get();
		return (o == null) ? 0 : o.longValue();	
	}
	
	private MutableValueModel<Long> getCurrentOffset() {
		return currentOffset;
	}
	
	@Override
	public ImmutableValueModel<Long> currentPageOffset() {
		return this.currentPageOffset;
	}
	
	@Override
	public ImmutableValueModel<Long> available() {
		return this.available;
	}

	private int pageSize() {
		return getPageSizeModel().get().intValue();		
	}

	public NotNullableModel<Q> getTemplate() {
		return this.template;
	}
	
	@Override
	public String toString() {
		return super.toString() + "; query-template=" + this.template.get();
	}
}
