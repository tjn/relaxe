/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

public class Varchar
    extends AbstractCharacterType {
    
//    public static final Varchar VARCHAR_10 = new Varchar(10);
//    public static final Varchar VARCHAR_20 = new Varchar(20);
//    public static final Varchar VARCHAR_30 = new Varchar(30);
//    public static final Varchar VARCHAR_40 = new Varchar(40);
//    public static final Varchar VARCHAR_50 = new Varchar(50);
//    public static final Varchar VARCHAR_100 = new Varchar(100);
    
    public static Varchar get(int length) {
        return new Varchar(length);
    }
    
    public Varchar(int length) {
        super(length);
    }
    
    @Override
    public SQLType.Name getSQLTypeName() {     
        return SQLType.Name.VARCHAR;
    }
}
