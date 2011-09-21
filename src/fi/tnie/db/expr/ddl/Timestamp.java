/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

public class Timestamp
    extends SQLType {
        
    /**
	 * 
	 */
	private static final long serialVersionUID = 779228554708816888L;

	public static Timestamp get() {
        return new Timestamp();
    }   
    
    @Override
    public SQLType.Name getSQLTypeName() {     
        return SQLType.Name.TIMESTAMP;
    }
}
