/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

public class Char 
    extends AbstractCharacterType {
    
    public Char(int length) {
        super(length);
    }
    
    @Override
    public SQLType.Name getSQLTypeName() {
        return SQLType.Name.CHAR;
    }
}
