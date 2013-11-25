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
package com.appspot.relaxe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityBuildContext;
import com.appspot.relaxe.ent.EntityBuilder;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.MutableEntityDataObject;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public class EntityBuilderManager<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	QE extends EntityQueryElement<A, R, T, E, H, F, M, C, QE>
>
	extends DataObjectProcessor<MutableEntityDataObject<E>> {
			
//	private EntityQuery<A, R, T, E, H, F, M, C, QE> query;
	private EntityQueryExpressionBuilder<A, R, T, E, H, F, M, C, QE> builder;
//	private M meta;
	
	private static Logger logger = LoggerFactory.getLogger(EntityBuilderManager.class);
	
	private UnificationContext identityContext;
	private EntityBuildContext context;
		
	private EntityBuilder<E, H> rootBuilder;
						
	public EntityBuilderManager(ValueExtractorFactory vef, EntityQueryExpressionBuilder<A, R, T, E, H, F, M, C, QE> builder, UnificationContext unificationContext) 
		throws QueryException {
		super(vef, builder.getQueryExpression());		
		this.builder = builder;
		this.identityContext = unificationContext;
	}
	
	@Override
	public void prepare() 
		throws EntityRuntimeException {
		
		try {			
			context = new DefaultEntityBuildContext(getMetaData(), builder, null);				
			TableReference rootRef = builder.getRootRef();
			M meta = builder.getQuery().getRootElement().getMetaData();
			this.rootBuilder = meta.newBuilder(null, null, rootRef, context, identityContext);
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
		H result = rootBuilder.read(o);

		//		result = read(result);					
		
		o.setRoot(result.value());
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

	public EntityQuery<A, R, T, E, H, F, M, C, QE> getQuery() {
		return builder.getQuery();
	}

	
	private static Logger logger() {
		return EntityBuilderManager.logger;
	}
}