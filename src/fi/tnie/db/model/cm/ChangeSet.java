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
	private boolean submitted;
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
	public <V> Proposition add(final ConstrainedValueModel<V> model, V proposed) {
		return add(model, proposed, false);  
	}
	
	/**
	 * Creates and adds proposition from <code>proposed</code> value and submits it.
	 * @param <V>
	 * @param model
	 * @param proposed
	 * @return
	 */	
	public <V> Proposition submit(final ConstrainedValueModel<V> model, V proposed) {
		return add(model, proposed, true);  
	}
		
	private <V> Proposition add(final ConstrainedValueModel<V> model, V proposed, boolean submit) {		
		Proposition p = model.propose(this, proposed);
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
		submit(this);
		
		if (!isRejected()) {
			commit();
			committed = true;
		}
		
		return committed;
	}
	
	public void submit() {
		submit(this);
	}

	@Override
	public void submit(ChangeSet cs) {
		// Calls to ConstrainedValueModel.submit(ChangeSet cs, Proposition p)
		// may cause the dependent models to be added in this change-set.
		// By taking a snapshot of the key-set we avoid   
		// ConcurrentModificationExceptions:
		
		Map<ConstrainedValueModel<?>, Proposition> pm = getPropositionMap();
		
		ArrayList<ConstrainedValueModel<?>> keys = 
			new ArrayList<ConstrainedValueModel<?>>(pm.keySet());
				
		for (ConstrainedValueModel<?> m : keys) {
			Proposition p = pm.get(m);		
			m.submit(cs, p);
						
			if (p.isRejected()) {
				this.rejected++;
				// one is enough
				break;
			}
		}
		
		this.submitted = true;
	}

	@Override
	public boolean isCommitted() {
		return this.committed; 
	}

	@Override
	public boolean isSubmitted() {		
		return this.submitted;
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
}
