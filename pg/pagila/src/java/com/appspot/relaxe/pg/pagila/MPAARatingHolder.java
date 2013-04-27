/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.util.EnumMap;

import com.appspot.relaxe.rpc.EnumHolder;
import com.appspot.relaxe.rpc.OtherHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;


public class MPAARatingHolder
	extends EnumHolder<MPAARating, MPAARatingType, MPAARatingHolder> {

	public static final MPAARatingHolder NULL_HOLDER = new MPAARatingHolder();
	
	private static EnumMap<MPAARating, MPAARatingHolder> holderMap = new EnumMap<MPAARating, MPAARatingHolder>(MPAARating.class);
	
	static {
		for (MPAARating r : MPAARating.values()) {
			holderMap.put(r, new MPAARatingHolder(r));
		}
	}
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -973907139465998994L;
	
	public MPAARatingHolder(MPAARating value) {
		super(value);		
	}
		
	private MPAARatingHolder() {		
	}

	@Override
	public MPAARatingHolder self() {
		return this;
	}
	
	public static MPAARatingHolder valueOf(MPAARating v) {
		return (v == null) ? NULL_HOLDER : MPAARatingHolder.holderMap.get(v);
	}
	
	public static MPAARatingHolder valueOf(String s) {
		return (s == null) ? NULL_HOLDER : valueOf(MPAARating.parse(s));
	}
	
	public static MPAARatingHolder valueOf(OtherHolder<?, ?, ?> h) {
		if (h == null) {
			return null; 
		}
		
		if (!MPAARatingType.TYPE.equals(h.getType())) {
			throw new IllegalArgumentException("can not convert type: " + h.getType().getName() + " into " + MPAARatingType.TYPE.getName());
		}
						
		MPAARating v = (MPAARating) h.value();
		return valueOf(v);		
	}

	@Override
	public MPAARatingType getType() {
		return MPAARatingType.TYPE;
	}
	
	@Override
	public MPAARatingHolder asOtherHolder(String typeName) {	
		return getType().getName().equals(typeName) ? self() : null;
	}
		
	public static MPAARatingHolder of(PrimitiveHolder<?, ?, ?> holder) {
		Object h = holder.self();
		MPAARatingHolder mh = (MPAARatingHolder) h;	
		return mh;
	}
}
