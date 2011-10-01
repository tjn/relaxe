/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.ent.QueryExpressionSource;


/** 
 * @author tnie
 */
public class SelectStatement
	extends Statement
	implements QueryExpression, QueryExpressionSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 540136231677444030L;
	private TableExpression tableExpression;
	
	private OrderBy orderBy;
	private Limit limit;
	private Offset offset;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SelectStatement() {
	}
	
	public SelectStatement(Select s, From f, Where w) {
		this((TableExpression) new DefaultTableExpression(s, f, w, null));
	}
	
	public SelectStatement(QueryExpression qe) {
		this(qe.getTableExpr(), qe.getOrderBy(), qe.getLimit(), qe.getOffset());
	}
	
	public SelectStatement(TableExpression te) {
		this(te, null, null, null);
	}
		
	public SelectStatement(TableExpression te, OrderBy orderBy, Limit limit, Offset offset) {
		super(Name.SELECT);
		this.tableExpression = te;
		this.orderBy = orderBy;
		this.limit = limit;		
		this.offset = offset;
	}

	
	public TableExpression nest() {
		return new NestedSelect(this);
	}	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		
	
//		getQueryExpression().traverse(vc, v);
		
		getTableExpr().traverse(vc, v);
		
		OrderBy o = getOrderBy();
						
		if (o != null) {
			o.traverse(vc, v);		
		}
		
		Limit limit = getLimit();
		
		if (limit != null) {
			limit.traverse(vc, v);
			
			Offset off = getOffset();
			
			if (off != null) {
				off.traverse(vc, v);
			}			
		}
	}

	@Override
	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	@Override
	public Offset getOffset() {
		return offset;
	}

	public void setOffset(Offset offset) {
		this.offset = offset;
	}

	public QueryExpression getQueryExpression() {
		return this;
	}
	
	@Override
	public OrderBy getOrderBy() {
		return this.orderBy;
	}
	
	@Override
	public TableExpression getTableExpr() {
		return this.tableExpression;
	}
}
