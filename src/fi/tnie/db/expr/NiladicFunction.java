/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.types.Type;

public class NiladicFunction    
    implements ValueExpression {
    
    private Keyword name;
    private int dataType;    
    
    /** Niladic function CURRENT_USER.
     * 
     * Type of this value expression is VARCHAR.
     * That may not be generally "right", but good enough. 
     */
    public static final NiladicFunction CURRENT_USER = 
        new NiladicFunction(Keyword.CURRENT_USER, Type.VARCHAR);

    /** Niladic function CURRENT_DATE. 
     *  Type of this value expression is Types.DATE  
     */
    public static final NiladicFunction CURRENT_DATE = 
        new NiladicFunction(Keyword.CURRENT_USER, Type.DATE);
    
    /** Niladic function CURRENT_TIME. 
     *  Type of this value expression is Types.TIME  
     */    
    public static final NiladicFunction CURRENT_TIME = 
        new NiladicFunction(Keyword.CURRENT_TIME, Type.TIME);
    
    /** Niladic function CURRENT_TIMESTAMP. 
     *  Type of this value expression is Types.TIMESTAMP  
     */        
    public static final NiladicFunction CURRENT_TIMESTAMP = 
        new NiladicFunction(Keyword.CURRENT_TIMESTAMP, Type.TIMESTAMP);    
    
    private NiladicFunction(Keyword name, int dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public Keyword getName() {        
        return this.name;
    }

    @Override
    public int getType() {
        return this.dataType;
    }

    @Override
    public ColumnName getColumnName() {
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
