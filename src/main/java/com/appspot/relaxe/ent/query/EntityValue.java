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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.expr.AbstractRowValueConstructor;
import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.expr.RowValueConstructor;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;

public class EntityValue<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>
	implements EntityReference<A, R, T, E, B, H, F, M> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1795474006332835626L;
	
	private final RowValueConstructor value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private EntityValue() {
		this.value = null;
	}
	
	public EntityValue(E value) {
		super();
		
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		final E pke = value.toPrimaryKey();
		
		if (pke == null) {
			throw new NullPointerException("value.toPrimaryKey()");
		}
				
		BaseTable t = pke.getMetaData().getBaseTable();
		PrimaryKey pk = t.getPrimaryKey();		
		ColumnMap cm = pk.getColumnMap();
		
		int cc = cm.size();
		
		if (cc == 1) {						
			Column col = cm.get(0);
			ValueHolder<?, ?, ?> vh = value.get(col);			
			ImmutableValueParameter<?, ?, ?> p = newRowValueConstructorElement(col, vh);			
			this.value = AbstractRowValueConstructor.of(p);
		}
		else {			
			List<ValueExpression> pl = new ArrayList<ValueExpression>(cc); 
			
			for (int i = 0; i < cc; i++) {
				Column col = cm.get(0);
				ValueHolder<?, ?, ?> vh = value.get(col);			
				ValueExpression p = newRowValueConstructorElement(col, vh);
				pl.add(p);				
			}
			
			this.value = AbstractRowValueConstructor.of(pl);
		}		
	}

	@Override
	public RowValueConstructor expression(EntityQueryContext c) {						
		return value;
	}

	/**
	 * TODO: merge with similar code in PersistenceManager
	 * @param col
	 * @param holder
	 * @return
	 */
	
	private static <		
		PV extends Serializable,
		PT extends ValueType<PT>,
		PH extends ValueHolder<PV, PT, PH>
	>
	ImmutableValueParameter<PV, PT, PH> newRowValueConstructorElement(Column col, ValueHolder<PV, PT, PH> holder) {		
		return new ImmutableValueParameter<PV, PT, PH>(col, holder.self());
	}
}
