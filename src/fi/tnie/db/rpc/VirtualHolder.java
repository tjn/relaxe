/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.VirtualType;


/**
 * 
 * @author tnie
 *
 * @param <S> Type of the virtualized value.
 * @param <T> Type used to implement <code>S</code>.
 * @param <I> Primitive type corresponding <code>T</code>
 * @param <H> Primitive holder corresponding <code>T</code> and <code>I</code>.  
 * @param <V> Primitive type which represent the type of this class.
 * @param <Z> Subclassed virtual holder.
 */
public abstract class VirtualHolder<
	S extends Serializable,
	T extends Serializable,
	I extends PrimitiveType<I>,
	H extends PrimitiveHolder<T, I>,
	V extends PrimitiveType<V>,
	Z extends VirtualType<Z, I, V>,
	VH extends VirtualHolder<S, T, I, H, V, Z, VH>
>
	extends PrimitiveHolder<S, Z> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1151472831021160920L;	
	private S value;
	
	public VirtualHolder(S newValue) {
		this.value = newValue;		
	}
			
	public abstract H implementedAs();
	
	@Override
	public S value() {	
		return this.value;
	}
	
	@Override
	public int getSqlType() {
		return getType().getSqlType();
	}
}
