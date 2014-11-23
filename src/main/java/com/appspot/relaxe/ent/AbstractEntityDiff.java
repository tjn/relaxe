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

import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;


public abstract class AbstractEntityDiff<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>
	implements EntityDiff<A, R, T, E>
{
	private E original;
	private E modified;

//	private static Logger logger = LoggerFactory.getLogger(AbstractEntityDiff.class);

	protected AbstractEntityDiff(E original, E modified) {
		super();
		this.original = original;
		this.modified = modified;
	}

	@Override
	public E getOriginal() {
		return original;
	}

	@Override
	public E getModified() {
		return modified;
	}

	@Override
	public Change change() {
		if (original == null && modified == null) {
			return null;
		}

		if (original == null) {
			return Change.ADDITION;
		}

		if (modified == null) {
			return Change.DELETION;
		}

		if (!attributes().isEmpty()) {
			return Change.MODIFICATION;
		}

		return null;
	}

	/**
	 * TODO: Make definition clearer.
	 *
	 * @param original
	 * @param modified
	 * @return
	 * @throws EntityException 
	 */

	protected Map<A, Change> attributes(E original, E modified) throws EntityRuntimeException {				
		EntityMetaData<A, R, T, E, ?, ?, ?, ?> meta = original.getMetaData();
		Map<A, Change> cm = new HashMap<A, Change>();

		for (A a : meta.attributes()) {
			ValueHolder<?, ?, ?> o = original.value(a);
			ValueHolder<?, ?, ?> m = modified.value(a);

			if ((o == null && m == null) || (o == m)) {
				continue;
			}

			if (o == null || o.isNull()) {
				cm.put(a, Change.ADDITION);
				continue;
			}

			if (m == null || m.isNull()) {
				cm.put(a, Change.DELETION);
				continue;
			}

			try {
				if (!o.equals(m)) {
					cm.put(a, Change.MODIFICATION);
				}
			}
			catch (ClassCastException e) {
				cm.put(a, Change.MODIFICATION);
				// TODO: should't we throw an exception
//				logger().info(e.getMessage());
			}
		}

		return cm;
	}

	protected Map<R, Change> references(E original, E modified) throws EntityRuntimeException {
		M meta = original.getMetaData();
		Map<R, Change> cm = new HashMap<R, Change>();

		for (R r : meta.relationships()) {
			Entity<?, ?, ?, ?, ?, ?, ?, ?> o = original.ref(r).value();
			Entity<?, ?, ?, ?, ?, ?, ?, ?> m = modified.ref(r).value();
			

			if ((o == null && m == null) || (o == m)) {
				continue;
			}

			if (o == null) {
				cm.put(r, Change.ADDITION);
				continue;
			}

			if (m == null) {
				cm.put(r, Change.DELETION);
				continue;
			}
			
			EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
									
			if (o != m && primaryKeyDiffers(ek, original, modified)) {
				cm.put(r, Change.MODIFICATION);
				continue;				
			}			
		}

		return cm;
	}
	
	
	
	private 
	<
		KA extends AttributeName,		
		KR extends Reference,
		KT extends ReferenceType<KA, KR, KT, KE, KB, KH, KF, KM>,
		KE extends Entity<KA, KR, KT, KE, KB, KH, KF, KM>,
		KB extends MutableEntity<KA, KR, KT, KE, KB, KH, KF, KM>,
		KH extends ReferenceHolder<KA, KR, KT, KE, KH, KM>,
		KF extends EntityFactory<KE, KB, KH, KM, KF>,
		KM extends EntityMetaData<KA, KR, KT, KE, KB, KH, KF, KM>,
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>	
	>	
	boolean primaryKeyDiffers(EntityKey<KA, KR, KT, KE, KB, KH, KF, KM, RA, RR, RT, RE, RB, RH, RF, RM, ?> key, KE a, KE b) {
								
		RH ra = key.get(a);
		RH rb = key.get(b);
		
		if (ra == null || rb == null) {
			return (ra != rb);
		}		
		
		return primaryKeyDiffers(ra.value(), rb.value());		
	}
	
	
	

	private
	<
		P extends Entity<?, ?, ?, P, ?, ?, ?, ?>		
	>
	boolean primaryKeyDiffers(P o, P m) throws EntityRuntimeException {
				
		P a = o.toPrimaryKey();
		P b = m.toPrimaryKey();
		
		if (a == null || b == null) {
			return a != b;
		}

		return (!a.equals(b));
	}

//	public static Logger logger() {
//		return AbstractEntityDiff.logger;
//	}
}
