/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaElementName;

public class ImmutableSchemaElement
	implements SchemaElement
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5769270673777439062L;
	private Environment environment;
	private SchemaElementName name;		
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableSchemaElement() {
	}
		
	protected ImmutableSchemaElement(Environment environment, SchemaElementName name) {
		super();
		this.environment = environment;		
		this.name = name;
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public SchemaElementName getName() {
		return this.name;
	}

	@Override
	public String getQualifiedName() {
		return this.name.generate();
	}

	@Override
	public Identifier getUnqualifiedName() {
		return this.name.getUnqualifiedName();
	}	
}
