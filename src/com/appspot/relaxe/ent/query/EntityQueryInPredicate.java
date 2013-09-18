/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.query;

import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.op.Comparison;

public class EntityQueryInPredicate
	implements EntityQueryPredicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9214275181479579908L;
	
	private Comparison.Op op;
	private EntityQueryValueReference left;	
	private EntityQueryValueReference right;
	
	public EntityQueryInPredicate(EntityQueryValueReference left,
			EntityQueryValueReference right) {
		this(Comparison.Op.EQ, left, right);
	}
	
	public EntityQueryInPredicate(Comparison.Op op, EntityQueryValueReference left,
			EntityQueryValueReference right) {
		super();
		this.op = op;
		this.left = left;
		this.right = right;
	}

	@Override
	public Predicate predicate(EntityQueryContext ctx) {
		ValueExpression a = left.expression(ctx);
		ValueExpression b = right.expression(ctx);		
		return op.newComparison(a, b);	
	}
	
}