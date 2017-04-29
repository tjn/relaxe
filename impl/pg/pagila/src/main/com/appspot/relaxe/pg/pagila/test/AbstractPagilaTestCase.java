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
package com.appspot.relaxe.pg.pagila.test;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.pg.AbstractPGTestCase;
import com.appspot.relaxe.pg.pagila.PagilaPersistenceContext;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.rdbms.pg.PGImplementation;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;

public abstract class AbstractPagilaTestCase
	extends AbstractPGTestCase {

	@Override
	protected PersistenceContext<PGImplementation> createPersistenceContext() {
		return new PagilaPersistenceContext();
	}

	@Override
	public String getDatabase() {
		return "pagila";
	}
		
	public TableMapper getDefaultTableMapper() {
		return getDefaultTableMapper(getDatabase());
	}


	protected <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, B, H, F, M>, 
		E extends Entity<A, R, T, E, B, H, F, M>, 
		B extends MutableEntity<A, R, T, E, B, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, B, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	void testPrimaryKeyEntity(E pk) {		
		testImmutable(pk);
		
		for (A attr : pk.attributes()) {
			ValueHolder<?, ?, ?> vh = pk.value(attr);
			assertNotNull(attr.identifier(), vh);
			assertNotNull(attr.identifier(), vh.value());			
		}		
	}
	
	
	protected <
		A extends AttributeName, 
		R extends Reference, 
		T extends ReferenceType<A, R, T, E, B, H, F, M>, 
		E extends Entity<A, R, T, E, B, H, F, M>, 
		B extends MutableEntity<A, R, T, E, B, H, F, M>, 
		H extends ReferenceHolder<A, R, T, E, H, M>, 
		F extends EntityFactory<E, B, H, M, F>, 
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	> 
	void testImmutable(E ie) {
		assertNotNull(ie);
		assertSame(ie, ie.asImmutable());				
		assertSame(ie, ie.toImmutable());
								
		E ri = ie.asRead();
		assertNotNull(ri);
		
		B wi = ie.asWrite();
		assertNull(wi);
		
		B me = ie.toMutable();		
		assertNotNull(me);		
		assertNotSame(ie, me);
				
		assertEquals(ie.attributes(), me.attributes());
		
		M meta = ie.getMetaData();
						
		for (A attr : ie.attributes()) {			
			Column c = meta.getColumn(attr);
			assertEquals(meta.type().getClass().getCanonicalName() + "." + attr.toString(), ie.get(c), me.get(c));
		}		
	}	


}
