package com.appspot.relaxe.paging;

import java.util.List;

import com.appspot.relaxe.model.MutableBooleanModel;
import com.appspot.relaxe.ui.action.AbstractAction;
import com.appspot.relaxe.ui.action.Action;

public class DefaultPagerModel<
	P extends Page<?>,
	G extends Pager<?, ?, P, G>	
> implements PagerModel {
		
	private G pager;
	
	private Action firstPageAction;
	private MutableBooleanModel firstPage;	

	private Action lastPageAction;
	private MutableBooleanModel lastPage;
	
	private Action refreshAction;
	private MutableBooleanModel refresh;

	private Action previousPageAction;
	private MutableBooleanModel previousPage;
	
	private Action previousAction;
	private MutableBooleanModel previous;
	
	private Action nextAction;
	private MutableBooleanModel next;
	
	private Action nextPageAction;
	private MutableBooleanModel nextPage;
		
	public DefaultPagerModel(G pager) {
		super();
		this.pager = pager;
		
		this.firstPage = new MutableBooleanModel(false);
		this.lastPage = new MutableBooleanModel(false);
		this.refresh = new MutableBooleanModel(false);
		this.previous = new MutableBooleanModel(false);
		this.next = new MutableBooleanModel(false);
		this.previousPage = new MutableBooleanModel(false);
		this.nextPage = new MutableBooleanModel(false);		
		
		pager.addPagerEventHandler(new PagerEventHandler<G>() {
			@Override
			public void handleEvent(PagerEvent<G> e) {
				update(e.getSource());
			}			
		});
		
		update(pager);
	}
	
	public G getPager() {
		return pager;
	}	

	private void update(G p) {		
		P cp = p.getCurrentPage();
		
		boolean idle = (p.getState() == Pager.State.IDLE);
				
		int px = (p.index() == null) ? -1 : p.index().intValue();
		Long avail = (cp == null) ? null : cp.available();
		
		List<?> cc = (cp == null) ? null : cp.getContent();		
		int size = cc == null ? 0 : cc.size();
						
		boolean mayHaveNextPage = (avail == null) ? true : p.offset() + size < avail.longValue();
		boolean hasNextOrNextPage = (!p.isLastElement()) || mayHaveNextPage;
		
		this.firstPage.set(idle);
		this.lastPage.set(idle);
		this.refresh.set(idle);
		
		this.previousPage.set(idle && p.offset() > 0);
		this.previous.set(idle && p.offset() > 0 || px > 0);
		this.next.set(idle && hasNextOrNextPage);
		this.nextPage.set(idle && mayHaveNextPage);
	}

	@Override
	public Action getNextAction() {
		if (this.nextAction == null) {
			this.nextAction = new AbstractAction(this.next.asImmutable(), ">") {
				@Override
				protected void run() {
					getPager().next();
				}
			};			
		}
		
		return this.nextAction;		
	}
	
	@Override
	public Action getPreviousAction() {
		if (this.previousAction == null) {
			this.previousAction = new AbstractAction(this.previous.asImmutable(), "<") {
				@Override
				protected void run() {
					getPager().previous();
				}
			};			
		}
		
		return this.previousAction;		
	}

	@Override
	public Action getNextPageAction() {
		if (this.nextPageAction == null) {
			this.nextPageAction = new AbstractAction(this.nextPage.asImmutable(), ">>") {
				@Override
				protected void run() {
					getPager().nextPage();
				}
			};			
		}
		
		return this.nextPageAction;		
	}

	@Override
	public Action getPreviousPageAction() {
		if (this.previousPageAction == null) {
			this.previousPageAction = new AbstractAction(this.previousPage.asImmutable(), "<<") {
				@Override
				protected void run() {
					getPager().previousPage();
				}
			};			
		}
		
		return this.previousPageAction;		
	}

	@Override
	public Action getFirstPageAction() {
		if (this.firstPageAction == null) {
			this.firstPageAction = new AbstractAction(this.firstPage.asImmutable(), "|<") {
				@Override
				protected void run() {
					getPager().firstPage();
				}
			};			
		}
		
		return this.firstPageAction;		
	}

	@Override
	public Action getLastPageAction() {
		if (this.lastPageAction == null) {
			this.lastPageAction = new AbstractAction(this.lastPage.asImmutable(), "|<") {
				@Override
				protected void run() {
					getPager().lastPage();
				}
			};			
		}
		
		return this.lastPageAction;		
	}

	@Override
	public Action getRefreshAction() {
		if (this.refreshAction == null) {
			this.refreshAction = new AbstractAction(this.refresh.asImmutable(), "<>") {
				@Override
				protected void run() {
					getPager().refresh();
				}
			};			
		}
		
		return this.refreshAction;
	}	
}
