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

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryContext;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.expr.AbstractRowValueConstructor;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.RowValueConstructor;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public class QueryElementEntityReference<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>,
	QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
>
	implements EntityReference<A, R, T, E, B, H, F, M> {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1578624124882342842L;
	
	private QE element;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private QueryElementEntityReference() {
	}	
	
	public QueryElementEntityReference(QE element) {
		super();
		
		if (element == null) {
			throw new NullPointerException("element");
		}
		
		this.element = element;
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
		QE extends EntityQueryElement<A, R, T, E, B, H, F, M, QE>
	> 
	EntityReference<A, R, T, E, B, H, F, M> of(EntityQueryElement<A, R, T, E, B, H, F, M, QE> ee) {
		return new QueryElementEntityReference<A, R, T, E, B, H, F, M, QE>(ee.self());		
	}
	
	


	@Override
	public RowValueConstructor expression(EntityQueryContext c) {
		TableReference tref = c.getTableRef(element);
		
		M m = element.getMetaData();
		
		BaseTable table = m.getBaseTable();
		PrimaryKey pk = table.getPrimaryKey();		
		ColumnMap cm = pk.getColumnMap();
		int cc = cm.size();
		
		ElementList<ColumnReference> el = null;
		
		if (cc == 1) {			
			Column col = cm.get(0);			
			el = ElementList.newElementList(new ColumnReference(tref, col));
		}
		else {
			List<ColumnReference> cl = new ArrayList<ColumnReference>();
			
			for (int i = 0; i < cc; i++) {
				Column col = cm.get(0);				
				cl.add(new ColumnReference(tref, col));
			}
			
			el = ElementList.newElementList(cl);			
		}
		
		RowValueConstructor rvc = AbstractRowValueConstructor.of(el);		
		return rvc;
	}

	
	
}
