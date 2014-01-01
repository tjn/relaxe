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
package com.appspot.relaxe.model.cm;

import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.model.AbstractValueModel;
import com.appspot.relaxe.model.Registration;


public abstract class AbstractConstrainedValueModel<V>
	extends AbstractValueModel<V>
	implements ConstrainedValueModel<V> {
	
	public AbstractConstrainedValueModel() {
		
	}

	private HashMap<Key, Constraint> constraintMap;
	
	private HashMap<ChangeSet, V> propositionMap;

	private HashMap<Key, Constraint> getConstraintMap() {
		if (constraintMap == null) {
			constraintMap = new HashMap<Key, Constraint>();			
		}

		return constraintMap;
	}
	
	@Override
	public Registration addConstraint(Constraint ph) {				
		return register(getConstraintMap(), ph);
	}
	
	/**
	 * Proposes the change <code>p</code> and applies it if no-one complains. 
	 * 
	 * @param p
	 * @return
	 */
	@Override
	public Proposition submit(ChangeSet cs, final Proposition p) {
		// p.submit(cs);		
		
		for (Constraint pl : getConstraintMap().values()) {
			fireSubmitted(cs, p, pl);
		}
								
		return p;
	}
	
	public Proposition propose(V newValue) {
		ChangeSet cs = new ChangeSet();
		return propose(cs, newValue, null);
	}
	
	@Override
	public Proposition propose(ChangeSet cs, final V newValue, Proposition impliedBy) {
		if (cs == null) {
			throw new NullPointerException("cs");
		}
		
		Proposition p = createProposition(newValue, impliedBy);
		getPropositionMap().put(cs, newValue);
		
		return p;		
	}
	
	public Proposition apply(V newValue) {
		ChangeSet cs = new ChangeSet(); // atomic change
		
		Proposition p = cs.submit(this, newValue, null);
		
		if (!p.isRejected()) {
			cs.commit();
		}
		
		return p;
	}

	protected Proposition createProposition(V newValue, Proposition impliedBy) {
		return new SimpleProposition<V>(this, newValue, impliedBy) {
			@Override
			protected void apply() {				
				AbstractConstrainedValueModel<V> m = AbstractConstrainedValueModel.this;				
				m.fireIfChanged(from(), to());				
			}
		};
	}

	private void fireSubmitted(ChangeSet cs, Proposition p, Constraint pl) {
		pl.apply(cs, p);		
	}
		
	@Override
	public V proposed(ChangeSet cs) {
		return getPropositionMap().get(cs);
	}

	private Map<ChangeSet, V> getPropositionMap() {
		if (propositionMap == null) {
			propositionMap = new HashMap<ChangeSet, V>();			
		}

		return propositionMap;
	}
}
