/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.model.BooleanModel;
import com.appspot.relaxe.model.Registration;


public abstract class AbstractPagerModel<T extends Serializable, P extends PagerModel<T, P, C>, C> {

	private Map<Registration, PagerModelEventHandler<T, P, C>> handlerMap;
		
	private Map<Registration, PagerModelEventHandler<T, P, C>> getHandlerMap() {
		if (handlerMap == null) {
			handlerMap = new HashMap<Registration, PagerModelEventHandler<T, P, C>>();			
		}

		return handlerMap;
	}
		
	public Registration addPagingEventListener(PagerModelEventHandler<T, P, C> handler) {
		if (handler == null) {
			throw new NullPointerException("handler");
		}
		
		final Map<Registration, PagerModelEventHandler<T, P, C>> hm = getHandlerMap();
		
		Registration reg = new Registration() {			
			@Override
			public void remove() {
				hm.remove(this);
			}
		};
		
		hm.put(reg, handler);
		
		return reg;
	}	
	
	protected void fireEvent(PagerModelEvent<T, P, C> newEvent) {
		for (PagerModelEventHandler<T, P, C> h : getHandlerMap().values()) {
			h.handleEvent(newEvent);			
		}
	}

	public abstract BooleanModel hasPreviousPage();
	public abstract BooleanModel hasNextPage();

	public abstract P self();
	
	
}
