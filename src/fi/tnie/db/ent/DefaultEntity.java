/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DecimalKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.StringKey;
import fi.tnie.db.ent.value.TimeKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.StringHolder;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
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
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,	
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>, 
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
>
	extends AbstractEntity<A, R, T, E, H, F, M, C> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3498823449580706161L;
//	private Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> refs;
			
	private List<Map<A, ?>> valueMapList;
	
	private Map<A, VarcharHolder> varcharValueMap;
	private Map<A, IntegerHolder> intValueMap;
	private Map<A, CharHolder> charValueMap;
	private Map<A, DateHolder> dateValueMap;
	private Map<A, TimestampHolder> timestampValueMap;
	private Map<A, TimeHolder> timeValueMap;
	private Map<A, DoubleHolder> doubleValueMap;
	private Map<A, DecimalHolder> decimalValueMap;
	
	private Map<A, IntervalHolder.YearMonth> yearMonthIntervalValueMap;
	private Map<A, IntervalHolder.DayTime> dayTimeIntervalValueMap;
	
//	private static Logger logger = Logger.getLogger(DefaultEntity.class);

	protected DefaultEntity() {
		super();
	}

//	@Override
//	protected Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> references() {
//		if (refs == null) {
//			refs = new HashMap<R, ReferenceHolder<?,?,?,?,?,?>>();
//		}
//
//		return refs;
//	}

	private Map<A, IntegerHolder> getIntValueMap() {
		if (intValueMap == null) {
			intValueMap = new HashMap<A, IntegerHolder>();
			getValueMapList().add(intValueMap);
		}

		return intValueMap;
	}

	private Map<A, VarcharHolder> getVarcharValueMap() {
		if (varcharValueMap == null) {
			varcharValueMap = new HashMap<A, VarcharHolder>();
			getValueMapList().add(varcharValueMap);
		}

		return varcharValueMap;
	}

	private Map<A, DateHolder> getDateValueMap() {
		if (dateValueMap == null) {
			dateValueMap = new HashMap<A, DateHolder>();
			getValueMapList().add(dateValueMap);
		}

		return dateValueMap;
	}

	@Override
	public DateHolder getDate(DateKey<A, T, E> k) {
		return getDateValueMap().get(k.name());	
	}

	private Map<A, DoubleHolder> getDoubleValueMap() {
		if (doubleValueMap == null) {
			doubleValueMap = new HashMap<A, DoubleHolder>();
			getValueMapList().add(doubleValueMap);
		}

		return doubleValueMap;
	}
	
	private Map<A, DecimalHolder> getDecimalValueMap() {
		if (decimalValueMap == null) {
			decimalValueMap = new HashMap<A, DecimalHolder>();
			getValueMapList().add(decimalValueMap);
		}

		return decimalValueMap;
	}

	@Override
	public DoubleHolder getDouble(DoubleKey<A, T, E> k) {
		return getDoubleValueMap().get(k.name());	
	}	

	@Override
	public DecimalHolder getDecimal(DecimalKey<A, T, E> k) {
		return getDecimalValueMap().get(k.name());	
	}	

	@Override
	public IntegerHolder getInteger(IntegerKey<A, T, E> k) {
		return getIntValueMap().get(k.name());
	}
	
	@Override
	public fi.tnie.db.rpc.IntervalHolder.DayTime getInterval(IntervalKey.DayTime<A, T, E> k) {
		return getDayTimeIntervalValueMap().get(k.name());
	}
	
	@Override
	public fi.tnie.db.rpc.IntervalHolder.YearMonth getInterval(IntervalKey.YearMonth<A, T, E> k) {
		return getYearMonthIntervalValueMap().get(k.name());
	}
	
	

	@Override
	public VarcharHolder getVarchar(VarcharKey<A, T, E> k) {
		return getVarcharValueMap().get(k.name());
	}

	
	@Override
	public <
		P extends PrimitiveType<P>,
		SH extends StringHolder<P>,
		K extends StringKey<A, T, E, P, SH, K>
	>
	SH getString(K k) throws EntityRuntimeException {
		SH sh = get(k.self());
		return sh;
	}
	
	@Override
	public <
		P extends PrimitiveType<P>,
		SH extends StringHolder<P>,
		K extends StringKey<A, T, E, P, SH, K>
	>
	void setString(K k, SH s) throws EntityRuntimeException {		
		set(k.self(), s);
	}
	
	@Override
	public <
		P extends PrimitiveType<P>,
		SH extends StringHolder<P>,
		K extends StringKey<A, T, E, P, SH, K>
	>
	void setString(K k, String s) throws EntityRuntimeException {		
		set(k.self(), k.newHolder(s));
	}	

	private Map<A, TimestampHolder> getTimestampValueMap() {
		if (timestampValueMap == null) {
			timestampValueMap = new HashMap<A, TimestampHolder>();
			getValueMapList().add(timestampValueMap);
		}

		return timestampValueMap;
	}
	
	private Map<A, TimeHolder> getTimeValueMap() {
		if (timeValueMap == null) {
			timeValueMap = new HashMap<A, TimeHolder>();
		}

		return timeValueMap;
	}

	@Override
	public TimestampHolder getTimestamp(TimestampKey<A, T, E> k) {
		return getTimestampValueMap().get(k.name());
	}
	
	@Override
	public TimeHolder getTime(TimeKey<A, T, E> k) {
		return getTimeValueMap().get(k.name());
	}
	
	private Map<A, CharHolder> getCharValueMap() {
		if (charValueMap == null) {
			charValueMap = new HashMap<A, CharHolder>();
			getValueMapList().add(charValueMap);
		}

		return charValueMap;
	}
	
	
	public <
	S extends Serializable, 
	P extends fi.tnie.db.types.PrimitiveType<P>, 
	PH extends fi.tnie.db.rpc.PrimitiveHolder<S, P>, 
	K extends fi.tnie.db.ent.value.PrimitiveKey<A, T, E, S, P, PH, K>> 
	void set(K k, PH newValue) throws EntityRuntimeException {
		k.set(self(), newValue);		
	};

	@Override
	public CharHolder getChar(CharKey<A, T, E> k) {
		return getCharValueMap().get(k.name());
	}

	public PrimitiveHolder<?, ?> get(A attribute) throws EntityRuntimeException {
		return getMetaData().getKey(attribute).get(self());
	}

	@Override
	public void setInteger(IntegerKey<A, T, E> k, IntegerHolder newValue) {
		getIntValueMap().put(k.name(), newValue);		
	}

	@Override
	public void setVarchar(VarcharKey<A, T, E> k, VarcharHolder newValue) {
		getVarcharValueMap().put(k.name(), newValue);		
	}

	@Override
	public void setChar(CharKey<A, T, E> k, CharHolder newValue) {
		getCharValueMap().put(k.name(), newValue);		
	}

	@Override
	public void setDate(DateKey<A, T, E> k, DateHolder newValue) {
		getDateValueMap().put(k.name(), newValue);		
	}

	@Override
	public void setDouble(DoubleKey<A, T, E> k, DoubleHolder newValue) {
		getDoubleValueMap().put(k.name(), newValue);		
	}

	@Override
	public void setDecimal(DecimalKey<A, T, E> k, DecimalHolder newValue) {
		getDecimalValueMap().put(k.name(), newValue);		
	}

	@Override
	public void setTimestamp(TimestampKey<A, T, E> k, TimestampHolder newValue) {
		getTimestampValueMap().put(k.name(), newValue);		
	}
	
	@Override
	public void setTime(TimeKey<A, T, E> k, TimeHolder newValue) {
		getTimeValueMap().put(k.name(), newValue);		
	}
	
	@Override
	public void setInterval(IntervalKey.DayTime<A, T, E> k,
			fi.tnie.db.rpc.IntervalHolder.DayTime newValue) {
		getDayTimeIntervalValueMap().put(k.name(), newValue);
	}
	
	@Override
	public void setInterval(IntervalKey.YearMonth<A, T, E> k,
			fi.tnie.db.rpc.IntervalHolder.YearMonth newValue) {
		getYearMonthIntervalValueMap().put(k.name(), newValue);		
	}
	
	public fi.tnie.db.rpc.PrimitiveHolder<?,?> value(A attribute) throws EntityRuntimeException {
		PrimitiveKey<A, T, E, ?, ?, ?, ?> key = getMetaData().getKey(attribute);
		return key.get(self());
	};

	public <
		S extends Serializable, 
		P extends fi.tnie.db.types.PrimitiveType<P>, 
		PH extends fi.tnie.db.rpc.PrimitiveHolder<S, P>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A, T, E, S, P, PH, K>
	> PH get(K k) throws EntityRuntimeException {
		return k.get(self());
	}
	
		
	@Override
	public <
		RA extends Attribute,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
		RF extends EntityFactory<RE, RH, RM, RF, RC>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
		RC extends Content,
		RK extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, RK>
	> RH getRef(RK k) {
		return k.get(self());
	};
	

	private Map<A, IntervalHolder.YearMonth> getYearMonthIntervalValueMap() {
		if (yearMonthIntervalValueMap == null) {
			yearMonthIntervalValueMap = new HashMap<A, IntervalHolder.YearMonth>();
			getValueMapList().add(yearMonthIntervalValueMap);
		}

		return yearMonthIntervalValueMap;
	}

	private Map<A, IntervalHolder.DayTime> getDayTimeIntervalValueMap() {
		if (dayTimeIntervalValueMap == null) {
			dayTimeIntervalValueMap = new HashMap<A, IntervalHolder.DayTime>();
			getValueMapList().add(dayTimeIntervalValueMap);
		}

		return dayTimeIntervalValueMap;
	};
	
