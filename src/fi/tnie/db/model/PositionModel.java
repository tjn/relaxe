/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

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
