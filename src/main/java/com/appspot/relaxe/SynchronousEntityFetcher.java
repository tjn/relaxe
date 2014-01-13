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
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.paging.EntityFetcher;
import com.appspot.relaxe.paging.PageReceiver;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;


public class SynchronousEntityFetcher<
	A extends AttributeName,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,	
	RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>	
> implements EntityFetcher<A, R, T, E, H, F, M, RE> {
	
	private EntityQueryExecutor<A, R, T, E, H, F, M, RE> executor;
	private Connection connection;
	
	public SynchronousEntityFetcher(EntityQueryExecutor<A, R, T, E, H, F, M, RE> executor, Connection connection) {
		super();
		this.executor = executor;
		this.connection = connection;
	}

	@Override
	public void fetch(EntityQuery<A, R, T, E, H, F, M, RE> query, FetchOptions opts, PageReceiver<EntityQueryResult<A, R, T, E, H, F, M, RE>> receiver, PageReceiver<Throwable> errorReceiver) {		
		try {
			EntityQueryResult<A, R, T, E, H, F, M, RE> qr = executor.execute(query, opts, this.connection);
			receiver.receive(qr);			
		}
		catch (SQLException e) {
			errorReceiver.receive(e);
		} 
		catch (QueryException e) {
			errorReceiver.receive(e);
		} 
		catch (EntityException e) {
			errorReceiver.receive(e);
		}
	}
}
