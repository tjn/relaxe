/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.expr;

public abstract class NonJoinedTable
	extends AbstractTableReference {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5713534855741377091L;
	private CorrelationClause correlationClause;
	private SelectListElement all;
	
	public NonJoinedTable() {		
	}

	@Override
	public OrdinaryIdentifier getCorrelationName(QueryContext qctx) {
		return qctx.correlationName(this);
	}
	
	
	
	@Override
	public ElementList<Identifier> getColumnNameList() {
		CorrelationClause cc = getCorrelationClause();
		ElementList<Identifier> nl = (cc == null) ? null : cc.getDerivedColumnList();
		
		
		if (nl == null) { 
			return getUncorrelatedColumnNameList();
		}
		
		return nl;
	}

	public static class CorrelationClause
		extends CompoundElement
		implements Clause {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3876482034646951163L;
		private ElementList<Identifier> derivedColumnList;
		private NonJoinedTable nonJoinedTable;
		
//		/**
//		 * 
//		 */		
//		private Map<Identifier, Identifier> columnNameMap;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		public CorrelationClause() {	
		}
			
		public CorrelationClause(NonJoinedTable nonJoinedTable, ElementList<Identifier> derivedColumnList) {
			super();
			this.nonJoinedTable = nonJoinedTable;
			this.derivedColumnList = derivedColumnList;
		}
		
//		boolean altersColumnNames() {
//			if ((columnNameMap == null) || columnNameMap.isEmpty()) {
//				return false;
//			}
//			
//			return true;
//		}
		
		public ElementList<Identifier> getDerivedColumnList() {
			return derivedColumnList;
		}
				
		@Override
		public void traverse(VisitContext vc, ElementVisitor v) {
			v.start(vc, this);
			
//			traverseContent(vc, v);
			
			Identifier cn = nonJoinedTable.getCorrelationName(v.getContext());
			cn.traverse(vc, v);
			
			ElementList<Identifier> dcl = getDerivedColumnList();
			
			if (dcl != null) {
				Symbol.PAREN_LEFT.traverse(vc, v);
				dcl.traverse(vc, v);
				Symbol.PAREN_RIGHT.traverse(vc, v);
			}
			
			v.end(this);
		}
		
		
//		private ElementList<Identifier> getNames() {
//			if (!altersColumnNames()) {
//				return null;				
//			}
//			
//			if (names == null) {
//				names = new ElementList<Identifier>();
//			}
//			else {
//				names.getContent().clear();
//			}
//			
//			// ElementList<SelectListElement> elems = new ElementList<SelectListElement>();				
//			List<SelectListElement> elems = new ArrayList<SelectListElement>();
//			nonJoinedTable.addAll(elems);
//			
//			List<Identifier> nl = this.names.getContent();			
//			
//			for (SelectListElement e : elems) {
//				for (Identifier n : e.getColumnNames()) {
//					nl.add(getColumnName(n));
//				}
//			}
//
//			return names;
//		}		
	
//		public Map<Identifier, Identifier> getColumnNameMap() {
//			if (columnNameMap == null) {
//				columnNameMap = new HashMap<Identifier, Identifier>();				
//			}
//	
//			return columnNameMap;
//		}
		
//		private Identifier getColumnName(Identifier n) {
//			Identifier cn = getColumnNameMap().get(n);			
//			return (cn == null) ? n : cn;
//		}
	}

	public CorrelationClause getCorrelationClause() {
		if (correlationClause == null) {
			correlationClause = new CorrelationClause(this, null);			
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
