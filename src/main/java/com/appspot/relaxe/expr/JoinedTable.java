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

import java.util.List;

import com.appspot.relaxe.meta.ForeignKey;

public class JoinedTable
	extends AbstractTableReference {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3584757870039445884L;
	private AbstractTableReference left;
	private AbstractTableReference right;
	private JoinType joinType;
	private JoinCondition joinCondition;
	
	private SelectListElement all;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected JoinedTable() {
	}
	
	public JoinedTable(ForeignKey fk) {
		this(fk, JoinType.INNER);
	}
	
	public JoinedTable(ForeignKey fk, JoinType joinType) {
		this(fk, joinType, false);				
	}
	
	public JoinedTable(ForeignKey fk, JoinType joinType, boolean invert) {
		if (invert) {
			this.left = new TableReference(fk.getReferenced());
			this.right = new TableReference(fk.getReferencing());
		}
		else {
			this.left = new TableReference(fk.getReferencing());
			this.right = new TableReference(fk.getReferenced());
			
		}
		this.joinType = joinType;
		this.joinCondition = new ForeignKeyJoinCondition(fk, this.left, this.right);
	}
				
	public JoinedTable(AbstractTableReference left, AbstractTableReference right,
			JoinType joinType, JoinCondition joinCondition) {
		super();
		this.left = left;
		this.right = right;
		this.joinType = joinType;
		this.joinCondition = joinCondition;
	}
	
//	protected JoinedTable(JoinType joinType) {
//		super();
//		this.joinType = joinType;
//	}

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		if (getLeft() == null) {
//			throw new NullPointerException("'left' must not be null");
//		}		
//		
//		if (getRight() == null) {
//			throw new NullPointerException("'right' must not be null");
//		}
//		
//		if (getJoinType() == null) {
//			throw new NullPointerException("'joinType' must not be null");
//		}		
//		if (getJoinCondition() == null) {
//			throw new NullPointerException("'joinCondition' must not be null");
//		}
//		
//		getLeft().generate(qc, dest);		
//		dest.append(" ");		
//		dest.append(getJoinType());
//		dest.append(" JOIN ");
//		getRight().generate(qc, dest);
//		dest.append(" ON (");
//		getJoinCondition().generate(qc, dest);
//		dest.append(") ");		
//	}

	public AbstractTableReference getLeft() {
		return this.left;
	}
	
	public AbstractTableReference getRight() {
		return this.right;
	}

	protected JoinType getJoinType() {
		return joinType;
	}

	protected JoinCondition getJoinCondition() {
		return joinCondition;
	}

	@Override
	public ElementList<? extends Identifier> getUncorrelatedColumnNameList() {
		ElementList<Identifier> names = new ElementList<Identifier>();
		
		copyColumnNameList(getLeft(), names);
		copyColumnNameList(getRight(), names);
				
		return names;
	}
	
	private void copyColumnNameList(AbstractTableReference src, ElementList<Identifier> dest) {
		if (src != null) {
			ElementList<? extends Identifier> nl = src.getUncorrelatedColumnNameList();
			
			if (!nl.isEmpty()) {
				dest.getContent().addAll(nl.getContent());
			}
		}
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getLeft().traverse(vc, v);
		getJoinType().traverse(vc, v);
		getRight().traverse(vc, v);		
		SQLKeyword.ON.traverse(vc, v);
		getJoinCondition().traverse(vc, v);	
	}

	@Override
	public void addAll(List<SelectListElement> dest) {		
		getLeft().addAll(dest);
		getRight().addAll(dest);
	}
	
	
	
	public NestedJoin nest() {
		return new NestedJoin(this);
	}

	@Override
	public ElementList<? extends Identifier> getColumnNameList() {
		return getUncorrelatedColumnNameList();
	}

	@Override
	public final OrdinaryIdentifier getCorrelationName(QueryContext qctx) {
		return null;
	}
	
	@Override
	public int getColumnCount() {
		int lc = getLeft().getColumnCount();
		int rc = getRight().getColumnCount();
				
		return lc + rc;
	}

	@Override
	public SelectListElement getAllColumns() {		 
		if (all == null) {
			/**
			 * TODO: The following does not look like serializable... 
			 */			
			all = new AllColumns() {
				/**
				 * 
				 */
				private static final long serialVersionUID = -7040630161905921350L;

				@Override
				protected TableRefList getTableRefs() {				
					return JoinedTable.this;
				};			
			};			
		}
			
		return all;
	}
}
