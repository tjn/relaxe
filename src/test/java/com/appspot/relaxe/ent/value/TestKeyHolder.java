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
package com.appspot.relaxe.ent.value;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerAttribute;
import com.appspot.relaxe.ent.value.HasLong;
import com.appspot.relaxe.ent.value.IntegerAttribute;
import com.appspot.relaxe.ent.value.LongAttribute;
import com.appspot.relaxe.ent.value.ValueAttribute;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public class TestKeyHolder
	implements HasInteger<TestKeyHolder.Attribute, TestKeyHolder>, HasLong<TestKeyHolder.Attribute, TestKeyHolder> {

	
	
	
	

	<
		S extends Serializable,
		P extends AbstractPrimitiveType<P>,
		PH extends AbstractPrimitiveHolder<S, P, PH>,
		K extends ValueAttribute<TestKeyHolder.Attribute, TestKeyHolder, S, P, PH, K>
	>
	PH get(K k) {
		PH ph = k.get(this);
		return ph;
	}
	
	
	private IntegerHolder a1 = null;
	private IntegerHolder a2 = null;
	
	
	private Map<TestKeyHolder.Attribute, LongHolder> longHolderMap = new TreeMap<TestKeyHolder.Attribute, LongHolder>();
	
	enum Attribute implements com.appspot.relaxe.ent.AttributeName
	{
		A1(IntegerType.TYPE),
		A2(IntegerType.TYPE),
		B1(LongType.TYPE),
		B2(LongType.TYPE),
		;
		
		
	    private String identifier;
	    private AbstractPrimitiveType<?> type;
	        
	    Attribute(AbstractPrimitiveType<?> type) {
	    	this.type = type;
	    }
	    
	    Attribute(String identifier, AbstractPrimitiveType<?> type) {
	    	this(type);
	        this.identifier = identifier;
	    }    
	    
	    @Override
	    public String identifier() {
	        return (identifier == null) ? name() : identifier;
	    }

	    @Override
		public AbstractPrimitiveType<?> type() {
	    	return this.type;
	    }    
	}

	@Override
	public IntegerHolder getInteger(IntegerAttribute<Attribute, TestKeyHolder> key) {
		switch (key.name()) {
		case A1:
			return a1;
		case A2:
			return a2;
		default:
			break;
		}
		
		return null;
	}

	@Override
	public void setInteger(IntegerAttribute<Attribute, TestKeyHolder> key, IntegerHolder newValue) {
		switch (key.name()) {
		case A1:
			a1 = newValue;
			break;
		case A2:
			a2 = newValue;
			break;
		default:
			break;
		}		
	}

		

	@Override
	public LongHolder getLong(LongAttribute<Attribute, TestKeyHolder> key) {
		return longHolderMap.get(key.name());		
	}

	@Override
	public void setLong(LongAttribute<Attribute, TestKeyHolder> key,
			LongHolder newValue) {
		longHolderMap.put(key.name(), newValue);		
	}
	
	
	public static void main(String[] args) {
		TestKeyHolder e = new TestKeyHolder();
		
		HasIntegerAttribute<Attribute, TestKeyHolder> meta = new HasIntegerAttribute<TestKeyHolder.Attribute, TestKeyHolder>() {

			private IntegerAttribute<Attribute, TestKeyHolder> a1k = null;
			private IntegerAttribute<Attribute, TestKeyHolder> a2k = null;
			
			@Override
			public IntegerAttribute<Attribute, TestKeyHolder> getIntegerAttribute(TestKeyHolder.Attribute a) {
				switch (a) {
				case A1:
					return a1k;
				case A2:
					return a2k;
				default:
					break;
				}
				
				return null;
			}

			@Override
			public void register(IntegerAttribute<Attribute, TestKeyHolder> key) {
				switch (key.name()) {
				case A1:
					a1k = key;
					break;
				case A2:
					a1k = key;
					break;
				default:
					break;
				}
			}
		};
		
		IntegerAttribute<TestKeyHolder.Attribute, TestKeyHolder> a1k = IntegerAttribute.get(meta, TestKeyHolder.Attribute.A1);
			
		IntegerHolder h = e.get(a1k);		
		
	}
}
