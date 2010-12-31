/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.model.AbstractValueModel;
import fi.tnie.db.model.Registration;

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
	public Proposition submit(ChangeSet cs, final Proposition p) {
		// p.submit(cs);		
		
		for (Constraint pl : getConstraintMap().values()) {
			fireSubmitted(cs, p, pl);
		}
								
		return p;
	}
	
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
