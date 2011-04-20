/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.map.AttributeInfo;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;
import fi.tnie.db.types.PrimitiveType;

public class DefaultAttributeInfo
	implements AttributeInfo {
	
	private Table table;
	private Column column;
	private Class<?> attributeType;
	private Class<?> holderType;
	private Class<?> keyType;
	private Class<?> accessorType;
	private Class<?> identityMapType;
//	private Class<PrimitiveType<?>> primitiveType;
	private PrimitiveType<?> primitiveType;
	
	private static Map<Class<?>, Class<?>> primitiveTypeMap = new HashMap<Class<?>, Class<?>>();
	
	static {
		primitiveTypeMap.put(Boolean.class, Boolean.TYPE);
		primitiveTypeMap.put(Byte.class, Byte.TYPE);
		primitiveTypeMap.put(Double.class, Double.TYPE);
		primitiveTypeMap.put(Float.class, Float.TYPE);
		primitiveTypeMap.put(Integer.class, Integer.TYPE);
		primitiveTypeMap.put(Long.class, Long.TYPE);
		primitiveTypeMap.put(Short.class, Short.TYPE);		
	}
	
	
	public DefaultAttributeInfo(Table table, Column column) {
		super();
		this.table = table;
		this.column = column;
	}	
	
//	@Override
//	public Class<?> getPrimitiveType() {
//		return primitiveTypeMap.get(getAttributeType());
//	}

	@Override
	public Class<?> getAttributeType() {
		return this.attributeType;
	}

	@Override
	public Column getColumn() {
		return this.column;
	}

	@Override
	public Class<?> getHolderType() {
		return this.holderType;
	}

	@Override
	public Class<?> getKeyType() {
		return this.keyType;
	}

	@Override
	public Table getTable() {
		return this.table;
	}

	@Override
	public Class<?> getAccessorType() {
		return this.accessorType;
	}

	public void setAttributeType(Class<?> attributeType) {
		this.attributeType = attributeType;
	}

	public void setHolderType(Class<?> holderType) {
		this.holderType = holderType;
	}

	public void setKeyType(Class<?> keyType) {
		this.keyType = keyType;
	}

	public void setAccessorType(Class<?> valueType) {
		this.accessorType = valueType;
	}

	public PrimitiveType<?> getPrimitiveType() {
		return primitiveType;
	}

	public void setPrimitiveType(PrimitiveType<?> primitiveType) {
		this.primitiveType = primitiveType;
	}

	public Class<?> getIdentityMapType() {
		return identityMapType;
	}

	public void setIdentityMapType(Class<?> identityMapType) {
		this.identityMapType = identityMapType;
	}	
}
