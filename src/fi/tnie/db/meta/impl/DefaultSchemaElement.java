/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Comparator;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaElement;

public class DefaultSchemaElement
	implements SchemaElement {

	private Identifier name;
	private DefaultMutableSchema schema;
	private SchemaElementName qualifiedName;
	
	DefaultSchemaElement(DefaultMutableSchema schema, Identifier name) {		
		if (schema == null) {
			throw new NullPointerException("'schema' must not be null");
		}
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		this.schema = schema;
		this.name = name;
	}
	
	public DefaultMutableSchema getMutableSchema() {
		return this.schema;
	}
		
	public Schema getSchema() {
		return getMutableSchema();
	}

	@Override
	public String getQualifiedName() {
		return getName().generate();	
	}

	@Override
	public Identifier getUnqualifiedName() {
		return this.name;
	}

	@Override
	public SchemaElementName getName() {
		if (qualifiedName == null) {
			qualifiedName = new SchemaElementName(this);
		}

		return qualifiedName;
	}
	
	protected <E extends SchemaElement> DefaultSchemaElementMap<E> createElementMap() {		
		return new DefaultSchemaElementMap<E>(getSchema().getCatalog().getEnvironment());		
	}
	
	protected Comparator<Identifier> identifierComparator() {
		return getEnvironment().identifierComparator();
	}
	
	protected Environment getEnvironment() {
		return getSchema().getCatalog().getEnvironment();
	}
	
	
}
