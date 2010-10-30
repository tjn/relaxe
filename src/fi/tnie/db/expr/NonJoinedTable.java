/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NonJoinedTable
	extends AbstractTableReference {
	
	private CorrelationClause correlationClause;
	private SelectListElement all;

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
		
		return getCorrelationClause().getNames(); 		
	}

	public class CorrelationClause
		extends CompoundElement
		implements Clause {
		
		private ElementList<ColumnName> names;
		
		/**
		 * 
		 */		
		private Map<ColumnName, ColumnName> columnNameMap;
			
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
			
//			traverseContent(vc, v);
			
			Identifier cn = getCorrelationName(v.getContext());
			cn.traverse(vc, v);

			
			if (altersColumnNames()) {
				Symbol.PAREN_LEFT.traverse(vc, v);
				getNames().traverse(vc, v);
				Symbol.PAREN_RIGHT.traverse(vc, v);
			}
			
			v.end(this);
		}
		
		
		private ElementList<ColumnName> getNames() {
			if (!altersColumnNames()) {
				return null;				
			}
			
			if (names == null) {
				names = new ElementList<ColumnName>();
			}
			else {
				names.getContent().clear();
			}
			
			ElementList<SelectListElement> elems = new ElementList<SelectListElement>();				
			addAll(elems);
			
			List<ColumnName> nl = this.names.getContent();			
			
			for (SelectListElement e : elems.getContent()) {
				for (ColumnName n : e.getColumnNames()) {
					nl.add(getColumnName(n));
				}
			}

			return names;
		}		
	
		public Map<ColumnName, ColumnName> getColumnNameMap() {
			if (columnNameMap == null) {
				columnNameMap = new HashMap<ColumnName, ColumnName>();				
			}
	
			return columnNameMap;
		}
		
		private ColumnName getColumnName(ColumnName n) {
			ColumnName cn = getColumnNameMap().get(n);			
			return (cn == null) ? n : cn;
		}
	}

	public CorrelationClause getCorrelationClause() {
		if (correlationClause == null) {
			correlationClause = new CorrelationClause();			
		}

		return correlationClause;		
	}
	
	@Override	
	protected abstract void traverseContent(VisitContext vc, ElementVisitor v);

	@Override
	public SelectListElement getAllColumns() {
		if (all == null) {
			all = new TableColumns(this);
		}
			
		return all;
	}
	
}
