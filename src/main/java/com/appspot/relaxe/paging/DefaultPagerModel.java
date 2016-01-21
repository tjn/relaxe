/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.paging;

import java.util.List;

import com.appspot.relaxe.model.MutableBooleanModel;
import com.appspot.relaxe.model.ValueModel;

public class DefaultPagerModel<
	P extends Page<?>,
	G extends Pager<?, ?, P, G>	
> implements PagerModel {
		
	private G pager;
	
	private PagerAction firstPageAction;
	private MutableBooleanModel firstPage;	

	private PagerAction lastPageAction;
	private MutableBooleanModel lastPage;
	
	private PagerAction refreshAction;
	private MutableBooleanModel refresh;

	private PagerAction previousPageAction;
	private MutableBooleanModel previousPage;
	
	private PagerAction previousAction;
	private MutableBooleanModel previous;
	
	private PagerAction nextAction;
	private MutableBooleanModel next;
	
	private PagerAction nextPageAction;
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
	
	private abstract class ImmutablePagerAction
		implements PagerAction {
	
		private ValueModel<Boolean> enabled;
		
		public ImmutablePagerAction(ValueModel<Boolean> enabled) {
			super();
			this.enabled = enabled;
		}

		@Override
		public boolean execute() {
			boolean enabled = isEnabled();
			
			if (enabled) {
				run();
			}
			return enabled;
		}
		
		protected abstract void run();

		@Override
		public boolean isEnabled() {
			return this.enabled.get().booleanValue();
		}
	
	}


	@Override
	public PagerAction getNextAction() {
		if (this.nextAction == null) {
			this.nextAction = new ImmutablePagerAction(this.next) {
				@Override
				public void run() {
					getPager().next();
				}
			};			
		}
		
		return this.nextAction;		
	}
	
	@Override
	public PagerAction getPreviousAction() {
		if (this.previousAction == null) {
			this.previousAction = new ImmutablePagerAction(this.previous) {
				@Override
				public void run() {
					getPager().previous();
				}
			};			
		}
		
		return this.previousAction;		
	}

	@Override
	public PagerAction getNextPageAction() {
		if (this.nextPageAction == null) {
			this.nextPageAction = new ImmutablePagerAction(this.nextPage) {
				@Override
				public void run() {
					getPager().nextPage();
				}
				
			};			
		}
		
		return this.nextPageAction;		
	}

	@Override
	public PagerAction getPreviousPageAction() {
		if (this.previousPageAction == null) {
			this.previousPageAction = new ImmutablePagerAction(this.previousPage) {
				@Override
				public void run() {
					getPager().previousPage();
				}
			};			
		}
		
		return this.previousPageAction;		
	}

	@Override
	public PagerAction getFirstPageAction() {
		if (this.firstPageAction == null) {
			this.firstPageAction = new PagerAction() {
				@Override
				public boolean execute() {
					boolean enabled = isEnabled();
					
					if (enabled) {
						getPager().firstPage();
					}
					
					return enabled;
				}
				@Override
				public boolean isEnabled() {
					return firstPage.get().booleanValue();
				}
				
			};			
		}
		
		return this.firstPageAction;		
	}

	@Override
	public PagerAction getLastPageAction() {
		if (this.lastPageAction == null) {
			this.lastPageAction = new ImmutablePagerAction(this.lastPage) {
				@Override
				public void run() {
					getPager().lastPage();
				}
			};			
		}
		
		return this.lastPageAction;		
	}

	@Override
	public PagerAction getRefreshAction() {
		if (this.refreshAction == null) {
			this.refreshAction = new ImmutablePagerAction(this.refresh) {
				@Override
				public void run() {
					getPager().refresh();
				}
			};			
		}
		
		return this.refreshAction;
	}
	
	
	
}
