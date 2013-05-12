/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.EnumSet;

public enum MySQLKeyword
    implements Token {
    
    /**
     * <code>DELETE [LOW_PRIORITY] [QUICK] [IGNORE]
        FROM tbl_name[.*] [, tbl_name[.*]] ...
        <bold>USING</bold> table_references
        [WHERE where_condition]
        </code>
     */
    USING,
    ;
    

    private static EnumSet<MySQLKeyword> keywords = EnumSet.allOf(MySQLKeyword.class);
    
    @Override
	public void traverse(VisitContext vc, ElementVisitor v) {
        v.start(vc, this);
        v.end(this);        
    }
    
    @Override
    public String getTerminalSymbol() {
        return super.toString().replace('_', ' ');
    }

    @Override
    public boolean isOrdinary() {
        return true;
    }
    
    public static boolean isKeyword(String s) {
        s = s.trim().toUpperCase();     
        return keywords.contains(s);
    }
}
