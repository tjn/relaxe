/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.expr.ColumnExpr;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Table;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.PrimitiveType;
import com.appspot.relaxe.types.ReferenceType;


public abstract class DefaultEntityBuilder<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
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
		
//		if (pkws > 0) {		
			for (AttributeWriter<A, E> w : this.primaryKeyWriterList) {					
				PrimitiveHolder<?, ?, ?> h = src.get(w.getIndex());
				
				if (h == null || h.isNull()) {
					// referenced primary key contained nulls => not identified
					return null;				
				}
			}
//		}
		
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
	 * @return Number of values which were nulls according to copied {@link PrimitiveHolder}
	 * @see {@link AbstractPrimitiveHolder#isNull()} 
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
						
		ColumnExpr ce = ctx.getInputMetaData().column(index);
		Identifier cn = ce.getColumnName();
						
		final Column col = table.columnMap().get(cn);
		
		if (col == null) {
			throw new NullPointerException("unresolved column for cn: " + cn.getName() + " in table " + table.getQualifiedName());
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
		
		PrimitiveKey<A, E, ?, ?, ?, ?> pk = m.getKey(attribute);
		
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
		P extends PrimitiveType<P>,
		VH extends PrimitiveHolder<V, P, VH>,	
		VK extends PrimitiveKey<A, E, V, P, VH, VK>
	>
	AttributeWriter<A, E> createWriter(final PrimitiveKey<A, E, V, P, VH, VK> key, final int index) {
		return new AttributeWriter<A, E>() {
			@Override
			public PrimitiveHolder<?, ?, ?> write(DataObject src, E dest)
					throws EntityRuntimeException {
				
				PrimitiveHolder<?, ?, ?> h = src.get(index);
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
	

//	private static Logger logger() {
//		return DefaultEntityBuilder.logger;
//	}
}
