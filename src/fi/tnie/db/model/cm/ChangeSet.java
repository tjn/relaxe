/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChangeSet
	implements Proposition {

	private int rejected = 0;
	private boolean committed;
	
	private Map<ConstrainedValueModel<?>, Proposition> propositionMap;
	
	/**
	 * Creates proposition from <code>proposed</code> value and adds it to this change set.
	 * 
	 * @param <V>
	 * @param model
	 * @param proposed
	 * @return
	 */
	public <V> Proposition add(final ConstrainedMutableValueModel<V> model, V proposed) {
		return add(model, proposed, false, null);  
	}
	
	/**
	 * Creates and adds proposition from <code>proposed</code> value and submits it.
	 * @param <V>
	 * @param model
	 * @param proposed
	 * @return
	 */	
	public <V> Proposition submit(final ConstrainedValueModel<V> model, V proposed, Proposition impliedBy) {
		return add(model, proposed, true, impliedBy);  
	}
		
	private <V> Proposition add(final ConstrainedValueModel<V> model, V proposed, boolean submit, Proposition impliedBy) {
		if (model.asMutable() == null && impliedBy == null) {
			throw new NullPointerException("impliedBy -proposition is required for read-only model");
		}		
		
		Proposition p = model.propose(this, proposed, impliedBy);
		getPropositionMap().put(model, p);
		
		if (submit) {
			model.submit(this, p);
		}
		
		return p;
	}
	
	@Override
	public void commit() {
		for (Proposition p : getPropositionMap().values()) {
			p.commit();					
		}
		
		committed = true;
	}

	@Override
	public boolean isRejected() {
		return this.rejected > 0;
	}
	
	/**
	 * Attempts to apply this change set as a whole.
	 *  
	 * @return
	 */
	
	public boolean apply() {		
		boolean committed = false;
		submit();
		
		if (!isRejected()) {
			commit();
			committed = true;
		}
		
		return committed;
	}	
	
	private void submit() {
		// Calls to ConstrainedValueModel.submit(ChangeSet cs, Proposition p)
		// may cause the dependent models to be added in this change-set.
		// By taking a snapshot of the key-set we avoid   
		// ConcurrentModificationExceptions:
		
		Map<ConstrainedValueModel<?>, Proposition> pm = getPropositionMap();
		
		ArrayList<ConstrainedValueModel<?>> keys = 
			new ArrayList<ConstrainedValueModel<?>>(pm.keySet());
				
		for (ConstrainedValueModel<?> m : keys) {
			Proposition p = pm.get(m);		
			m.submit(this, p);
						
			if (p.isRejected()) {
				this.rejected++;
				// one is enough
				break;
			}
		}		
	}

	@Override
	public boolean isCommitted() {
		return this.committed; 
	}

	@Override
	public void reject() {
		this.rejected++;
	}
	
	public Map<ConstrainedValueModel<?>, Proposition> getPropositionMap() {
		if (propositionMap == null) {
			propositionMap = new LinkedHashMap<ConstrainedValueModel<?>, Proposition>();			
		}

		return propositionMap;
	}

	@Override
	public Proposition impliedBy() {
		return null;
	}
	
}
