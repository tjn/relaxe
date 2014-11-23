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
package com.appspot.relaxe.ent;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.ent.value.StringAttribute;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.StringHolder;
import com.appspot.relaxe.value.ValueHolder;


public abstract class AbstractEntity<
	A extends AttributeName,
	R extends Reference, 
	T extends ReferenceType<A, R, T, E, B, H, F, M>,	
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>, 
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
> 
	implements Entity<A, R, T, E, B, H, F, M>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1538787348338709153L;	

	@Override
	public ValueHolder<?, ?, ?> get(Column column) {
		
		if (column == null) {
			throw new NullPointerException("column");
		}
		
		M m = getMetaData();
		
		A a = m.getAttribute(column);
		
		if (a != null) {				
			Attribute<A, E, ?, ?, ?, ?, ?> k = m.getKey(a);
					
			if (k != null) {
				return k.get(self());			
			}	
		}
								
		// column may be part of multiple
		// overlapping foreign-keys:				
		Set<R> rs = m.getReferences(column);
		
		if (rs == null) {
			return null;
		}
	
		Entity<?, ?, ?, ?, ?, ?, ?, ?> ref = null;
		R r = null;
		
		for (R ri : rs) {						
			ReferenceHolder<?, ?, ?, ?, ?, ?> rh = ref(ri);
			
			if (rh != null) {
				ref = rh.value();
							
				if (ref != null) {				
					r = ri;
					break;
				}
			}
		}
				
		if (ref == null) {			
			return null;
		}
		
		ForeignKey fk = m.getForeignKey(r);
		Column fkcol = fk.getReferenced(column);
		
		return ref.get(fkcol);
	}

	/**
	 * Returns a type-safe self-reference. Implementation must return <code>this</code>.
	 *  
	 * @return
	 */
	@Override
	public abstract E self();
	
	
	@Override
	public T type() {
	 	return getMetaData().type();
	}
	
	@Override
	public final String toString() {
		StringBuffer buf = new StringBuffer();
		Map<Object, Integer> cm = new IdentityHashMap<Object, Integer>();
		traverse(buf, this, cm);		
		return buf.toString();
	}
		
	
	private void traverse(StringBuffer buf, Entity<?, ?, ?, ?, ?, ?, ?, ?> e, Map<Object, Integer> traversed) {		
		Integer no = traversed.get(e);
				
		if (no != null) {
			buf.append("(#");
			buf.append(no);
			buf.append(")");
			return;
		}
		
		no = Integer.valueOf(traversed.size() + 1);		
		traversed.put(e, no);
		
		M meta = getMetaData();
		
		buf.append("#");
		buf.append(no);
		buf.append(":");
		buf.append(super.toString());
		buf.append(":{");
				
		if (meta == null) {
			throw new NullPointerException("getMetaData()");
		}
		
		Set<A> as = meta.attributes();
		
		if (as == null) {
			throw new NullPointerException("getMetaData().attributes()");
		}
		
		int ord = 0;
		int ac = as.size();
				
		for (A a : as) {
			Attribute<A, E, ?, ?, ?, ?, ?> key = meta.getKey(a);
			
			ord++;
			
			if (key == null) {
				buf.append("<no key for attribute: ");
				buf.append(a);
				buf.append(">");
				continue;
			}					
						
			buf.append("@:");
			buf.append(key.name());
			buf.append("=");
			buf.append("{");			
			ValueHolder<?, ?, ?> v = key.get(self());
			buf.append(v);
			buf.append("}");
			
			if (ord < ac) {
				buf.append(",");
			}
		}
		
		buf.append(",");
		
		Set<R> rs = meta.relationships();
		
		int rc = 0;
		
		for (R r : rs) {
			EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = meta.getEntityKey(r);
			ReferenceHolder<?, ?, ?, ?, ?, ?> rh = k.get(self());
			
			if (rh != null) {
				rc++;
				buf.append(r);
				buf.append("=");
				
				if (rh.isNull()) {
					buf.append(rh.toString());
				}
				else {
					traverse(buf, rh.value(), traversed);
				}
			}
			
			buf.append(",");
		}
		
		buf.append("\n");
		buf.append("ref-count: " + rc);
		buf.append("}");
	}

	/**
	 * Returns true if the primary key of this entity has not null components.
	 * @throws EntityRuntimeException 
	 */	
	@Override
	public abstract boolean isIdentified();
	
	@Override
	public <
		VV extends Serializable, 
		VT extends com.appspot.relaxe.types.ValueType<VT>, 
		VH extends com.appspot.relaxe.value.ValueHolder<VV, VT, VH>, 
		K extends Attribute<A, E, B, VV, VT, VH, K>
	> 
	boolean has(K key) {
		VH vh = key.get(self());		
		return (vh != null);
	}	
	
	@Override
	public <
		VV extends java.io.Serializable, 
		VT extends com.appspot.relaxe.types.ValueType<VT>, 
		VH extends com.appspot.relaxe.value.ValueHolder<VV, VT, VH>, 
		K extends com.appspot.relaxe.ent.value.Attribute<A, E, B, VV, VT, VH, K>
	> 
	boolean match(K key, E another) {
		VH a = get(key);
		
		if (a == null) {
			return false;
		}
		
		VH b = another.get(key);
		
		if (b == null) {
			return false;
		}
		
		return a.contentEquals(b);		
	};
	
	@Override
	public <
		P extends ValueType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringAttribute<A, E, B, P, SH, K>
	>
	SH getString(K k) {
		SH sh = get(k.self());
		return sh;
	}
		
	
	@Override
	public E copy() {
		M meta = getMetaData();
		F ef = meta.getFactory();				
		E src = self(); 
		B dest = ef.newEntity();
		
		for (A a : meta.attributes()) {
			Attribute<A, E, B, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.copy(src, dest);
		}
		
		for (R r : meta.relationships()) {
			EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
			ek.copy(src, dest);			
		}
				
		Operation op = new Operation();
		
		try {		
			E ie = dest.toImmutable(op.getContext());		
			return ie;
		}
		finally {
			op.finish();
		}
	}
	

	@Override
	public com.appspot.relaxe.value.ValueHolder<?,?,?> value(A attribute) {
		Attribute<A, E, ?, ?, ?, ?, ?> key = getMetaData().getKey(attribute);
		return key.get(self());
	}
	
	 
		
}
 