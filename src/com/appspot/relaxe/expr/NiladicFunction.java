/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.AbstractPrimitiveType;

public class NiladicFunction    
    implements ValueExpression {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4927882086659272826L;
	private SQLKeyword name;
    private int dataType;    
    
    /** Niladic function CURRENT_USER.
     * 
     * AbstractType of this value expression is VARCHAR.
     * That may not be generally "right", but good enough. 
     */
    public static final NiladicFunction CURRENT_USER = 
        new NiladicFunction(SQLKeyword.CURRENT_USER, AbstractPrimitiveType.VARCHAR);

    /** Niladic function CURRENT_DATE. 
     *  AbstractType of this value expression is Types.DATE  
     */
    public static final NiladicFunction CURRENT_DATE = 
        new NiladicFunction(SQLKeyword.CURRENT_USER, AbstractPrimitiveType.DATE);
    
    /** Niladic function CURRENT_TIME. 
     *  AbstractType of this value expression is Types.TIME  
     */    
    public static final NiladicFunction CURRENT_TIME = 
        new NiladicFunction(SQLKeyword.CURRENT_TIME, AbstractPrimitiveType.TIME);
    
    /** Niladic function CURRENT_TIMESTAMP. 
     *  AbstractType of this value expression is Types.TIMESTAMP  
     */        
    public static final NiladicFunction CURRENT_TIMESTAMP = 
        new NiladicFunction(SQLKeyword.CURRENT_TIMESTAMP, AbstractPrimitiveType.TIMESTAMP);    
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected NiladicFunction() {
	}
    
    private NiladicFunction(SQLKeyword name, int dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public SQLKeyword getName() {        
        return this.name;
    }

    @Override
    public int getType() {
        return this.dataType;
    }

    @Override
    public Identifier getColumnName() {
        return null;
    }

    @Override
    public String getTerminalSymbol() {
        return null;
    }

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        this.name.traverse(vc, v);
    }
    
    
}
