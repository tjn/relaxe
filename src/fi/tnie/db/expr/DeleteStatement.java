/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class DeleteStatement
	extends Statement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7983755125216586175L;

	private TableReference target;		
	
	private Where where;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DeleteStatement() {
	}
	
	public DeleteStatement(TableReference tref, Predicate p) {
		super(Name.DELETE);		
		this.target = tref;
		getWhere().setSearchCondition(p);
	}
	
//	/**
//	 * This is an addtion for MySQL which does not seem to support 
//	 * table references in DELETE's
//	 * 
//	 * @param table
//	 * @param p
//	 */
//    public DeleteStatement(Table table, Predicate p) {
//        super(Name.DELETE);     
//        this.target = table.getName();        
//        getWhere().setSearchCondition(p);
//    }	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		Keyword.DELETE.traverse(vc, v);		
		Keyword.FROM.traverse(vc, v);
		getTarget().traverse(vc, v);	
		getWhere().traverse(vc, v);
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
