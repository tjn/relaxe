/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.value.AbstractArrayKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.StringArray;
import fi.tnie.db.test.HasPGTextArray;
import fi.tnie.db.test.HasPGTextArrayKey;
import fi.tnie.db.types.ArrayType;
import fi.tnie.db.types.VarcharType;

public class PGTextArrayKey<
	A extends Attribute,
	E extends HasPGTextArray<A, E>	
>
	extends AbstractArrayKey<A, E, String, StringArray, VarcharType, PGTextArrayType, PGTextArrayHolder, PGTextArrayKey<A, E>>
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private PGTextArrayKey() {
	}
	
	private PGTextArrayKey(HasPGTextArrayKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasPGTextArray<X, T>
	>
	PGTextArrayKey<X, T> get(HasPGTextArrayKey<X, T> meta, X a) {
		PGTextArrayKey<X, T> k = meta.getPGTextArrayKey(a);
		
		if (k == null) {
			ArrayType<?, ?> t = a.type().asArrayType();
			
			if (t != null && PGTextArrayType.TYPE.equals(a.type())) {
				k = new PGTextArrayKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}	

			
	@Override
	public PGTextArrayType type() {
		return PGTextArrayType.TYPE;
	}
	
	public void set(E e, PGTextArrayHolder newValue) 
		throws EntityRuntimeException {
		e.setPGTextArray(this, newValue);
	}
	
	public PGTextArrayHolder get(E e) 
		throws EntityRuntimeException {
		return e.getPGTextArray(this);
	}
	
		
//	@Override
//	public PGTextArrayHolder newHolder(PGTextArray newValue) {
//		return PGTextArrayHolder.valueOf(newValue);
//	}

	@Override
	public void copy(E src, E dest) {
		dest.setPGTextArray(this, src.getPGTextArray(this));
	}

	@Override
	public PGTextArrayKey<A, E> self() {
		return this;
	}

	@Override
	public PGTextArrayHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return PGTextArrayHolder.of(holder);
	}

	@Override
	public PGTextArrayHolder newHolder(StringArray newValue) {
		return PGTextArrayHolder.newHolder(newValue);
	}
}
