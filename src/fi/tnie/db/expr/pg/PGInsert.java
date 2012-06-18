/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.pg;

import fi.tnie.db.expr.AbstractClause;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.ValueElement;
import fi.tnie.db.expr.ValueRow;
import fi.tnie.db.expr.VisitContext;
import fi.tnie.db.meta.Table;

public class PGInsert
	extends InsertStatement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4505440690619367270L;
	private ReturningClause returning;
	
	public PGInsert() {
		super();
	}
	
	public PGInsert(Table target) {
		super(target);
	}

	public PGInsert(Table target, ElementList<ColumnName> columnNameList, ValueRow valueRow) {
		super(target, columnNameList, valueRow);
	}

	public PGInsert(Table target, ElementList<ColumnName> columnNameList) {
		super(target, columnNameList);	
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {

		SQLKeyword.INSERT.traverse(vc, v);
		SQLKeyword.INTO.traverse(vc, v);
			
		getTableName().traverse(vc, v);		
		
		ElementList<ColumnName> cl = getColumnNameList();
		
		if (cl != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);	
			cl.traverse(vc, v);
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
		
		ElementList<ValueRow> vr = getValues();
		
		if (vr.isEmpty()) {
			SQLKeyword.DEFAULT.traverse(vc, v);
			SQLKeyword.VALUES.traverse(vc, v);
		}
		else {
			SQLKeyword.VALUES.traverse(vc, v);		
			getValues().traverse(vc, v);			
		}		
		
		ReturningClause rc = getReturning();
		
		if (rc != null) {
			rc.traverse(vc, v);
		}
	}

	public ReturningClause getReturning() {
		return returning;
	}

	public void setReturning(ReturningClause returning) {
		this.returning = returning;
	}

	public static class ReturningClause
		extends AbstractClause {
			
		/**
		 * 
		 */
		private static final long serialVersionUID = -7588547880513689368L;
		private ValueElement e;
	
		public ReturningClause() {
			super(PostgreSQLKeyword.RETURNING);
		}
	
		@Override
		protected Element getContent() {
	
			return null;
		}
	}

}
