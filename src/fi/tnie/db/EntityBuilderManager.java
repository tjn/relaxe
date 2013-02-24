/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityBuildContext;
import fi.tnie.db.ent.EntityBuilder;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.ent.MutableEntityDataObject;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityBuilderManager<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
>
	extends DataObjectProcessor<MutableEntityDataObject<E>> {
			
	private EntityQuery<A, R, T, E, H, F, M, C, ?> query;	
	private M meta;
	
	private static Logger logger = Logger.getLogger(EntityBuilderManager.class);
	
	private UnificationContext identityContext;
	private EntityBuildContext context;
		
	private EntityBuilder<E> rootBuilder;
		
						
	public EntityBuilderManager(ValueExtractorFactory vef, EntityQuery<A, R, T, E, H, F, M, C, ?> query, UnificationContext unificationContext) 
		throws QueryException {
		super(vef, query);		
		this.query = query;
		this.meta = query.getMetaData();
		
		this.identityContext = unificationContext;
	}
	
	@Override
	public void prepare() 
		throws EntityRuntimeException {
		
		try {		
			context = new DefaultEntityBuildContext(getMetaData(), this.query, null);				
			TableReference rootRef = query.getTableRef();
			this.rootBuilder = this.meta.newBuilder(null, null, rootRef, context, identityContext);
		}
		catch (EntityException e) {
			throw new EntityRuntimeException(e.getMessage(), e);
		}
	}
	
	@Override
	public void finish() {
		if (getIdentityContext() != null) {
			getIdentityContext().close();	
		}
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
	protected void put(MutableEntityDataObject<E> o) {
//		logger().debug("put - enter");				
		E result = rootBuilder.read(o);

		//		result = read(result);					
		
		o.setRoot(result);		
		process(o);
	}

	protected E read(E result) {
				
		
		return result;
	}

	public void process(EntityDataObject<E> e) {		
	}

	@Override
	protected MutableEntityDataObject<E> get() {
		MutableEntityDataObject<E> o = new MutableEntityDataObject<E>(getMetaData());		
		return o;
	}	


	private UnificationContext getIdentityContext() {
		return identityContext;
	}

	public EntityQuery<?, ?, ?, ?, ?, ?, ?, ?, ?> getQuery() {
		return query;
	}

	
	private static Logger logger() {
		return EntityBuilderManager.logger;
	}
}
