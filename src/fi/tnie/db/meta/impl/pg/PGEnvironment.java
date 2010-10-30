/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.util.Comparator;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SimpleElement;
import fi.tnie.db.expr.Token;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.expr.ddl.ColumnDataType;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.FoldingComparator;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public class PGEnvironment
	extends DefaultEnvironment {
    
	public PGEnvironment() {
	}

	@Override
	public Comparator<Identifier> createIdentifierComparator() {
		return new FoldingComparator() {
			@Override
			protected String fold(String ordinaryIdentifier) {
				return ordinaryIdentifier.toLowerCase();
			}			
		};
	}
	
    @Override
    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
        // TODO: support big:     
        Identifier n = createIdentifier(columnName);                
        return new ColumnDefinition(n, new Serial());
    }
    
	private static class Serial
		extends SimpleElement
		implements ColumnDataType, Token {

    @Override
    public String getTerminalSymbol() {
        return "serial";
    }

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        v.start(vc, this);
        v.end(this);
    }

    @Override
    public boolean isOrdinary() {
        return true;
    }
}

}
