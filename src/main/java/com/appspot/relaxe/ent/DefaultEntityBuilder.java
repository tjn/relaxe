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
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.im.EntityIdentityMap;
import com.appspot.relaxe.ent.im.ReferenceIdentityMap;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.AbstractValueHolder;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;


public abstract class DefaultEntityBuilder<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>
>
	implements EntityBuilder<E, H> {
	
//	private static Logger logger = LoggerFactory.getLogger(DefaultEntityBuilder.class);

	private TableReference tableRef = null;
	
	private List<AttributeWriter<A, E>> primaryKeyWriterList = new ArrayList<AttributeWriter<A, E>>();
	private List<AttributeWriter<A, E>> attributeWriterList = new ArrayList<AttributeWriter<A, E>>();
			
	private EntityIdentityMap<A, R, T, E, H, M> identityMap;

	public DefaultEntityBuilder(TableReference referencing, ForeignKey referencedBy, TableReference tableRef, EntityBuildContext ctx, UnificationContext unificationContext) 
		throws EntityException {
		super();
		
		if ((referencing == null) != (referencedBy == null)) {
			throw new IllegalArgumentException("referencing & referencedBy must both be null or not-null");
		}
		
		this.tableRef = (tableRef == null) ? referencing : tableRef;
		ForeignKey map = (tableRef == null) ? referencedBy : null;
		
		EntityQueryContext qctx = ctx.getQueryContext();
			
		M meta = getMetaData();	
				
		if (unificationContext == null) {
			this.identityMap = new ReferenceIdentityMap<A, R, T, E, H, M>();
		}
		else {
			this.identityMap = meta.getIdentityMap(unificationContext);	
		}
						
		int cc = ctx.getInputMetaData().getColumnCount();
						
		for (int i = 1; i <= cc; i++) {
			TableReference tref = qctx.getOrigin(i);
			
			if (tref == this.tableRef) {
				addWriter(map, i - 1, ctx);
			}
		}
				
	}

	public abstract M getMetaData();

	@Override
	public H read(DataObject src) {
		
		int pkws = this.primaryKeyWriterList.size();
		
		if (pkws > 0) {		
			for (AttributeWriter<A, E> w : this.primaryKeyWriterList) {					
				ValueHolder<?, ?, ?> h = src.get(w.getIndex());
				
				if (h == null || h.isNull()) {
					// referenced primary key contained nulls => not identified
					return null;				
				}
			}
		}
		
		E ne = getMetaData().getFactory().newEntity();
		
//		logger().debug("read: " + ne);
		
		H eh = null;
		
//		if (pkws > 0) 
		{		
			copy(src, ne, this.primaryKeyWriterList);		
			eh = identityMap.get(ne);
			
			if (eh != null) {
				if (eh.value() == ne) {
					// If this was just inserted,
					// augment with the rest of attributes. 
					copy(src, ne, this.attributeWriterList);
				}
			}
		}
//		else {
//			// loaded without primary key attributes:
//			copy(src, ne, this.attributeWriterList);
//			// eh = ne.ref();
//			return null;
//		}		
		
		return eh;
	}
	
	/**	
	 * Copies values <code>src</code> to <code>dest</code> by calling {@link AttributeWriter#write(DataObject, Entity)} 
	 * for each element in <code>wl</code>.
	 * 	
	 * @param src
	 * @param dest
	 * @param wl List of writers to apply.
	 * @return Number of values which were nulls according to copied {@link ValueHolder}
	 * @see {@link AbstractValueHolder#isNull()} 
	 */
	private int copy(DataObject src, E dest, List<AttributeWriter<A, E>> wl) {
		int n = 0;
		
		for (AttributeWriter<A, E> w : wl) {
			if (w == null) {
				throw new NullPointerException("attribute writer was null");
			}
									
			w.write(src, dest);
		}
		
		return n;
	}	
	
	/**
	 * 
	 * @param index Column index for <code>ctx.getInputMetaData().column()</code>
	 * @param ctx
	 */
	private void addWriter(ForeignKey fk, int index, EntityBuildContext ctx) {
		Table table = this.tableRef.getTable();
						
//		ColumnExpr ce = ctx.getInputMetaData().column(index);		
//		Identifier cn = ce.getColumnName();

		Identifier cn = ctx.getInputMetaData().identifier(index);
		
						
		final Column col = table.getColumnMap().get(cn);
		
		if (col == null) {
			StringBuilder buf = new StringBuilder();
			
			int cc = ctx.getInputMetaData().getColumnCount();
			
			append(buf, 
					"unresolved column", 
					": cn: ", (cn == null ) ? null : cn.getContent(), 
					", table: ", (table == null ) ? null : table.getQualifiedName(),
					", index: ", Integer.toString(index),
					", cc: ", Integer.toString(cc),
					", col.map: ", (fk == null) ? null : fk.getColumnMap().toString()
			);
			
			
			for (int co = 1; co <= cc; co++) {
				TableReference tref = ctx.getQueryContext().getOrigin(co);
				String tn = tref.getTable().getQualifiedName();
								
				append(buf,
						"\n",
						"co: ", Integer.toString(co), 
						", tn: ", tn);
			}
			
			
			throw new NullPointerException(buf.toString());
		}		
		
		final Column resolved = (fk == null) ? col : fk.getReferenced(col);
		
		if (resolved == null) {
			return;
//			throw new NullPointerException("unresolved column for cn: " + cn.getName() + " by fk " + fk.getQualifiedName() + " in table " + table.getQualifiedName());
		}
		
		
		final BaseTable pktbl = (fk == null) ? table.asBaseTable() : fk.getReferenced(); 
		
		ConstantColumnResolver cr = new ConstantColumnResolver(resolved);
		
		M m = getMetaData();
		
		Column rc = cr.getColumn(index);
		A attribute = m.getAttribute(rc);
		
		if (attribute == null) { // TODO: ?
			return;
		}
		
		Attribute<A, E, ?, ?, ?, ?> pk = m.getKey(attribute);
		
		if (pk == null) {
			return;
		}
		
		AttributeWriter<A, E> w = createWriter(pk.self(), index);
		
		List<AttributeWriter<A, E>> wl = 
			pktbl.isPrimaryKeyColumn(resolved) ? this.primaryKeyWriterList : this.attributeWriterList;

				
		wl.add(w);
	}			
	
	
	private <		
		V extends Serializable,
		P extends ValueType<P>,
		VH extends ValueHolder<V, P, VH>,	
		VK extends Attribute<A, E, V, P, VH, VK>
	>
	AttributeWriter<A, E> createWriter(final Attribute<A, E, V, P, VH, VK> key, final int index) {
		return new AttributeWriter<A, E>() {
			@Override
			public ValueHolder<?, ?, ?> write(DataObject src, E dest)
					throws EntityRuntimeException {
				
				ValueHolder<?, ?, ?> h = src.get(index);
				VH vc = key.as(h);
				key.set(dest, vc);
				return vc;
			}
			
			@Override
			public int getIndex() {
				return index;
			}
		};
	}
	
	private StringBuilder append(StringBuilder buf, String ... elems) {
		for (int i = 0; i < elems.length; i++) {			
			buf.append(elems[i]);
		}
		
		return buf;
	}	

//	private static Logger logger() {
//		return DefaultEntityBuilder.logger;
//	}
}
