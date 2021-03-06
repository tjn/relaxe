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
package com.appspot.relaxe.model.ent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.value.Attribute;
import com.appspot.relaxe.model.ChangeListener;
import com.appspot.relaxe.model.DefaultMutableValueModel;
import com.appspot.relaxe.model.MutableValueModel;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;

public abstract class DefaultAttributeModelMap<
	A extends AttributeName,
	T extends ReferenceType<A, ?, T, E, B, ?, ?, ?>,
	E extends Entity<A, ?, T, E, B, ?, ?, ?>,
	B extends MutableEntity<A, ?, T, E, B, ?, ?, ?>,
	V extends Serializable,
	P extends ValueType<P>,
	H extends ValueHolder<V, P, H>,
	D extends AttributeModelMap<A, V, P, H, T, E, B, D>	
	>
	implements AttributeModelMap<A, V, P, H, T, E, B, D> {
	 
			
	public DefaultAttributeModelMap() {		
	}	

	private Map<A, MutableValueModel<H>> modelMap;

	private Map<A, MutableValueModel<H>> getValueModelMap() {
		if (modelMap == null) {
			modelMap = new HashMap<A, MutableValueModel<H>>();			
		}

		return modelMap;
	}
	
//	public ValueModel<String> getVarcharModel(A a) {
//		final VarcharKey<A, T, E> k = VarcharKey.get(target.getMetaData(), a);		
//		return (k == null) ? null : attr(k);
//	}
	
//	public StringModel getCharModel(A a) {
//		final CharKey<A, E> k = CharKey.get(target.getMetaData(), a);		
//		return (k == null) ? null : attr(k);
//	}

	
	@Override
	public <
		K extends com.appspot.relaxe.ent.value.Attribute<A,E,B,V,P,H,K>
	> 
	MutableValueModel<H> attr(final K k) throws EntityRuntimeException {
		if (k == null) {
			throw new NullPointerException("key");
		}
		
		Map<A, MutableValueModel<H>> mm = getValueModelMap();		
		MutableValueModel<H> m = mm.get(k.name());
		
		if (m == null) {
			H h = k.get(getTarget().as());
			MutableValueModel<H> nm = createValueModel(k, h);
									
			nm.addChangeHandler(new ChangeListener<H>() {				
				@Override
				public void changed(H from, H to) {					
					getTarget().set(k, to);
				}
			});
			
			m = nm;			
			mm.put(k.name(), nm);		
		}
		
		return m;
	}

	public <		 
		K extends Attribute<A, E, B, V, P, H, K>
	> 
	MutableValueModel<H> createValueModel(K k, H initialValue) {
		MutableValueModel<H> nm = new DefaultMutableValueModel<H>(initialValue);
		return nm;
	}
	
	public abstract B getTarget();
}
