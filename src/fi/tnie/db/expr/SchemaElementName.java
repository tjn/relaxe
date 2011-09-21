/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.SchemaElement;

// TODO: pull up the common parts from 
// SchemaName and SchemaElement name to abstract QualifiedName class.  

public final class SchemaElementName
	extends Name {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5960754158999967085L;
	private SchemaName qualifier;
	private Identifier name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SchemaElementName() {
	}
	
	public SchemaElementName(SchemaElement e) {
		this(new SchemaName(e.getSchema()), e.getUnqualifiedName());
	}
	
	public SchemaElementName(SchemaName qualifier, Identifier name) {
		super();
		this.qualifier = qualifier;
		this.name = name;
	}
	
	public SchemaElementName(Identifier catalog, Identifier schema, Identifier name) {
		super();		
		this.qualifier = new SchemaName(catalog, schema);
		this.name = name;
	}	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
	    if (this.qualifier != null) {
	        this.qualifier.traverse(vc, v);
	        Symbol.DOT.traverse(vc, v);	        
	    }
	    
		this.name.traverse(vc, v);
	}

	public Identifier getUnqualifiedName() {
		return name;
	}
	
	/** 
	 * Returns true if this name has not qualifying schema name. 
	 * 
	 * @return
	 */	
	public boolean isRelative() {
	    return this.qualifier == null; 
	}
	
	public SchemaName getQualifier() {
	    return this.qualifier;
	}
	
}
