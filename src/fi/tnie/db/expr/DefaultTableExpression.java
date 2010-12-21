/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.expr.SetOperator.Op;

/**
 * Table expression implementation
 * @author Administrator
 */

public class DefaultTableExpression
	extends QueryExpression
	implements TableExpression {

	private Select select;
	private From from;
	private Where where;
	private GroupBy groupBy;
	private Having having;
	
	private SelectListElement all = null;
	
	public DefaultTableExpression() {
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

	public From getFrom() {
		return from;
	}

	public void setFrom(From from) {
		this.from = from;
	}

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
				@Override
				protected TableRefList getTableRefs() {
					return getFrom().getTableReferenceList();				
				}			
			};			
		}

		return all;
	}
	
	@Override
	public OrderBy getOrderBy() {	
		return null;
	}
	
	@Override
	public TableExpression getTableExpr() {
		return this;
	}
}
