/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DefaultDataObject;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.IdentityContext;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.DefaultDataObject.MetaData;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Table;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityBuilder
	extends DataObjectProcessor {
			
	private EntityQuery<?, ?, ?, ?> query;
	
	private Map<TableReference, Integer> indexMap;
	private Source<?, ?, ?, ?>[] sources = {};
	private Implementation implementation;
	
	private Map<TableReference, Source<?, ?, ?, ?>> sourceMap;
	
	private IdentityContext identityContext = new SimpleIdentityContext();
						
	public EntityBuilder(ValueExtractorFactory vef, EntityQuery<?, ?, ?, ?> query, Implementation implementation) {
		super(vef, query.getQuery());
		this.query = query;
		this.implementation = implementation;
	}
	
	@Override
	public void prepare() {
		MetaData meta = getMetaData();
		int cc = meta.getColumnCount();
		
		AttributeWriterFactory wf = new DefaultAttributeWriterFactory();
				
		ArrayList<Source<?, ?, ?, ?>> sl = new ArrayList<Source<?,?,?,?>>();		 
		Source<?, ?, ?, ?> current = null;
				
		for (int i = 0; i < cc; i++) {
			TableReference tref = query.getOrigin(i);
			int index = indexOf(tref);
						
			if (index < sl.size()) {
				current = sl.get(index);
			}
			else {				
				EntityMetaData<?, ?, ?, ?> em = query.getMetaData(tref);				
				
				current = createSource(tref, em);
				sl.add(current);				
				em.getIdentityMap(getIdentityContext());				
			}
			
			current.addWriter(meta, i, wf);			
		}
		
		this.sources = sl.toArray(this.sources);		
		this.sourceMap = populateSourceMap(sl);		
	}

	private Map<TableReference, Source<?,?,?,?>> populateSourceMap(Iterable<Source<?,?,?,?>> sources) {
		Map<TableReference, Source<?,?,?,?>> map = new HashMap<TableReference, Source<?,?,?,?>>();
		
		for (Source<?, ?, ?, ?> src : sources) {
			map.put(src.tableReference, src);
		}
		
		return map;
	}
	
	@Override
	public void finish() {	
		getIdentityContext().close();
	}
	
	
	private int indexOf(TableReference tref) {
		if (indexMap == null) {
			indexMap = new HashMap<TableReference, Integer>();
		}
		
		Integer index = indexMap.get(tref);
		
		if (index == null) {
			index = Integer.valueOf(indexMap.size());
			indexMap.put(tref, index);
		}		
		
		return index.intValue();
	}

	private	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<T>,
		E extends Entity<A, R, T, E>
	>
	Source<A, R, T, E> createSource(TableReference tref, EntityMetaData<A, R, T, E> em) {
		return new Source<A, R, T, E>(tref, em);
	}

	
	@Override
	protected void put(DefaultDataObject o) {						
		for (Source<?, ?, ?, ?> src : this.sources) {
			// TODO: link entities up
			Entity<?, ?, ?, ?> e = src.read(o);
			
			if (e == null) {
			}
		}
		
	}
	
	public void process() {
	
	}

	@Override
	protected DefaultDataObject get() {
		return new DefaultDataObject(getMetaData());
	}	

	private class Source<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<T>,
		E extends Entity<A, R, T, E>
	>

{	
	private TableReference tableReference;
	private EntityMetaData<A, R, T, E> metaData;
	private List<AttributeWriter<A, R, T, E, ?, ?, ?, ?>> primaryKeyWriterList;		
	private List<AttributeWriter<A, R, T, E, ?, ?, ?, ?>> attributeWriterList;
		
	private Source(TableReference tableReference, EntityMetaData<A, R, T, E> metaData) {
		super();
		this.tableReference = tableReference;
		this.metaData = metaData;
	}

	private void addWriter(DataObject.MetaData meta, int index, AttributeWriterFactory wf) {
		Table table = this.tableReference.getTable();
		ColumnExpr ce = meta.column(index);
		Column col = table.columnMap().get(ce.getColumnName());
		
		AttributeWriter<A, R, T, E, ?, ?, ?, ?> w = wf.createWriter(this.metaData, meta, index);		
		List<AttributeWriter<A, R, T, E, ?, ?, ?, ?>> wl = 
			col.isPrimaryKeyColumn() ? this.primaryKeyWriterList : this.attributeWriterList;
		wl.add(w);		
	}
		
	public E read(DataObject src) {
		EntityFactory<A, R, T, E> f = metaData.getFactory();		
		final E ne = f.newInstance();
		
		// this.tableReference.getTable()		
		
		int nc = copy(src, ne, this.primaryKeyWriterList);
				
		for (R r : metaData.relationships()) {
			ForeignKey fk = metaData.getForeignKey(r);			
			TableReference referenced = getQuery().getReferenced(tableReference, fk);
			
			if (referenced != null) {
				Source<?, ?, ?, ?> dep = getSourceMap().get(referenced);
				Entity<?, ?, ?, ?> e = dep.read(src);
								
				EntityKey<A, R, T, E, ?, ?, ?, ?> ek = metaData.getEntityKey(r);
				ReferenceType<?> t = e.ref().getType();
				ne.set(r, e.ref());
				
//				dep.metaData.getFactory().newHolder(e);
				
				if (e == null) {					
				}
				else {
				}
			}
		}
						
		// TODO: Fix. Primary key may contain reference columns as well. 
		
		if (!ne.isIdentified()) {
			return null;
		}
				
		final E me = ne.unify(getIdentityContext());
		copy(src, ne, this.attributeWriterList);		
		return me;
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
	private int copy(DataObject src, E dest, List<AttributeWriter<A, R, T, E, ?, ?, ?, ?>> wl) {
		int n = 0;
		
		for (AttributeWriter<A, R, T, E, ?, ?, ?, ?> w : wl) {
			PrimitiveHolder<?, ?> h = w.write(src, dest);
			
			if (h.isNull()) {
				n++;
			}
		}
		
		return n;
	}	
}

	private IdentityContext getIdentityContext() {
		return identityContext;
	}

	private EntityQuery<?, ?, ?, ?> getQuery() {
		return query;
	}
	
	private Map<TableReference, Source<?, ?, ?, ?>> getSourceMap() {
		return sourceMap;
	}
}
