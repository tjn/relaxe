/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.meta.IdentifierRules;


public class Select
	extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8112104453171815591L;
	
	private ElementList<SelectListElement> selectList;	
	private boolean distinct;	
	

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private Select() {
	}
	
	public Select(ValueExpression ve) {
		this(new ValueElement(ve));  
	}
	
	public Select(SelectListElement elem) {
		this.distinct = false;
		this.selectList = new ElementList<SelectListElement>(Collections.singleton(elem));  
	}
	
	public Select(ElementList<SelectListElement> elements, boolean distinct) {
		if (elements == null) {
			throw new NullPointerException("elements");
		}
		
		this.selectList = elements;
		this.distinct = distinct;		 
	}
	
	public Select(ElementList<SelectListElement> elements) {
		this(elements, false);
	}
		

	public ElementList<SelectListElement> getSelectList() {
		return selectList;
	}
	
	public int getColumnCount() {
		int cc = 0;
		
		for(SelectListElement e : getSelectList().getContent()) {
			cc += e.getColumnCount();
		}
		
		return cc;
	}
	
	public List<ValueExpression> expandValueExprList() {
		ArrayList<ValueExpression> el = new ArrayList<ValueExpression>();
		
		for(SelectListElement e : getSelectList().getContent()) {
			int cc = e.getColumnCount();
			
			for (int i = 1; i <= cc; i++) {
				el.add(e.getColumnExpr(i));
			}
		}		
		
		return el;
	}
	
	/**
	 * NOTE: selected list contains <code>null</code> element
	 * for each column which is not a column reference.  
	 * 
	 * @return
	 */	
	public List<ColumnExpr> expandColumnExprList() {
		ArrayList<ColumnExpr> el = new ArrayList<ColumnExpr>();
		
		for(SelectListElement e : getSelectList().getContent()) {
			int cc = e.getColumnCount();
			
			for (int i = 1; i <= cc; i++) {								
				el.add(e.getTableColumnExpr(i));
			}
		}		
		
		return el;
	}

//	public void setDistinct(boolean distinct) {
//		this.distinct = distinct;
//	}

	public boolean isDistinct() {
		return distinct;
	}
	
//	public SelectListElement add(SelectListElement e) {
//		if (e == null) {
//			throw new NullPointerException("'e' must not be null");
//		}
//		
//		getSelectList().add(e);
//		return e;
//	}
	
	
	
	
		
	public ElementList<? extends Identifier> getColumnNameList() {		
		ElementList<Identifier> cl = null;
		ElementList<SelectListElement> p = getSelectList();
		
		if (!p.isEmpty()) {
			cl = new ElementList<Identifier>();
			
			for (SelectListElement e : p.getContent()) {
				cl.getContent().addAll(e.getColumnNames());				
			}
		}
		
		return cl;
	}
	
	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		SQLKeyword.SELECT.traverse(vc, v);
	}
	
public static class Builder {
		
		private List<SelectListElement> elementList;
		private boolean distinct;
		
		private List<SelectListElement> getElementList() {
			if (elementList == null) {
				elementList = new ArrayList<SelectListElement>();				
			}

			return elementList;
		}  
	
		public ColumnReference add(ColumnReference expr) {		
			if (expr == null) {
				throw new NullPointerException("'expr' must not be null");
			}
					
			getElementList().add(expr);
			
			return expr;
		}
		
		public ValueElement add(ValueExpression expr) {
			return add(expr, (Identifier) null);
		}
		
		public ValueElement add(ValueExpression expr, Identifier newName) {
			if (expr == null) {
				throw new NullPointerException("'expr' must not be null");
			}
			
			ValueElement e = new ValueElement(expr, newName);
			getElementList().add(e);
			return e;
		}	
		
		
				
		public boolean isDistinct() {
			return distinct;
		}

		public void setDistinct(boolean distinct) {
			this.distinct = distinct;
		}
		
		public Builder() {			
		}

		public Builder(boolean distinct) {
			super();
			this.distinct = distinct;
		}

		public ValueElement add(ValueExpression expr, String newName, IdentifierRules ir) 
			throws IllegalIdentifierException {
			Identifier n = null;
			
			if (newName != null) {
				n = ir.toIdentifier(newName);
			}
			
			return add(expr, n);		
		}	
		
		public Select newSelect() {
			return new Select(new ElementList<SelectListElement>(getElementList()), isDistinct());			
		}
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		traverseClause(vc, v);
		
		if (this.distinct) {
			SQLKeyword.DISTINCT.traverse(vc, v);
		}
		
		getContent().traverse(vc, v);	
	}
	
	@Override
	protected Element getContent() {		
		return getSelectList();
	}	
}
