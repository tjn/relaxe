/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.expr.op.ParenthesizedPredicate;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;

public class ForeignKeyJoinCondition 
	extends JoinCondition { 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4566688892782028829L;
	private ForeignKey foreignKey;
	private AbstractTableReference referencing;		
	private AbstractTableReference referenced;
	
	private Predicate condition;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private ForeignKeyJoinCondition() {
	}
	
	public ForeignKeyJoinCondition(ForeignKey foreignKey, AbstractTableReference referencing, AbstractTableReference referenced) {
		super();
		this.foreignKey = foreignKey;
		this.referencing = referencing; 
		this.referenced = referenced;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getCondition().traverse(vc, v);
	}

	private Predicate getCondition() {
		if (this.condition == null) {
			Predicate jp = null;
			
			ColumnMap cm = foreignKey.getColumnMap();
			
			for (Column a : cm.values()) {				
				Column b = foreignKey.getReferenced(a);

				jp = AndPredicate.newAnd(jp, Comparison.eq(
						new ColumnReference(referencing, a),
						new ColumnReference(referenced,  b)));				
			}
			
			
//			for (Entry<Column, Column> e : foreignKey.column().entrySet()) {
//				Column a = e.getKey();				
//				Column b = e.getValue();
//							
//				jp = AndPredicate.newAnd(jp, Comparison.eq(
//						new ColumnReference(referencing, a),
//						new ColumnReference(referenced,  b)));
//			}
			
			this.condition = jp;

		}

		return this.condition;		
	}
	
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}