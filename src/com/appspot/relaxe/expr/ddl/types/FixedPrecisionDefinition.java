/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

public abstract class FixedPrecisionDefinition
    extends AbstractNumericTypeDefinition {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 2301149943314207588L;
	
	private IntLiteral precision = null;
	private IntLiteral scale = null;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public FixedPrecisionDefinition() {		
	}
	
	public FixedPrecisionDefinition(int precision) {
		this.precision = IntLiteral.valueOf(precision);		 
	}
	    
    public FixedPrecisionDefinition(int precision, int scale, SQLTypeDefinition.Name type) {
    	this(Integer.valueOf(precision), Integer.valueOf(scale), type);
    }
    
    protected FixedPrecisionDefinition(Integer precision, Integer scale, SQLTypeDefinition.Name type) {
    	if (precision != null) {    		
        	this.precision = IntLiteral.valueOf(precision);
        	
        	if (precision.intValue() < 1) {
        		throw new IllegalArgumentException(
        				type + " precision (" + precision + ") must be > 0");        		
        	}
        	
        	
        	if (scale != null) {
        		if (scale < 0 || scale > precision) {
            		throw new IllegalArgumentException(
            				type + " scale (" + scale + ") must be between 0 and precision " + precision);
            	}
        		
        		this.scale = IntLiteral.valueOf(scale);	
        	}   		
    	}
    }
    
    @Override
    public abstract SQLTypeDefinition.Name getSQLTypeName();

	@Override
	public final boolean isExact() {
		return true;
	}
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		getSQLTypeName().traverse(vc, v);
		
		if (this.precision != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);
			this.precision.traverse(vc, v);
			
			if (this.scale != null) {
				Symbol.COMMA.traverse(vc, v);
				this.scale.traverse(vc, v);
			}
			
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
	}
	
	
}
