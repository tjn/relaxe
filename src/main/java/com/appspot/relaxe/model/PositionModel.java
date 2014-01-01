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
package com.appspot.relaxe.model;

public abstract class PositionModel
	extends MutableIntegerModel {
	
	public enum Fallback {
		REJECT,
		RESET,		
		ROLL,
	}
	
		
	public PositionModel() {
		super();		
	}
	
	@Override
	public void set(Integer n) {								
		if (isValid(n)) {
			super.set(n);
		}
	}

	public boolean isValid(Integer n) {
		if (n != null) {
			int np = n.intValue();					
											
			if (np < 0 || np >= limit()) {
				return false;
			}
		}
		
		return true;
	}
		
	protected abstract int limit();
	
	public Fallback set(Integer value, Fallback a) {
		if (value == null) {
			set(value);
			return null;
		}					
		
		return set(value.intValue(), a);				
	}
		
	public Fallback next(Fallback a) {
		Integer pos = get();		
		int np = (pos == null) ? 0 : pos.intValue() + 1;
		return set(np, a);
		
	}
	private Fallback set(int np, Fallback a) {
		int limit = limit();
				
		if (np < limit) {
			super.set(Integer.valueOf(np));
			return null;
		}
		
		a = (a == null) ? Fallback.REJECT : a;
		
		switch (a) {		
			case RESET:
				set(null);				
				break;
			case ROLL:
				if (0 < limit) {
					super.set(Integer.valueOf(0));
				}
				else {
					a = Fallback.REJECT;
				}
				break;
			default:		
		}	
		
		return a;
	}
}
