/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.ent.im.EntityIdentityMap;
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
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
	implements EntityBuilder<E> {						

	private TableReference tableRef = null;
	
	private List<AttributeWriter<A, T, E, ?, ?, ?, ?>> primaryKeyWriterList = 
		new ArrayList<AttributeWriter<A, T, E, ?,?,?,?>>();		
	
	private List<AttributeWriter<A, T, E, ?, ?, ?, ?>> attributeWriterList = 
		new ArrayList<AttributeWriter<A, T, E,?,?,?,?>>();
	
	private EntityIdentityMap<A, R, T, E, H> identityMap;

	public DefaultEntityBuilder(TableReference tableRef, EntityBuildContext ctx, UnificationContext identityContext) 
		throws EntityException {
		super();
		this.tableRef = tableRef;
			
		M meta = getMetaData();		
		this.identityMap = meta.getIdentityMap(identityContext);
						
		int cc = ctx.getInputMetaData().getColumnCount();
						
		for (int i = 1; i <= cc; i++) {
			TableReference tref = ctx.getQuery().getOrigin(i);
			
			if (tref == this.tableRef) {
				addWriter(i - 1, ctx);
			}		
		}
	}

	public abstract M getMetaData();

	@Override
	public E read(DataObject src) {
		E ne = getMetaData().getFactory().newInstance();		
		int nc = copy(src, ne, this.primaryKeyWriterList);
		
		if (nc > 0) {
			// referenced primary key contained nulls => not identified
			return null;
		}
		
		H eh = identityMap.get(ne);
		
		if (eh.value() != ne) {
			copy(src, ne, this.attributeWriterList);			
		}
		
		return eh.value();
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
	private int copy(DataObject src, E dest, List<AttributeWriter<A, T, E, ?, ?, ?, ?>> wl) {
		int n = 0;
		
		for (AttributeWriter<A, T, E, ?, ?, ?, ?> w : wl) {
			if (w == null) {
				throw new NullPointerException("attribute writer was null");
			}
			
			PrimitiveHolder<?, ?> h = w.write(src, dest);
			
			if (h.isNull()) {
				n++;
			}
		}
		
		return n;
	}	
	
	
	private void addWriter(int index, EntityBuildContext ctx) {
		Table table = tableRef.getTable();
						
		ColumnExpr ce = ctx.getInputMetaData().column(index);
		AttributeWriterFactory wf = ctx.getAttributeWriterFactory();
		
		ColumnName cn = ce.getColumnName();
		final Column col = table.columnMap().get(cn);
		
		ConstantColumnResolver cr = new ConstantColumnResolver(col);
		
		M m = getMetaData();
						
		AttributeWriter<A, T, E, ?, ?, ?, ?> w = wf.createWriter(m, cr, index);
		
		if (w == null) {
			return;
		}
		
		List<AttributeWriter<A, T, E, ?, ?, ?, ?>> wl = 
			col.isPrimaryKeyColumn() ? this.primaryKeyWriterList : this.attributeWriterList;
				
		wl.add(w);
	}			
}
