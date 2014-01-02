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
/**
 *
 */
package com.appspot.relaxe;

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public class EntityReader<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	QE extends EntityQueryElement<A, R, T, E, H, F, M, QE>
>
	extends EntityBuilderManager<A, R, T, E, H, F, M, QE> {

	private List<EntityDataObject<E>> content;

	public EntityReader(ValueExtractorFactory vef, EntityQueryExpressionBuilder<A, R, T, E, H, F, M, QE> builder, UnificationContext unificationContext)
		throws QueryException {
		this(vef, builder, new ArrayList<EntityDataObject<E>>(), unificationContext);
	}

	public EntityReader(ValueExtractorFactory vef, EntityQueryExpressionBuilder<A, R, T, E, H, F, M, QE> builder, List<EntityDataObject<E>> result, UnificationContext identityContext)
		throws QueryException {
		super(vef, builder, identityContext);

		if (result == null) {
			throw new NullPointerException("result");
		}

		this.content = result;
	}

	@Override
	public void process(EntityDataObject<E> e) {
		content.add(e);
	}

	public List<EntityDataObject<E>> getContent() {
		return content;
	}

}