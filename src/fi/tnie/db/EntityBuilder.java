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
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Table;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityBuilder<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends DataObjectProcessor {
			
	private EntityQuery<A, R, T, E, M> query;	
	private Map<TableReference, Integer> indexMap;
		
	private Map<TableReference, Source<?, ?, ?, ?, ?>> sourceMap;
	
	private Source<A, R, T, E, M> root;
	private M meta;
	
	private IdentityContext identityContext = new SimpleIdentityContext();
						
	public EntityBuilder(ValueExtractorFactory vef, EntityQuery<A, R, T, E, M> query) {
		super(vef, query.getQuery());
		this.query = query;
		this.meta = query.getMetaData();		
	}
	
	@Override
	public void prepare() {
		MetaData meta = getMetaData();
		int cc = meta.getColumnCount();
		
		AttributeWriterFactory wf = new DefaultAttributeWriterFactory();
		
						
		ArrayList<Source<?, ?, ?, ?, ?>> sl = new ArrayList<Source<?, ?, ?, ?, ?>>();		 
		Source<?, ?, ?, ?, ?> current = null;
					
		this.root = new Source<A, R, T, E, M>(getQuery().getTableRef(), this.meta);
		
		sl.add(this.root);
				
		for (int i = 1; i <= cc; i++) {
			TableReference tref = query.getOrigin(i);
			int index = indexOf(tref);
						
			if (index < sl.size()) {
				current = sl.get(index);
			}
			else {				
				EntityMetaData<?, ?, ?, ?, ?, ?, ?> em = query.getMetaData(tref);
				current = createSource(tref, em.self());
				sl.add(current);				
				em.getIdentityMap(getIdentityContext());				
			}
			
			current.addWriter(meta, i - 1, wf);			
		}
		
//		this.sources = sl.toArray(new Source[] {});
		this.sourceMap = populateSourceMap(sl);		
	}

	private Map<TableReference, Source<?,?,?,?,?>> populateSourceMap(Iterable<Source<?,?,?,?,?>> sources) {
		Map<TableReference, Source<?,?,?,?,?>> map = new HashMap<TableReference, Source<?,?,?,?,?>>();
		
		for (Source<?, ?, ?, ?, ?> src : sources) {
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
		IA extends Attribute,
		IR extends Reference,
		IT extends ReferenceType<IT, IM>,
		IE extends Entity<IA, IR, IT, IE, ?, ?, IM>,
		IM extends EntityMetaData<IA, IR, IT, IE, ?, ?, IM>
	>
	Source<IA, IR, IT, IE, IM> createSource(TableReference tref, IM em) {
		return new Source<IA, IR, IT, IE, IM>(tref, em);
	}

	
	@Override
	protected void put(DefaultDataObject o) {		
		TableReference rr = getQuery().getTableRef();
		
		Source<A, R, T, E, M> src = this.root;				
		E result = read(rr, src, o);				
		process(result);
	}
	
	
	private <
		X extends Attribute,
		Y extends Reference,		
		Z extends ReferenceType<Z, IM>,
		V extends Entity<X, Y, Z, V, ?, ?, IM>,
		IM extends EntityMetaData<X, Y, Z, V, ?, ?, IM>
	>		
	V read(TableReference tref, Source<X, Y, Z, V, IM> src, DefaultDataObject o) {		
		V ne = src.read(o);
		
		if (ne == null) {
			return null;
		}
								
		EntityMetaData<X, Y, ?, V, ?, ?, IM> m = src.metaData;
				
		for (Y r : m.relationships()) {
			ForeignKey fk = m.getForeignKey(r);			
			TableReference rr = query.getReferenced(tref, fk);
			
			if (rr == null) {
				
			}	
			else {				
				Source<?, ?, ?, ?, ?> ns = getSourceMap().get(rr);																		
				readAndAssign(ne, r, ns, o);				
			}			
		}	
				
		return ne.isIdentified() ? ne : null;
	}
	
	
	<
		R extends Reference,		
		E extends Entity<?, R, ?, E, ?, ?, ?>,
		X extends Attribute,
		Y extends Reference,
		Z extends ReferenceType<Z, D>,
		V extends Entity<X, Y, Z, V, ?, ?, D>,
		D extends EntityMetaData<X, Y, Z, V, ?, ?, D>	
	>
	void readAndAssign(E referencing, R ref, Source<X, Y, Z, V, D> dest, DefaultDataObject o) {		
		EntityKey<R, ?, E, ?, Z, V, ?, D, ?> k = referencing.getMetaData().getEntityKey(ref, dest.metaData);
		
		V ne = read(dest.tableReference, dest, o);		
		k.set(referencing, ne);		
	}

	public void process(E e) {
		
	}

	@Override
	protected DefaultDataObject get() {
		return new DefaultDataObject(getMetaData());
	}	

	private class Source<
		IA extends Attribute,		
		IR extends Reference,
		IT extends ReferenceType<IT, IM>,
		IE extends Entity<IA, IR, IT, IE, ?, ?, IM>,
		IM extends EntityMetaData<IA, IR, IT, IE, ?, ?, IM>
	>

{	
	private TableReference tableReference;
	private IM metaData;
	private List<AttributeWriter<IA, IT, IE, ?, ?, ?, ?>> primaryKeyWriterList = new ArrayList<AttributeWriter<IA,IT,IE,?,?,?,?>>();		
	private List<AttributeWriter<IA, IT, IE, ?, ?, ?, ?>> attributeWriterList = new ArrayList<AttributeWriter<IA,IT,IE,?,?,?,?>>();
		
	private Source(TableReference tableReference, IM metaData) {
		super();
		this.tableReference = tableReference;
		this.metaData = metaData;
	}

	private void addWriter(DataObject.MetaData meta, int index, AttributeWriterFactory wf) {
		Table table = this.tableReference.getTable();
		ColumnExpr ce = meta.column(index);
		ColumnName cn = ce.getColumnName();
		final Column col = table.columnMap().get(cn);
		
		ConstantColumnResolver cr = new ConstantColumnResolver(col);		
						
		AttributeWriter<IA, IT, IE, ?, ?, ?, ?> w = wf.createWriter(this.metaData, cr, index);		
		List<AttributeWriter<IA, IT, IE, ?, ?, ?, ?>> wl = 
			col.isPrimaryKeyColumn() ? this.primaryKeyWriterList : this.attributeWriterList;
				
		wl.add(w);
	}
		
	public IE read(DataObject src) {
		EntityFactory<IE, ?, ?, ?> f = metaData.getFactory();		
		final IE ne = f.newInstance();
		
		// this.tableReference.getTable()	
		
		copy(src, ne, this.primaryKeyWriterList);
		final IE me = ne.unify(getIdentityContext());
		
		if (me == null) {
			return null;
		}
		
		copy(src, me, this.attributeWriterList);		
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
	private int copy(DataObject src, IE dest, List<AttributeWriter<IA, IT, IE, ?, ?, ?, ?>> wl) {
		int n = 0;
		
		for (AttributeWriter<IA, IT, IE, ?, ?, ?, ?> w : wl) {
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

	public EntityQuery<?, ?, ?, ?, ?> getQuery() {
		return query;
	}
	
	private Map<TableReference, Source<?, ?, ?, ?, ?>> getSourceMap() {
		return sourceMap;
	}
	
}
