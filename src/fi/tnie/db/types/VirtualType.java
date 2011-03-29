/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class VirtualType<T extends PrimitiveType<T>, P extends PrimitiveType<P>, V extends PrimitiveType<V>>
	extends PrimitiveType<T> {
		
	public abstract V virtualized();	
	public abstract P implementedAs();
		
	@Override
	public int getSqlType() {
		return implementedAs().getSqlType();
	}
	
}
