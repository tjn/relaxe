/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.expr.SetOperator.Op;

/**
 * Table expression implementation
 * @author Administrator
 */

public class DefaultTableExpression
	extends AbstractQueryExpression
	implements TableExpression, QueryExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1839724451652406829L;
	
	private Select select;
	private From from;
	private Where where;
	private GroupBy groupBy;
	private Having having;	
	// private SelectListElement all = null;
	
	public DefaultTableExpression() {
	}
	
	public DefaultTableExpression(TableExpression e) {
		this(e.getSelect(), e.getFrom(), e.getWhere(), e.getGroupBy());
	}
	
	public DefaultTableExpression(Select select, From from) {
		this(select, from, null, null);
	}
	
	
	public DefaultTableExpression(Select select, From from, Where where, GroupBy groupBy) {
		super();
		
		if (select == null) {
			throw new NullPointerException("select");
		}
		
		this.select = select;
		this.from = from;
		this.where = where;
		this.groupBy = groupBy;
	}



	private void traverse(Element e, VisitContext vc, ElementVisitor v) {
		if (e != null) {
			e.traverse(vc, v);
		}
	}
	
	@Override
	public Select getSelect() {
		return select;
	}

	@Override
	public From getFrom() {
		return from;
	}

	@Override
	public Where getWhere() {
		return where;
	}

	public Having getHaving() {
		return having;
	}

	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		traverse(getSelect(), vc, v);
		traverse(getFrom(), vc, v);
		traverse(getWhere(), vc, v);
		traverse(getGroupBy(), vc, v);
		traverse(getHaving(), vc, v);
	}

	@Override
	public GroupBy getGroupBy() {
		return groupBy;
	}
	
	public SetOperator unionAll(TableExpression rp) {
		return new SetOperator(Op.UNION, true, this, rp);
	}
	
	public SetOperator union(TableExpression rp) {
		return new SetOperator(Op.UNION, false, this, rp);
	}
	
	public SetOperator intersect(TableExpression rp) {
		return new SetOperator(Op.INTERSECT, false, this, rp);
	}
	
	public SetOperator intersectAll(TableExpression rp) {
		return new SetOperator(Op.INTERSECT, true, this, rp);
	}
	
	public SetOperator except(TableExpression rp) {
		return new SetOperator(Op.EXCEPT, false, this, rp);
	}
	
	public SetOperator exceptAll(TableExpression rp) {
		return new SetOperator(Op.EXCEPT, true, this, rp);
	}
	
//	public void selectAll() {		 	
//		getSelect().getSelectList().set(getAll());
//	}
	
//	private SelectListElement getAll() {
//		if (all == null) {
//			all = new AllColumns() {
//				/**
//				 * TODO: The following does not look like serializable... 
//				 */
//				private static final long serialVersionUID = -98472738386271356L;
//
//				@Override
//				protected TableRefList getTableRefs() {
//					return getFrom().getTableReferenceList();				
//				}			
//			};			
//		}
//
//		return all;
//	}
	
	@Override
	public final OrderBy getOrderBy() {	
		return null;
	}
	
	@Override
	public final Limit getLimit() {	
		return null;
	}
	
	@Override
	public final Offset getOffset() {	
		return null;
	}
	
	@Override
	public TableExpression getTableExpr() {
		return this;
	}
}
