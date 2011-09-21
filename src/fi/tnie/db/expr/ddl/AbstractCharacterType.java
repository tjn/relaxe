/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.VisitContext;

public abstract class AbstractCharacterType
    extends SQLType {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7291068267695357641L;
	private Specification length = null;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractCharacterType() {
	}
    
    protected AbstractCharacterType(int length) {                        
        if (length < 1) {
            throw new IllegalArgumentException("illegal varchar length: " + length);
        }
        
        this.length = new Specification(length);
    }    
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        getName().traverse(vc, v);
        Symbol.PAREN_LEFT.traverse(vc, v);
        this.length.traverse(vc, v);        
        Symbol.PAREN_RIGHT.traverse(vc, v);
    }
}
