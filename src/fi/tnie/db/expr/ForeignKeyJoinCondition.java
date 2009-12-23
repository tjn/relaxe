/**
 * 
 */
package fi.tnie.db.expr;

import java.util.Map.Entry;

import fi.tnie.db.QueryContext;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class ForeignKeyJoinCondition 
	extends JoinCondition { 

	private ForeignKey foreignKey;
	private AbstractTableReference referencing;		
	private AbstractTableReference referenced;
	
	public ForeignKeyJoinCondition(ForeignKey foreignKey, AbstractTableReference referencing, AbstractTableReference referenced) {
		super();
		this.foreignKey = foreignKey;
		this.referencing = referencing; 
		this.referenced = referenced;			
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		Predicate jp = null;			
					
		for (Entry<Column, Column> e : foreignKey.columns().entrySet()) {
			Column a = e.getKey();				
			Column b = e.getValue();
			jp = AndPredicate.newAnd(jp, new Eq(
					new ColumnExpr(referencing, a.getName()),
					new ColumnExpr(referenced,  b.getName())));
		}
		
		jp.generate(qc, dest);
	}
}