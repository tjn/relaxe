/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.QueryContext;

public abstract class NonJoinedTable
	extends AbstractTableReference {
	
	private CorrelationClause correlationClause;

	@Override
	public OrdinaryIdentifier getCorrelationName(QueryContext qctx) {
		return qctx.correlationName(this);
	}
	
	@Override
	public ElementList<? extends ColumnName> getColumnNameList() {		 		
		if ((correlationClause == null) || 
				(!correlationClause.altersColumnNames())) {
			return getUncorrelatedColumnNameList();
		}
		
		// TODO:
		
		return null;		
	}

	public class CorrelationClause
		extends CompoundElement
		implements Clause {
				
		/**
		 * 
		 */		
		private Map<SelectListElement, ColumnName> columnNameMap;
			
		private CorrelationClause() {
			super();
		}
		
		boolean altersColumnNames() {
			if ((columnNameMap == null) || columnNameMap.isEmpty()) {
				return false;
			}
			
			return true;
		}
				
		@Override
		public void traverse(VisitContext vc, ElementVisitor v) {
			v.start(vc, this);
			getCorrelationName(v.getContext());
			
			Map<SelectListElement, ColumnName> nm = this.columnNameMap;
			
			if ((nm != null) && (!nm.isEmpty())) {				
												
			}
			
			v.end(this);
		}
	
		public Map<SelectListElement, ColumnName> getColumnNameMap() {
			if (columnNameMap == null) {
				columnNameMap = new HashMap<SelectListElement, ColumnName>();				
			}
	
			return columnNameMap;
		}
		
		private ColumnName getColumnName(SelectListElement e) {
			ColumnName cn = getColumnNameMap().get(e);
			
			if (cn == null) {
				cn = e.getColumnName();
			}
			
			return cn;
		}
	}

	public Element getCorrelationClause() {
		if (correlationClause == null) {
			correlationClause = new CorrelationClause();			
		}

		return correlationClause;		
	}

}
