/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Schema;

public class SchemaName
	extends Name {
	
//	private Schema schema;
	private Identifier catalogName;
	private Identifier schemaName;
	
		
	public SchemaName(Schema schema) {
		this(schema.getCatalog().getUnqualifiedName(), schema.getUnqualifiedName());		
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
}
