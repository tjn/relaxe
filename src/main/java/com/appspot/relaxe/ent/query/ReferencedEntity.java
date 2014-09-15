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
package com.appspot.relaxe.ent.query;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.expr.AbstractRowValueConstructor;
import com.appspot.relaxe.expr.RowValueConstructor;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public class ReferencedEntity<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>,
	QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>,
	RA extends AttributeName,
	RR extends Reference,	
	RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
	RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
	RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
	RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
	RF extends EntityFactory<RE, RB, RH, RM, RF>,
	RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
	K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>	
> 
	implements EntityReference<RA, RR, RT, RE, RB, RH, RF, RM> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1578624124882342842L;
	
	private QE element;
	private K key;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private ReferencedEntity() {
	}	
	
	private ReferencedEntity(QE element, K key) {
		super();
		
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		this.element = element;
		
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		this.key = key;
				
		
	}
	
	
	public static <
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>,
		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>,
		RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		K extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K>	
	> 
	EntityReference<RA, RR, RT, RE, RB, RH, RF, RM> of(EntityQueryElement<A, R, T, E, B, H, F, M, QE> qe, EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, K> key) {
		return new ReferencedEntity<A, R, T, E, B, H, F, M, QE, RA, RR, RT, RE, RB, RH, RF, RM, K>(qe.self(), key.self());		
	}
	
	


	@Override
	public RowValueConstructor expression(EntityQueryContext c) {
		TableReference tref = c.getTableRef(element);				
		ForeignKey fk = key.getSource().getForeignKey(key.name());		
		ColumnMap cm = fk.getColumnMap();		
		RowValueConstructor rvc = AbstractRowValueConstructor.of(tref, cm);		
		return rvc;
	}

	
	
}
