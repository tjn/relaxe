/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class MySQLDeleteStatement
	extends DeleteStatement {

//	private TableReference target;
//	private Where where;
	
	public MySQLDeleteStatement(TableReference tref, Predicate p) {
		super(tref, p);		
	}		
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		Keyword.DELETE.traverse(vc, v);		
		Keyword.FROM.traverse(vc, v);		
		TableReference tref = getTarget();				
		SchemaElementName n = tref.getTableName();
		n.traverse(vc, v);		
		MySQLKeyword.USING.traverse(vc, v);				
		tref.traverse(vc, v);	
		getWhere().traverse(vc, v);
	}
}
