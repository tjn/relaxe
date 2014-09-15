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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;

public abstract class AbstractRowValueConstructor<E extends Element>
	extends CompoundElement implements RowValueConstructor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2415272956499443256L;

	/**
	 * No-argument constructor for GWT Serialization.
	 */
	protected AbstractRowValueConstructor() {
	}
	
	private E content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractRowValueConstructor(E content) {
		this.content = content;
	}
	
	public E getContent() {
		return content;
	}
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		Symbol.PAREN_LEFT.traverse(vc, v);
		getContent().traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
	}
	
	
	public static RowValueConstructor of(RowValueConstructorElement element) {
		return new ElementRowValueConstructor(element); 
	}
	
	public static RowValueConstructor of(Collection<? extends RowValueConstructorElement> elementList) {
		return new ElementListRowValueConstructor(ElementList.newElementList(elementList)); 
	}	
	
	public static RowValueConstructor of(ElementList<? extends RowValueConstructorElement> elementList) {
		return new ElementListRowValueConstructor(elementList); 
	}
	
	public static RowValueConstructor of(QueryExpression queryExpression) {
		return new SubqueryRowValueConstructor(queryExpression); 
	}
	
	public static RowValueConstructor of(TableReference tref, ColumnMap columnMap) {
		
		int cc = columnMap.size();
		
		ElementList<ColumnReference> el = null;
		
		if (cc == 1) {			
			Column col = columnMap.get(0);			
			el = ElementList.newElementList(new ColumnReference(tref, col));
		}
		else {
			List<ColumnReference> cl = new ArrayList<ColumnReference>(cc);
			
			for (int i = 0; i < cc; i++) {
				Column col = columnMap.get(i);				
				cl.add(new ColumnReference(tref, col));
			}
			
			el = ElementList.newElementList(cl);			
		}
				
		RowValueConstructor rvc = of(el);
		
		return rvc;		
	}		

	
	
	public static class ElementRowValueConstructor
		extends AbstractRowValueConstructor<RowValueConstructorElement> {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5469931734160856649L;
		

		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private ElementRowValueConstructor() {
		}

		public ElementRowValueConstructor(RowValueConstructorElement content) {
			super(content);
		}
		
		@Override
		protected void traverseContent(VisitContext vc, ElementVisitor v) {
			getContent().traverse(vc, v);
		}
	}
	
	public static class ElementListRowValueConstructor
		extends AbstractRowValueConstructor<ElementList<? extends RowValueConstructorElement>> {
	
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5469931734160856649L;
		
	
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private ElementListRowValueConstructor() {
		}
	
		public ElementListRowValueConstructor(ElementList<? extends RowValueConstructorElement> content) {
			super(content);
		}		
	
		public ElementListRowValueConstructor(Collection<? extends RowValueConstructorElement> values) {
			super(ElementList.<RowValueConstructorElement>newElementList(Symbol.COMMA, values));
		}
		
		@Override
		protected void traverseContent(VisitContext vc, ElementVisitor v) {
			Symbol.PAREN_LEFT.traverse(vc, v);
			getContent().traverse(vc, v);
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}
	}	
		
	public static class SubqueryRowValueConstructor
		extends AbstractRowValueConstructor<QueryExpression> {
	
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5469931734160856649L;
		
	
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private SubqueryRowValueConstructor() {
		}
	
		public SubqueryRowValueConstructor(QueryExpression content) {
			super(content);
		}
	}
	
	
	

}
