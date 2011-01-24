/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;



import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.Key;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.ReferenceType;

/**
 *	TODO:
		This does not handle well enough the case
		of overlapping foreign-keys:
		If the foreign key A "contains" another foreign key B (
		the set of columns in a foreign key A contains
		the columns of another foreign key B as a proper subset),
		we could assume the table <code>T</code> <code>A</code> references also contains
		a foreign key <code>C</code> which also references table <code>T</code>.

		Proper implementation should probably set conflicting references to <code>null</code>.

 * @author Administrator
 *
 * @param <A>
 * @param <R>
 * @param <Q>
 * @param <E>
 */


public abstract class DefaultEntity<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AbstractEntity<A, R, T, E> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3498823449580706161L;
	private Map<A, PrimitiveHolder<?, ?>> values;
	private Map<R, ReferenceHolder<?, ?, ?, ?>> refs;

//	private static Logger logger = Logger.getLogger(DefaultEntity.class);

	protected DefaultEntity() {
		super();
	}

	@Override
	protected Map<R, ReferenceHolder<?, ?, ?, ?>> references() {
		if (refs == null) {
			refs = new HashMap<R, ReferenceHolder<?,?,?,?>>();
		}

		return refs;
	}

	private Map<VarcharKey<A, E>, VarcharHolder> varcharValueMap;
	private Map<IntegerKey<A, E>, IntegerHolder> intValueMap;

	private Map<IntegerKey<A, E>, IntegerHolder> getIntValueMap() {
		if (intValueMap == null) {
			intValueMap = new HashMap<IntegerKey<A, E>, IntegerHolder>();
		}

		return intValueMap;
	}

	private Map<VarcharKey<A, E>, VarcharHolder> getVarcharValueMap() {
		if (varcharValueMap == null) {
			varcharValueMap = new HashMap<VarcharKey<A, E>, VarcharHolder>();
		}

		return varcharValueMap;
	}

	private Map<DateKey<A, E>, DateHolder> dateValueMap;

	private Map<DateKey<A, E>, DateHolder> getDateValueMap() {
		if (dateValueMap == null) {
			dateValueMap = new HashMap<DateKey<A, E>, DateHolder>();
		}

		return dateValueMap;
	}

	@Override
	public DateHolder getDate(DateKey<A, E> k) {
		return getDateValueMap().get(k);	
	}


	@Override
	public IntegerHolder getInteger(IntegerKey<A, E> k) {
		return getIntValueMap().get(k);
	}

	@Override
	public VarcharHolder getVarchar(VarcharKey<A, E> k) {
		return getVarcharValueMap().get(k);
	}

	private Map<TimestampKey<A, E>, TimestampHolder> timestampValueMap;

	private Map<TimestampKey<A, E>, TimestampHolder> getTimestampValueMap() {
		if (timestampValueMap == null) {
			timestampValueMap = new HashMap<TimestampKey<A, E>, TimestampHolder>();
		}

		return timestampValueMap;
	}

	@Override
	public TimestampHolder getTimestamp(TimestampKey<A, E> k) {
		return getTimestampValueMap().get(k);
	}

	private Map<CharKey<A, E>, CharHolder> charValueMap;

	private Map<CharKey<A, E>, CharHolder> getCharValueMap() {
		if (charValueMap == null) {
			charValueMap = new HashMap<CharKey<A, E>, CharHolder>();
		}

		return charValueMap;
	}
	
	public <
	S extends Serializable, 
	P extends fi.tnie.db.types.PrimitiveType<P>, 
	H extends fi.tnie.db.rpc.PrimitiveHolder<S,P>, 
	K extends fi.tnie.db.ent.value.Key<A,S,P,H,E,K>> 
	void set(K k, H newValue) {
		k.set(self(), newValue);		
	};

	@Override
	public CharHolder getChar(CharKey<A, E> k) {
		return getCharValueMap().get(k);
	}

	public PrimitiveHolder<?, ?> get(A attribute) {
		return getMetaData().getKey(attribute).get(self());
	}

	@Override
	public void setInteger(IntegerKey<A, E> k, IntegerHolder newValue) {
		getIntValueMap().put(k, newValue);		
	}

	@Override
	public void setVarchar(VarcharKey<A, E> k, VarcharHolder newValue) {
		getVarcharValueMap().put(k, newValue);		
	}

	@Override
	public void setChar(CharKey<A, E> k, CharHolder newValue) {
		getCharValueMap().put(k, newValue);		
	}

	@Override
	public void setDate(DateKey<A, E> k, DateHolder newValue) {
		getDateValueMap().put(k, newValue);		
	}

	@Override
	public void setDouble(DoubleKey<A, E> k, DoubleHolder newValue) {
		// TODO: double value map
//		getDoubleValueMap().put(k, newValue);		
	}

	@Override
	public void setTimestamp(TimestampKey<A, E> k, TimestampHolder newValue) {
		getTimestampValueMap().put(k, newValue);		
	};
	
	public fi.tnie.db.rpc.PrimitiveHolder<?,?> value(A attribute) {
		Key<A, ?, ?, ?, E, ?> key = getMetaData().getKey(attribute);
		return key.get(self());		
	};

	public <S extends Serializable, P extends fi.tnie.db.types.PrimitiveType<P>, H extends fi.tnie.db.rpc.PrimitiveHolder<S,P>, K extends fi.tnie.db.ent.value.Key<A,S,P,H,E,K>> H get(K k) {
		return k.get(self());		
	};
	

}
