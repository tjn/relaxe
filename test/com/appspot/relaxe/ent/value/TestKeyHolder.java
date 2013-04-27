/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerKey;
import com.appspot.relaxe.ent.value.HasLong;
import com.appspot.relaxe.ent.value.IntegerKey;
import com.appspot.relaxe.ent.value.LongKey;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.LongType;
import com.appspot.relaxe.types.PrimitiveType;


public class TestKeyHolder
	implements HasInteger<TestKeyHolder.Attribute, TestKeyHolder>, HasLong<TestKeyHolder.Attribute, TestKeyHolder> {

	
	
	
	

	<
		S extends Serializable,
		P extends PrimitiveType<P>,
		PH extends PrimitiveHolder<S, P, PH>,
		K extends PrimitiveKey<TestKeyHolder.Attribute, TestKeyHolder, S, P, PH, K>
	>
	PH get(K k) {
		PH ph = k.get(this);
		return ph;
	}
	
	
	private IntegerHolder a1 = null;
	private IntegerHolder a2 = null;
	
	
	private Map<TestKeyHolder.Attribute, LongHolder> longHolderMap = new TreeMap<TestKeyHolder.Attribute, LongHolder>();
	
	enum Attribute implements com.appspot.relaxe.ent.Attribute
	{
		A1(IntegerType.TYPE),
		A2(IntegerType.TYPE),
		B1(LongType.TYPE),
		B2(LongType.TYPE),
		;
		
		
	    private String identifier;
	    private PrimitiveType<?> type;
	        
	    Attribute(PrimitiveType<?> type) {
	    	this.type = type;
	    }
	    
	    Attribute(String identifier, PrimitiveType<?> type) {
	    	this(type);
	        this.identifier = identifier;
	    }    
	    
	    @Override
	    public String identifier() {
	        return (identifier == null) ? name() : identifier;
	    }

	    public PrimitiveType<?> type() {
	    	return this.type;
	    }    
	}

	@Override
	public IntegerHolder getInteger(IntegerKey<Attribute, TestKeyHolder> key) {
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
	public void setInteger(IntegerKey<Attribute, TestKeyHolder> key, IntegerHolder newValue) {
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
	public LongHolder getLong(LongKey<Attribute, TestKeyHolder> key) {
		return longHolderMap.get(key.name());		
	}

	@Override
	public void setLong(LongKey<Attribute, TestKeyHolder> key,
			LongHolder newValue) {
		longHolderMap.put(key.name(), newValue);		
	}
	
	
	public static void main(String[] args) {
		TestKeyHolder e = new TestKeyHolder();
		
		HasIntegerKey<Attribute, TestKeyHolder> meta = new HasIntegerKey<TestKeyHolder.Attribute, TestKeyHolder>() {

			private IntegerKey<Attribute, TestKeyHolder> a1k = null;
			private IntegerKey<Attribute, TestKeyHolder> a2k = null;
			
			@Override
			public IntegerKey<Attribute, TestKeyHolder> getIntegerKey(TestKeyHolder.Attribute a) {
				switch (a) {
				case A1:
					return a1k;
				case A2:
					return a2k;
				}
				
				return null;
			}

			@Override
			public void register(IntegerKey<Attribute, TestKeyHolder> key) {
				switch (key.name()) {
				case A1:
					a1k = key;
					break;
				case A2:
					a1k = key;
					break;
				}
			}
		};
		
		IntegerKey<TestKeyHolder.Attribute, TestKeyHolder> a1k = IntegerKey.get(meta, TestKeyHolder.Attribute.A1);
	
		
		IntegerHolder h = e.get(a1k);
		
		
	}
	
	private Map<TestKeyHolder.Attribute, LongHolder> getLongHolderMap() {
		if (longHolderMap == null) {
			longHolderMap = new TreeMap<Attribute, LongHolder>();			
		}

		return longHolderMap;
	}

}
