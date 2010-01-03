/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr;

import java.util.Map.Entry;

import fi.tnie.db.expr.op.AndPredicate;
import fi.tnie.db.expr.op.Comparison;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class ForeignKeyJoinCondition 
	extends JoinCondition { 

	private ForeignKey foreignKey;
	private AbstractTableReference referencing;		
	private AbstractTableReference referenced;
	
	private Predicate condition;
	
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
			
			for (Entry<Column, Column> e : foreignKey.columns().entrySet()) {
				Column a = e.getKey();				
				Column b = e.getValue();
							
				jp = AndPredicate.newAnd(jp, Comparison.eq(
						new TableColumnExpr(referencing, a),
						new TableColumnExpr(referenced,  b)));
			}
			
			this.condition = jp;

		}

		return this.condition;		
	}
}