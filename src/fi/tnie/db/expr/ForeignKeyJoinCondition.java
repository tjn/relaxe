/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr;

import fi.tnie.db.expr.op.AndPredicate;
import fi.tnie.db.expr.op.Comparison;
import fi.tnie.db.expr.op.ParenthesizedPredicate;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ColumnMap;
import fi.tnie.db.meta.ForeignKey;

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