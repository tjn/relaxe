/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.Serializable;
import java.util.Comparator;

import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.DelimitedIdentifier;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.expr.SimpleElement;
import fi.tnie.db.expr.Token;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.expr.ddl.ColumnDataType;
import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.Folding;
import fi.tnie.db.meta.FoldingComparator;
import fi.tnie.db.meta.SerializableEnvironment;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public class PGEnvironment
	extends DefaultEnvironment
	implements SerializableEnvironment {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 6323320738822722962L;
	
	private static Environment environment = new PGEnvironment();
	
	
	public static Environment environment() {
		return PGEnvironment.environment;
	}

	public PGEnvironment() {
	}

	@Override
	public Comparator<Identifier> createIdentifierComparator() {
		return FoldingComparator.LOWERCASE;
	}	
	
    
    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
        // TODO: support big:     
        Identifier n = createIdentifier(columnName);                
        return new ColumnDefinition(n, new Serial());
    }
    
	private static class Serial
		extends SimpleElement
		implements ColumnDataType, Token {

	    /**
		 * 
		 */
		private static final long serialVersionUID = -1177806475050170208L;

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
	
	
	@Override
	public Identifier createIdentifier(String name)
			throws IllegalIdentifierException {
		return (name == null) ? null : AbstractIdentifier.create(name, Folding.LOWERCASE);
	}
}
