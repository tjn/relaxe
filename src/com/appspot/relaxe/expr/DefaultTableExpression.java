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
	private SelectListElement all = null;
	
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

	public void setSelect(Select select) {
		this.select = select;
	}

	@Override
	public Select getSelect() {
		return select;
	}

	@Override
	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	@Override
	public Where getWhere() {
		if (where == null) {
			where = new Where();			
		}

		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public Having getHaving() {
		return having;
	}

	public void setHaving(Having having) {
		this.having = having;
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

	public void setGroupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	
	@Override
	public String generate() {
		StringBuffer dest = new StringBuffer();
		ElementVisitor v = new QueryGenerator(dest);
		traverse(null, v);		
		return dest.toString();
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
	
	public void selectAll() {		 	
		getSelect().getSelectList().set(getAll());
	}
	
	private SelectListElement getAll() {
		if (all == null) {
			all = new AllColumns() {
				/**
				 * TODO: The following does not look like serializable... 
				 */
				private static final long serialVersionUID = -98472738386271356L;

				@Override
				protected TableRefList getTableRefs() {
					return getFrom().getTableReferenceList();				
				}			
			};			
		}

		return all;
	}
	
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