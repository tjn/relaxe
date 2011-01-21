/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;



import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.CharValue;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DateValue;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntegerValue;
import fi.tnie.db.ent.value.Key;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.TimestampValue;
import fi.tnie.db.ent.value.Value;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.ent.value.VarcharValue;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
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

//	@Override
//	public Map<A, PrimitiveHolder<?, ?>> values() {
//		if (values == null) {
//			values = new HashMap<A, PrimitiveHolder<?, ?>>();
//		}
//
//		return values;
//	}

	@Override
	protected Map<R, ReferenceHolder<?, ?, ?, ?>> references() {
		if (refs == null) {
			refs = new HashMap<R, ReferenceHolder<?,?,?,?>>();
		}

		return refs;
	}




//	public <S extends Serializable, P extends fi.tnie.db.types.PrimitiveType<P>, H extends fi.tnie.db.rpc.PrimitiveHolder<S,P>, K extends fi.tnie.db.ent.Key<A,S,P,H,E,K>> fi.tnie.db.ent.Value<A,S,P,H,E,K> value(K k) {
//		ValueFactory<A, S, P, H, E, K> vf = k.valueFactory();
//		newValueMap = vf.newValueMap();
//
//	};



	private Map<VarcharKey<A, E>, VarcharValue<A, E>> varcharValueMap;
	private Map<IntegerKey<A, E>, IntegerValue<A, E>> intValueMap;

	private Map<IntegerKey<A, E>, IntegerValue<A, E>> getIntValueMap() {
		if (intValueMap == null) {
			intValueMap = new HashMap<IntegerKey<A, E>, IntegerValue<A, E>>();
		}

		return intValueMap;
	}

	private Map<VarcharKey<A, E>, VarcharValue<A, E>> getVarcharValueMap() {
		if (varcharValueMap == null) {
			varcharValueMap = new HashMap<VarcharKey<A, E>, VarcharValue<A, E>>();
		}

		return varcharValueMap;
	}

	private Map<DateKey<A, E>, DateValue<A, E>> dateValueMap;

	private Map<DateKey<A, E>, DateValue<A, E>> getDateValueMap() {
		if (dateValueMap == null) {
			dateValueMap = new HashMap<DateKey<A, E>, DateValue<A, E>>();
		}

		return dateValueMap;
	}

	@Override
	public DateValue<A, E> dateValue(DateKey<A, E> k) {
		Map<DateKey<A, E>, DateValue<A, E>> vm = getDateValueMap();
		DateValue<A, E> value = vm.get(k);

		if (value == null) {
			DateValue<A, E> v = k.newValue();
			vm.put(k, v);
		}

		return value;
	}


	@Override
	public IntegerValue<A, E> integerValue(IntegerKey<A, E> k) {
		Map<IntegerKey<A, E>, IntegerValue<A, E>> vm = getIntValueMap();
		IntegerValue<A, E> value = vm.get(k);

		if (value == null) {
			value = k.newValue();
			vm.put(k, value);
		}

		return value;
	}

	@Override
	public VarcharValue<A, E> varcharValue(VarcharKey<A, E> k) {
		Map<VarcharKey<A, E>, VarcharValue<A, E>> vm = getVarcharValueMap();
		VarcharValue<A, E> value = vm.get(k);

		if (value == null) {
			value = k.newValue();
			vm.put(k, value);
		}

		return value;
	}

	private Map<TimestampKey<A, E>, TimestampValue<A, E>> timestampValueMap;

	private Map<TimestampKey<A, E>, TimestampValue<A, E>> getTimestampValueMap() {
		if (timestampValueMap == null) {
			timestampValueMap = new HashMap<TimestampKey<A, E>, TimestampValue<A, E>>();
		}

		return timestampValueMap;
	}

	@Override
	public TimestampValue<A, E> timestampValue(TimestampKey<A, E> k) {
		Map<TimestampKey<A, E>, TimestampValue<A, E>> vm = getTimestampValueMap();
		TimestampValue<A, E> value = vm.get(k);

		if (value == null) {
			value = k.newValue();
			vm.put(k, value);
		}

		return value;
	}


	private Map<CharKey<A, E>, CharValue<A, E>> charValueMap;

	private Map<CharKey<A, E>, CharValue<A, E>> getCharValueMap() {
		if (charValueMap == null) {
			charValueMap = new HashMap<CharKey<A, E>, CharValue<A, E>>();
		}

		return charValueMap;
	}

	@Override
	public CharValue<A, E> charValue(CharKey<A, E> k) {
		Map<CharKey<A, E>, CharValue<A, E>> vm = getCharValueMap();
		CharValue<A, E> value = vm.get(k);

		if (value == null) {
			value = k.newValue();
			vm.put(k, value);
		}

		return value;
	}



	@Override
	public <S extends Serializable, P extends PrimitiveType<P>, H extends PrimitiveHolder<S, P>, K extends Key<A, S, P, H, E, K>> Value<A, S, P, H, E, K> value(
			K k) {
		return k.value(self());
	}

	public fi.tnie.db.ent.value.Value<A,?,?,?,E,?> value(A attribute) {
		return getMetaData().getKey(attribute).value(self());
	};

}
