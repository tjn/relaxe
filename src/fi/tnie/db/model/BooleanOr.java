/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.model;

import java.util.Arrays;
import java.util.List;

public class BooleanOr
	extends AbstractBooleanOperatorModel {
			
	
	public BooleanOr(BooleanModel ... input) {
		this(Arrays.asList(input));
	}
	
	public BooleanOr(List<BooleanModel> input) {
		super(input);
	}
	
	/**
	 * Computes Boolean OR from the input.
	 * 
	 * Returns Boolean.TRUE if and only if <code>input</code> contains at least one model with <code>true</code> value.   
	 * Otherwise, returns <code>null</code> if <code>input</code> contains at least one model with <code>null</code> value.
	 * Otherwise, returns Boolean.FALSE.
	 * 
	 * As a consequence, Boolean.FALSE is returned if the input is empty.  
	 * 
	 * @throws NullPointerException if the input is <code>null</code>.
	 * @precondition: <code>input</code> does not contain null -elements. 
	 */
	@Override
	protected Boolean computeNewValue(List<BooleanModel> input)
		throws NullPointerException {
		Boolean result = Boolean.FALSE;
		
		for (BooleanModel m : input) {
			Boolean v = m.get();
			
			if (v == null) {
				result = null;
				continue;
			}
			
			if (v.booleanValue()) {
				result = Boolean.TRUE;
				break;
			}				
		}
		
		return result;
	}		
}