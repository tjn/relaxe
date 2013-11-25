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
package com.appspot.relaxe.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractValueModel<V>
	implements ValueModel<V> {
	
	private DefaultImmutableModel<V> immutable;
	
	public static class Key
		implements Registration {
	
		private Map<Key, ?> map;
		
		public Key(Map<Key, ?> map) {
			if (map == null) {
				throw new NullPointerException("map");
			}
			
			this.map = map;
		}
		
		@Override
		public void remove() {
			this.map.remove(this);
		}
	}
	
	private HashMap<Key, ChangeListener<V>> registrationMap;
	
	@Override
	public Registration addChangeHandler(ChangeListener<V> ch) {
		return register(getRegistrationMap(), ch);
	}

	protected <R> Registration register(Map<Key, R> dest, R handler) {
		if (handler == null) {
			throw new NullPointerException("handler");
		}
		
		if (dest == null) {
			throw new NullPointerException("dest");
		}		
		
		Key reg = new Key(dest);
		dest.put(reg, handler);
		return reg;
	}
	
	private HashMap<Key, ChangeListener<V>> getRegistrationMap() {
		if (registrationMap == null) {
			registrationMap = new HashMap<Key, ChangeListener<V>>();			
		}

		return registrationMap;
	}
	
	protected void fireIfChanged(V from, V to) {
		if (hasChanged(from, to)) {
			fireChanged(from, to);
		}
	}
		
	protected void fireChanged(V from, V to) {		
		for (ChangeListener<V> cl : getRegistrationMap().values()) {
			fireChanged(from, to, cl);
		}
	}

	private void fireChanged(V from, V to, ChangeListener<V> cl) {
		cl.changed(from, to);		
	}
	
	/**
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	protected boolean hasChanged(V oldValue, V newValue) {		
		if (oldValue == null) {
			return newValue != null;
		}
		
		if (newValue == null) {
			return true;
		}
		
		return (!oldValue.equals(newValue));    
	}
	
	/**
	 * Returns <code>null</code> which makes subclasses immutable by default.
	 */	
	@Override
	public MutableValueModel<V> asMutable() {	
		return null;
	}
	
	@Override
	public ImmutableValueModel<V> asImmutable() {
		if (immutable == null) {
			immutable = new DefaultImmutableModel<V>(this);			
		}

		return immutable;
	}
	
	@Override
	public boolean isMutable() {
		return (asMutable() != null);
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}
	
	@Override
	public ConstantValueModel<V> asConstant() {
		return new DefaultConstantValueModel<V>(get());
	}
}
