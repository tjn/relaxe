/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Table;

public class DeleteStatement
	extends Statement {

	private TableReference target;			
	private Where where;
				
//	public DeleteStatement(Table target) {
//		this(target, null);
//	}
	
	public DeleteStatement(TableReference tref, Predicate p) {
		super(Name.DELETE);		
		this.target = tref;
		getWhere().setSearchCondition(p);
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		Keyword.DELETE.traverse(vc, v);		
		Keyword.FROM.traverse(vc, v);
		getTarget().traverse(vc, v);		
		traverseNonEmpty(getWhere().getSearchCondition(), vc, v);
	}

	public TableReference getTarget() {
		return target;
	}	
	
	public Where getWhere() {
		if (where == null) {
			where = new Where();			
		}

		return where;
	}
}
