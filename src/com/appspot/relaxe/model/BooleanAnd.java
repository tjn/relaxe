/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.model;

import java.util.Arrays;
import java.util.List;

public class BooleanAnd
	extends AbstractBooleanOperatorModel {
	
	public BooleanAnd(BooleanModel ... input) {
		this(Arrays.asList(input));
	}
		
	public BooleanAnd(List<BooleanModel> input) {
		super(input);
	}

	/**
	 * Computes Boolean AND from the input.
	 * 
	 * Returns Boolean.TRUE if and only if <code>input</code> all the element it cat least one model with <code>true</code> value.   
	 * Otherwise, returns <code>null</code> if <code>input</code> contains at least one model with <code>null</code> value.
	 * Otherwise, returns Boolean.FALSE.
	 * 
	 * Boolean.FALSE is returned if the input is empty.  
	 * 
	 * @throws NullPointerException if the input <code>null</code>.
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
			
			result = v.booleanValue() ? Boolean.TRUE : Boolean.FALSE;
			
			if (!v.booleanValue()) {
				break;				
			}
		}
		
		return result;
	}		
}