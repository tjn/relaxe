/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Comparator;

import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;

public abstract class DefaultEnvironment 
	implements SerializableEnvironment {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6518870057408097303L;
	private Comparator<Identifier> identifierComp;
	
	private final SchemaElementMap<ForeignKey> emptyForeignKeyMap = new EmptyForeignKeyMap(this); 
	
			
	@Override
	public Identifier createIdentifier(String name)
			throws IllegalIdentifierException {
		return (name == null) ? null : AbstractIdentifier.create(name);
	}

	@Override
	public final Comparator<Identifier> identifierComparator() {
		if (identifierComp == null) {
			identifierComp = createIdentifierComparator();			
		}

		return identifierComp;		
	}	
	
	protected Comparator<Identifier> createIdentifierComparator() {
		return FoldingComparator.UPPERCASE;
	}
	
		
	public SchemaElementMap<ForeignKey> emptyForeignKeyMap()  {
		return this.emptyForeignKeyMap;
	}
	
	
}
