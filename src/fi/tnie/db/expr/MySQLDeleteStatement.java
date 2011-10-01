/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class MySQLDeleteStatement
	extends DeleteStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2492700739356278591L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected MySQLDeleteStatement() {
	}
	
	public MySQLDeleteStatement(TableReference tref, Predicate p) {
		super(tref, p);		
	}		
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.DELETE.traverse(vc, v);		
		SQLKeyword.FROM.traverse(vc, v);		
		TableReference tref = getTarget();				
		SchemaElementName n = tref.getTableName();
		n.traverse(vc, v);		
		MySQLKeyword.USING.traverse(vc, v);				
		tref.traverse(vc, v);	
		getWhere().traverse(vc, v);
	}
}
