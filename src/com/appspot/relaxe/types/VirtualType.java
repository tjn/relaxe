/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;

public abstract class VirtualType<T extends AbstractPrimitiveType<T>, P extends AbstractPrimitiveType<P>, V extends AbstractPrimitiveType<V>>
	extends AbstractPrimitiveType<T> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -377670690526020985L;

	public abstract V virtualized();	
	public abstract P implementedAs();
		
	@Override
	public int getSqlType() {
		return implementedAs().getSqlType();
	}	
}
