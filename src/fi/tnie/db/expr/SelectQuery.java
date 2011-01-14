/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


/**
 * Is this class duplicate with DefaultTableExpression?
 * @author tnie
 *
 */
public class SelectQuery
	extends QueryExpression {

	private TableExpression tableExpr;
	private OrderBy orderBy;
	private Limit limit;
	private Offset offset;
	
	@Override
	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	@Override	
	public TableExpression getTableExpr() {
		return tableExpr;
	}

	public void setTableExpr(TableExpression tableExpr) {
		this.tableExpr = tableExpr;
	}
	
	public TableExpression nest() {
		return new NestedSelect(this);
	}	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		getTableExpr().traverse(vc, v);
		
		OrderBy o = getOrderBy();
						
		if (o != null) {
			o.traverse(vc, v);		
		}
		
		Limit limit = getLimit();
		
		if (limit != null) {
			limit.traverse(vc, v);
		}
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	public Offset getOffset() {
		return offset;
	}

	public void setOffset(Offset offset) {
		this.offset = offset;
	}

	
}
