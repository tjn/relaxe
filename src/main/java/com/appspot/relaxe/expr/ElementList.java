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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class ElementList<E extends Element>
	extends CompoundElement implements Iterable<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9070139545586143988L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ElementList() {
	}
	
	public abstract int size();	
	public abstract E get(int index);
	
	public static <E extends Element> ElementList<E> newElementList(E element) {
		return new OneElementList<E>(element);		
	}
	
	public static <E extends Element> ElementList<E> newElementList(Collection<E> elements) {
		return newElementList(Symbol.COMMA, elements);
	}
	
	public static <E extends Element> ElementList<E> newElementList(Symbol delim, Collection<? extends E> elements) {
		if (elements == null) {
			throw new NullPointerException("values");
		}
		
		if (elements.isEmpty()) {
			throw new IllegalArgumentException("element list must not be empty");
		}
		
		return new ListElementList<E>(delim, elements);		
	}
	
	
	private static class ElementListIterator<E extends Element>
		implements Iterator<E> {
		
		private int index;		
		private ElementList<E> elementList;		
		
		public ElementListIterator(ElementList<E> elementList) {
			this.elementList = elementList;
		}

		@Override
		public boolean hasNext() {
			return (this.index < elementList.size());
		}

		@Override
		public E next()
			throws NoSuchElementException {						
			if (this.index >= elementList.size()) {				
				throw new NoSuchElementException();
			}	
			E next = elementList.get(index);			
			this.index++;
			
			return next;			
		}

		@Override
		public void remove() {
			 throw new UnsupportedOperationException();			
		}	
		
	}
	
	
	@Override
	public Iterator<E> iterator() {
		return new ElementListIterator<E>(this);
	}
	
	
	public static class OneElementList<E extends Element>
		extends ElementList<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1844583016467280335L;
		/**
		 * 
		 */
		private E element;
			
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private OneElementList() {
			super();		
		}
		
		public OneElementList(E element) {			
			if (element == null) {
				throw new NullPointerException("element");
			}
			
			this.element = element;
		}

		@Override
		public int size() {
			return 1;			
		}

		@Override
		public E get(int index) {
			if (index != 0) {
				throw new IndexOutOfBoundsException(Integer.toString(index));
			}
			
			return element;
		}
		
		@Override
		protected void traverseContent(VisitContext vc, ElementVisitor v) {
			this.element.traverse(vc, v);
		}
	}
	
	
	
	
	public static class ListElementList<E extends Element>
		extends ElementList<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2475653227870417408L;
		
		private Symbol delim;
		private List<E> content;
		
		protected ListElementList(Collection<? extends E> elems) {
			this(Symbol.COMMA, elems);
		}
		
		protected ListElementList(Symbol delim, Collection<? extends E> elems) {
			if (delim == null) {
				throw new NullPointerException("delim");
			}
			
			if (elems == null) {
				throw new NullPointerException("elems");
			}
			
			if (elems.isEmpty()) {
				throw new IllegalArgumentException("element list must not be empty");
			}
			
			this.delim = delim;
			this.content = new ArrayList<E>(elems);
		}
		

		private Symbol getDelim() {
			return delim;
		}
		
		@Override
		public void traverseContent(VisitContext vc, ElementVisitor v) {
			Iterator<E> ei = content.iterator();
			
			Symbol p = getDelim();
			
			while (ei.hasNext()) {
				E e = ei.next();
				
				e.traverse(vc, v);
				
				if (p != null && ei.hasNext()) {
					p.traverse(vc, v);
				}
			}			
		}

		@Override
		public int size() {
			return this.content.size();
		}

		@Override
		public E get(int index) {
			return this.content.get(index);
		}
	}
}
