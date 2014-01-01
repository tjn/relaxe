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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ElementList<E extends Element>
	extends CompoundElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9070139545586143988L;
	private Symbol delim;
	private List<E> content;
	private transient List<E> immutableContent = null;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ElementList() {
	}
	
	public ElementList(E e) {
		this(Symbol.COMMA, Collections.singleton(e));
	}
				
	public ElementList(Symbol delim, Collection<E> elems) {
		super();
		
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
	
	public ElementList(Collection<E> elems) {
		this(Symbol.COMMA, elems);
	}
	
	public List<E> getContent() {
		if (immutableContent == null) {
			immutableContent = Collections.unmodifiableList(this.content);			
		}

		return immutableContent;
	}

//	public boolean add(E e) {
//		return getContent().add(e);
//	}
//		
//	public void set(E e) {
//		if (e == null) {
//			throw new NullPointerException("'e' must not be null");
//		}
//		
//		getContent().clear();
//		add(e);
//	}
	
	public boolean isEmpty() {
		return (this.content == null) || this.content.isEmpty();  
	}
	
//	public void copyTo(ElementList<E> dest) {
//		if (!isEmpty()) {
//			List<E> el = dest.getContent();
//			
//			for (E e : this.content) {
//				el.add(e);
//			}
//		}
//	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		Iterator<E> ei = getContent().iterator();
		
		Symbol p = getDelim();
		
		while (ei.hasNext()) {
			E e = ei.next();
			
			e.traverse(vc, v);
			
			if (p != null && ei.hasNext()) {
				p.traverse(vc, v);
			}
		}			
	}

	public Symbol getDelim() {
		return delim;
	}
	
	
	public static class Builder<E extends Element> {
		
		private List<E> content;
				
		public ElementList<E> newElementList() {
			return null;
		}
		
		public void add(E elem) {
			if (elem == null) {
				throw new NullPointerException("elem");
			}
		}
		
		
	}
}
