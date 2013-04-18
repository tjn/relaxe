/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Schema;

public final class SchemaName
	extends Name {

	/**
	 * 
	 */
	private static final long serialVersionUID = 231673251640312834L;
	private Identifier catalogName;

	// TODO: rename me to unqualifiedName 
	private Identifier schemaName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public SchemaName() {
	}
		
	public SchemaName(Schema schema) {
		this(schema, true);		
	}
	
	public SchemaName(Schema schema, boolean relative) {
		this(relative ? null : schema.getCatalogName(), schema.getUnqualifiedName());		
	}
	
	public SchemaName(Identifier catalogName, Identifier schemaName) {
		super();
		
		if (schemaName == null) {
			throw new NullPointerException();
		}
		
		this.catalogName = catalogName;		
		this.schemaName = schemaName;		
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);		
		Identifier cn = getCatalogName();
		
		if (cn != null) {
			cn.traverse(vc, v);
			Symbol.DOT.traverse(vc, v);
		}
		
		getSchemaName().traverse(vc, v);		
		v.end(this);
	}
	
	public Identifier getCatalogName() {
		return this.catalogName;
	}
	
	public Identifier getSchemaName() {
		return this.schemaName;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) {
//			throw new NullPointerException();
//		}
//						
//		Comparator<Identifier> icmp = 
//			getSchema().getCatalog().identifierComparator();
//		
//		SchemaName n = (SchemaName) obj;
//				
//		return 
//			icmp.compare(n.getCatalogName(), this.getCatalogName()) == 0 && 
//			icmp.compare(n.getSchemaName(), this.getSchemaName()) == 0;
//	}
	

	 /** 
     * Returns true if this name has not qualifying catalog name. 
     * 
     * @return
     */ 
    public boolean isRelative() {
        return this.catalogName == null; 
    }
    
    public SchemaName toRelative() {
        return (this.catalogName == null) ? this : new SchemaName(null, this.schemaName); 
    }
}
