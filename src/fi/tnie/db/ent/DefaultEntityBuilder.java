/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.im.EntityIdentityMap;
import fi.tnie.db.ent.im.ReferenceIdentityMap;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Table;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

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
	
	private static Logger logger = Logger.getLogger(DefaultEntityBuilder.class);


	private TableReference tableRef = null;
	
	private List<AttributeWriter<A, E>> primaryKeyWriterList = new ArrayList<AttributeWriter<A, E>>();
	private List<AttributeWriter<A, E>> attributeWriterList = new ArrayList<AttributeWriter<A, E>>();
			
	private EntityIdentityMap<A, R, T, E, H> identityMap;

	public DefaultEntityBuilder(TableReference referencing, ForeignKey referencedBy, TableReference tableRef, EntityBuildContext ctx, UnificationContext unificationContext) 
		throws EntityException {
		super();
		
		if ((referencing == null) != (referencedBy == null)) {
			throw new IllegalArgumentException("referencing & referencedBy must both be null or not-null");
		}
		
		this.tableRef = (tableRef == null) ? referencing : tableRef;
		ForeignKey map = (tableRef == null) ? referencedBy : null;
			
		M meta = getMetaData();	
				
		if (unificationContext == null) {
			this.identityMap = new ReferenceIdentityMap<A, R, T, E, H>();
		}
		else {
			this.identityMap = meta.getIdentityMap(unificationContext);	
		}
						
		int cc = ctx.getInputMetaData().getColumnCount();
						
		for (int i = 1; i <= cc; i++) {
			TableReference tref = ctx.getQuery().getOrigin(i);
			
			if (tref == this.tableRef) {
				addWriter(map, i - 1, ctx);
			}
		}
				
	}

	public abstract M getMetaData();

	@Override
	public H read(DataObject src) {
		E ne = getMetaData().getFactory().newInstance();
		
		logger().debug("read: " + ne);
		
		int nc = copy(src, ne, this.primaryKeyWriterList);
		
		if (nc > 0) {
			// referenced primary key contained nulls => not identified
			return null;
		}
		
		if (!ne.isIdentified()) {
//			// read by linker
			logger().debug("read: not identified: " + ne);
//			return null;
		}
		
		H eh = identityMap.get(ne);
		
		
		if (eh != null) {		
			if (eh.value() != ne) {
				copy(src, ne, this.attributeWriterList);			
			}
		}
		
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
	 * @see {@link PrimitiveHolder#isNull()} 
	 */
	private int copy(DataObject src, E dest, List<AttributeWriter<A, E>> wl) {
		int n = 0;
		
		for (AttributeWriter<A, E> w : wl) {
			if (w == null) {
				throw new NullPointerException("attribute writer was null");
			}
									
			PrimitiveHolder<?, ?, ?> h = w.write(src, dest);
			
			if (h == null || h.isNull()) {
				n++;
			}
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
//		AttributeWriterFactory wf = ctx.getAttributeWriterFactory();
		
		ColumnName cn = ce.getColumnName();
						
		final Column col = table.columnMap().get(cn);
		
		if (col == null) {
			throw new NullPointerException("unresolved column for cn: " + cn.getName() + " in table " + table.getQualifiedName());
		}		
		
		final Column resolved = (fk == null) ? col : fk.columns().get(col);
		
		if (resolved == null) {
			return;
//			throw new NullPointerException("unresolved column for cn: " + cn.getName() + " by fk " + fk.getQualifiedName() + " in table " + table.getQualifiedName());
		}
		
		ConstantColumnResolver cr = new ConstantColumnResolver(resolved);
		
		M m = getMetaData();
		
		// Column col = cr.getColumn(index);				
		
		
		Column rc = cr.getColumn(index);
		A attribute = m.getAttribute(rc);
		
		if (attribute == null) { // TODO: ?
			return;
		}
		
		PrimitiveKey<A, E, ?, ?, ?, ?> pk = m.getKey(attribute);
		
//		AttributeMapping am = ctx.getAttributeMapping(attribute);
		
		logger().debug("addWriter: attribute=" + attribute);
		logger().debug("addWriter: pk=" + pk);
		
		if (pk == null) {
			return;
		}
		
		AttributeWriter<A, E> w = createWriter(pk.self(), index);
								
//		AttributeWriter<A, E> w = wf.createWriter(m, cr, index);
		
//		if (am == null) {
//			return;
//		}
		
		List<AttributeWriter<A, E>> wl = 
			resolved.isPrimaryKeyColumn() ? this.primaryKeyWriterList : this.attributeWriterList;
				
		wl.add(w);
	}			
	
	
	private <		
		V extends Serializable,
		P extends PrimitiveType<P>,
		VH extends PrimitiveHolder<V, P, VH>,	
		VK extends PrimitiveKey<A, E, V, P, VH, VK>
	>
	AttributeWriter<A, E> createWriter(final VK key, final int index) {
		return new AttributeWriter<A, E>() {
			@Override
			public PrimitiveHolder<?, ?, ?> write(DataObject src, E dest)
					throws EntityRuntimeException {
				
				PrimitiveHolder<?, ?, ?> h = src.get(index);
				VH vc = key.as(h);				
				key.set(dest, vc);
				return vc;
			}
		};
	}
	

	private static Logger logger() {
		return DefaultEntityBuilder.logger;
	}
}