//	private Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> getRefs() {
//		if (refs == null) {
//			refs = new HashMap<R, ReferenceHolder<?, ?, ?, ?, ?, ?>>();			
//		}
//
//		return refs;
//	};
	
//	public <P extends fi.tnie.db.types.ReferenceType<P>, G extends fi.tnie.db.ent.Entity<?,?,P,G>, H extends fi.tnie.db.rpc.ReferenceHolder<?,?,P,G>, K extends fi.tnie.db.ent.value.EntityKey<A,R,T,E,P,G,H,? extends K>> void setRef(K k, H newValue) {
//		getRefs().put(k.name(), newValue);
//	}
	
	public <
		RA extends Attribute,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
		RF extends EntityFactory<RE, RH, RM, RF, RC>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
		RC extends Content,
		RK extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, RK>
	> 
	void setRef(RK k, RH newValue) {		
		k.set(self(), newValue);
	};
	
//	public Entity<?, ?, ?, ?, ?, ?, ?> getRef(R k) {
//		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRefs().get(k);				
//		return (rh == null) ? null : rh.value();		
//	}
	
	public Entity<?, ?, ?, ?, ?, ?, ?, ?> getRef(R r) {
		EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(r);
		ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return (rh == null) ? null : rh.value();		
	}
	
	
	
	public E copy() {
		M meta = getMetaData();
		F ef = meta.getFactory();				
		E src = self(); 
		E dest = ef.newInstance();
		
		for (A a : meta.attributes()) {
			PrimitiveKey<A, T, E, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.copy(src, dest);
		}
		
		for (R r : meta.relationships()) {
			EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
			ek.copy(src, dest);			
		}
		
		return dest;				
	}
	
	@Override
	public E unify(IdentityContext ctx) {	
		return getMetaData().unify(ctx, self());
	}

	public <		
		VV extends Serializable,
		VT extends PrimitiveType<VT>,
		VH extends PrimitiveHolder<VV, VT>,	
		K extends PrimitiveKey<A, T, E, VV, VT, VH, K>
	> 
	void remove(K key) {		
		set(key, null);
	}
	
	public <		
		VV extends Serializable,
		VT extends PrimitiveType<VT>,
		VH extends PrimitiveHolder<VV, VT>,	
		K extends PrimitiveKey<A, T, E, VV, VT, VH, K>
	> 
	void reset(K key) {
		VH nh = key.newHolder(null);
		set(key, nh);
	}
	
	public <
		VV extends Serializable, 
		VT extends fi.tnie.db.types.PrimitiveType<VT>, 
		VH extends fi.tnie.db.rpc.PrimitiveHolder<VV, VT>, 
		K extends PrimitiveKey<A, T, E, VV, VT, VH, K>
	> 
	boolean has(K key) {		
		return (this.get(key) != null);
	}
	
	@Override
	public Set<A> attributes() {
		if (this.valueMapList == null) {
			return Collections.emptySet();
		}
		
		Set<A> as = new HashSet<A>();				
		
		for (Map<A, ?> vm : valueMapList) {
			if (!vm.isEmpty()) {			
				as.addAll(vm.keySet());
			}
		}
				
		return as;
	}
	
	private List<Map<A, ?>> getValueMapList() {
		if (valueMapList == null) {
			valueMapList = new ArrayList<Map<A, ?>>();			
		}

		return valueMapList;
	}
	
	
	public fi.tnie.db.rpc.ReferenceHolder<?,?,?,?,?,?,?> ref(R ref) {
		EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = getMetaData().getEntityKey(ref);
		ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = getRef(k.self());
		return rh;
	};
	
	
	
//	public boolean has(R r) {
//		if (r == null || this.refs == null) {
//			return false;
//		}
//		
//		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = this.refs.get(r);		
//		return (rh != null);		
//	};
}
