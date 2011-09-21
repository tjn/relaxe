/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.ElementMap;
import fi.tnie.db.meta.SchemaElement;

public class UnionMap<E extends SchemaElement>
	implements ElementMap<E>, Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -522365637415786440L;
	private ElementMap<E> left;
	private ElementMap<E> right;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected UnionMap() {
	}
	
	public UnionMap(ElementMap<E> left, ElementMap<E> right) {		
		this.left = left;
		this.right = right;
	}

	@Override
	public E get(Identifier name) {
		E e = left.get(name);		
		return (e == null) ? right.get(name) : e;
	}

	@Override
	public E get(String name) {
		E e = left.get(name);		
		return (e == null) ? right.get(name) : e;		
	}

	@Override
	public Set<Identifier> keySet() {
		return new AbstractSet<Identifier>() {
			@Override
			public Iterator<Identifier> iterator() {
				return new ConcatIterator<Identifier>(
						left.keySet().iterator(), 
						right.keySet().iterator());
			}

			@Override
			public int size() {
				return 0;
			}
		};
	}

	@Override
	public Collection<? extends E> values() {					
		return new AbstractCollection<E>() {
			@Override
			public Iterator<E> iterator() {				
				return new ConcatIterator<E>(
						left.values().iterator(), 
						right.values().iterator());
			}

			@Override
			public int size() {				
				return left.keySet().size() + right.keySet().size();
			}
		};
	}
}
