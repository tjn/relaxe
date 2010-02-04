/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Table;

public class UpdateQuery
	extends CompoundElement {

	private TableReference target;			
	private ElementList<Assignment> assignmentClause;
	private Where where;
				
	public UpdateQuery(Table target, ElementList<Assignment> assignmentClause) {
		super();		
		this.target = new TableReference(target);
		this.assignmentClause = assignmentClause;
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		Keyword.UPDATE.traverse(vc, v);		
		getTarget().traverse(vc, v);		
		Keyword.SET.traverse(vc, v);
		assignmentClause.traverse(vc, v);
		traverseNonEmpty(getWhere().getSearchCondition(), vc, v);
	}

	public TableReference getTarget() {
		return target;
	}	
	
	public String generate() {
		StringBuffer dest = new StringBuffer();
		ElementVisitor v = new QueryGenerator(dest);
		traverse(null, v);		
		return dest.toString();
	}

	public ElementList<Assignment> getAssignmentList() {
		if (assignmentClause == null) {
			assignmentClause = new ElementList<Assignment>();			
		}

		return assignmentClause;
	}
	
	public Where getWhere() {
		if (where == null) {
			where = new Where();			
		}

		return where;
	}
}
