/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.util.EnumMap;
import java.util.EnumSet;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;

public abstract class EnumMetaData<
	A extends Enum<A> & Attribute,
	R extends Enum<R> & Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,	
	F extends EntityFactory<E, H, M, F, C>,	
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
> 
	extends DefaultEntityMetaData<A, R, T, E, H, F, M, C> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8574938084185912154L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected EnumMetaData() {
	}
		
	@Override
	protected void populate(BaseTable table) {
		populateAttributes(table);
		populateReferences(table);	
	}
	
	protected void populateAttributes(BaseTable table) {
		Class<A> t = getAttributeNameType();
		EnumSet<A> as = EnumSet.allOf(t);
		EnumMap<A, Column> am = new EnumMap<A, Column>(t);		
		populateAttributes(as, am, table);		
	}
	
	protected void populateReferences(BaseTable table) {
		Class<R> t = getReferenceNameType();
		EnumSet<R> rs = EnumSet.allOf(t);
		EnumMap<R, ForeignKey> rm = new EnumMap<R, ForeignKey>(t);		
		populateReferences(rs, rm, table);		
	}
		
	public abstract Class<A> getAttributeNameType();
	
	public abstract Class<R> getReferenceNameType();
}
