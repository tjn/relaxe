/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.CompoundElement;
import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.NiladicFunction;

import fi.tnie.db.expr.VisitContext;

public class DefaultDefinition
    extends CompoundElement {

    private Element value; 
    
//    TODO: DefaultDefinition(Literal) {
//    }
    
    public DefaultDefinition() {
        this.value = Keyword.NULL;
    }

    public DefaultDefinition(NiladicFunction nf) {
        if (nf == null) {
            throw new NullPointerException("'nf' must not be null");
        }
        
        this.value = nf;
    }
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        Keyword.DEFAULT.traverse(vc, v);
        this.value.traverse(vc, v);
    }
}
