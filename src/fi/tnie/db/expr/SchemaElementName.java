/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.SchemaElement;

public final class SchemaElementName
	extends Name {
	
	private SchemaName qualifier;
	private Identifier name;
	
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
		this.qualifier.traverse(vc, v);
		Symbol.DOT.traverse(vc, v);
		this.name.traverse(vc, v);
	}

	public Identifier getUnqualifiedName() {
		return name;
	}
		
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) {
//			throw new NullPointerException();
//		}
//			
//		Comparator<Identifier> icmp = 
//			qualifier.getSchema().getCatalog().identifierComparator();
//		
//		SchemaElementName n = (SchemaElementName) obj;
//							
//		return 
//		  n.qualifier.equals(this.qualifier) && 
//		  icmp.compare(n.getUnqualifiedName(), name) == 0;		
//	}
	
}
