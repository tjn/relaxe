/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DefaultDataObject;
import fi.tnie.db.ent.DefaultEntityBuildContext;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityBuildContext;
import fi.tnie.db.ent.EntityBuilder;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.IdentityContext;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityBuilderManager<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, H, ?, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	M extends EntityMetaData<A, R, T, E, H, ?, M>
>
	extends DataObjectProcessor {
			
	private EntityQuery<A, R, T, E, M> query;	
	private M meta;
	
	private IdentityContext identityContext = new SimpleIdentityContext();	
	private EntityBuildContext context;
	
	private EntityBuilder<E> rootBuilder;
						
	public EntityBuilderManager(ValueExtractorFactory vef, EntityQuery<A, R, T, E, M> query) {
		super(vef, query.getQuery());
		this.query = query;
		this.meta = query.getMetaData();		
	}
	
	@Override
	public void prepare() {				
		AttributeWriterFactory wf = new DefaultAttributeWriterFactory();				
		context = new DefaultEntityBuildContext(getMetaData(), this.query, wf, null);				
		TableReference rootRef = query.getTableRef();				
		this.rootBuilder = this.meta.newBuilder(rootRef, context);
	}
	
	@Override
	public void finish() {	
		getIdentityContext().close();
	}
	
	
//	private int indexOf(TableReference tref) {
//		if (indexMap == null) {
//			indexMap = new HashMap<TableReference, Integer>();
//		}
//		
//		Integer index = indexMap.get(tref);
//		
//		if (index == null) {
//			index = Integer.valueOf(indexMap.size());
//			indexMap.put(tref, index);
//		}		
//		
//		return index.intValue();
//	}
	
	@Override
	protected void put(DefaultDataObject o) {
		E result = rootBuilder.read(o);		
		process(result);
	}

	public void process(E e) {
		
	}

	@Override
	protected DefaultDataObject get() {
		return new DefaultDataObject(getMetaData());
	}	


	private IdentityContext getIdentityContext() {
		return identityContext;
	}

	public EntityQuery<?, ?, ?, ?, ?> getQuery() {
		return query;
	}
	
}
