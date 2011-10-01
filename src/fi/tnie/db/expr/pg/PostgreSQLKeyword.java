/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.pg;

import java.util.EnumSet;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.VisitContext;

public enum PostgreSQLKeyword
    implements Keyword {
    
    /**
     * <code>
     * INSERT INTO table [ ( column [, ...] ) ]
    	{ DEFAULT VALUES | VALUES ( { expression | DEFAULT } [, ...] ) [, ...] | query }
    	[ RETURNING * | output_expression [ AS output_name ] [, ...] ]
       </code>
     */
    RETURNING,
    ;
    

    private static EnumSet<PostgreSQLKeyword> keywords = EnumSet.allOf(PostgreSQLKeyword.class);
    
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
