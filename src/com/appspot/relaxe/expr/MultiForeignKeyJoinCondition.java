/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import java.util.Map;

import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.expr.op.ParenthesizedPredicate;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;

public class MultiForeignKeyJoinCondition 
	extends JoinCondition { 

	/**
	 * 
	 */
	private static final long serialVersionUID = -4566688892782028829L;
	private AbstractTableReference referencing;
	
	private Map<ForeignKey, TableReference> foreignKeyReferenceMap;
				
	private Predicate condition;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private MultiForeignKeyJoinCondition() {
	}
	
	public MultiForeignKeyJoinCondition(AbstractTableReference referencing, Map<ForeignKey, TableReference> foreignKeyReferenceMap) {
		super();
		this.referencing = referencing;
		this.foreignKeyReferenceMap = foreignKeyReferenceMap;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getCondition().traverse(vc, v);
	}

	private Predicate getCondition() {
		if (this.condition == null) {
			Predicate jp = null;
			
			for (Map.Entry<ForeignKey, TableReference> e : foreignKeyReferenceMap.entrySet()) {
				ForeignKey key = e.getKey();
				AbstractTableReference ref = e.getValue();
				
				for (Column a : key.getColumnMap().values()) {									
					Column b = key.getReferenced(a);
					
					Comparison pc = Comparison.eq(
							new ColumnReference(referencing, a),
							new ColumnReference(ref,  b));
								
					jp = AndPredicate.newAnd(jp, pc);
				}								
			}
			
			this.condition = jp;
		}

		return this.condition;		
	}
		
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}