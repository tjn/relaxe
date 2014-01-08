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

import com.appspot.relaxe.rpc.AbstractResponse;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;

public class DefaultEntityQueryResult<
	A extends AttributeName,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	RE extends EntityQueryElement<A, R, T, E, H, F, M, RE>
>
	extends AbstractResponse<EntityQuery<A, R, T, E, H, F, M, RE>>
	implements EntityQueryResult<A, R, T, E, H, F, M, RE> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6159979642605451277L;
	
	private DataObjectQueryResult<EntityDataObject<E>> content;
		
	protected DefaultEntityQueryResult() {
		super();
	}

	public DefaultEntityQueryResult(EntityQuery<A, R, T, E, H, F, M, RE> request, DataObjectQueryResult<EntityDataObject<E>> content) {
		super(request);
		this.content = content;
		this.content.getContent().size();
				
	}

	@Override
	public DataObjectQueryResult<EntityDataObject<E>> getContent() {
		return this.content;
	}
	
	@Override
	public FetchOptions getFetchOptions() {		
		return getContent().getFetchOptions();
	}
	
	@Override
	public int size() {		
		return getContent().size();
	}
	
	@Override
	public Boolean isLastPage() {		
		return getContent().isLastPage();
	}
	
	@Override
	public long getOffset() {		
		return getContent().getOffset();
	}
	
	@Override
	public Long available() {
		return getContent().available();
	}

	@Override
	public DataObjectQueryResult<EntityDataObject<E>> getResult() {
		return getContent();
	}	
}
