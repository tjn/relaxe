/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DecimalKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.ent.value.PrimitiveKey;
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
import fi.tnie.db.rpc.TimeHolder;
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
	R extends Reference,
	T extends ReferenceType<T, M>,	
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>, 
	M extends EntityMetaData<A, R, T, E, H, F, M>
>
	extends AbstractEntity<A, R, T, E, H, F, M> {

	/**
	 *
	 */
	private static final long serialVersionUID = 3498823449580706161L;
	private Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> refs;
	
	
	/**
	 * TODO: 
	 * Consider having simple A as key here.  
	 * 
	 */	
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

	@Override
	protected Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> references() {
		if (refs == null) {
			refs = new HashMap<R, ReferenceHolder<?,?,?,?,?,?>>();
		}

		return refs;
	}

	private Map<A, IntegerHolder> getIntValueMap() {
		if (intValueMap == null) {
			intValueMap = new HashMap<A, IntegerHolder>();
		}

		return intValueMap;
	}

	private Map<A, VarcharHolder> getVarcharValueMap() {
		if (varcharValueMap == null) {
			varcharValueMap = new HashMap<A, VarcharHolder>();
		}

		return varcharValueMap;
	}

	private Map<A, DateHolder> getDateValueMap() {
		if (dateValueMap == null) {
			dateValueMap = new HashMap<A, DateHolder>();
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
		}

		return doubleValueMap;
	}
	
	private Map<A, DecimalHolder> getDecimalValueMap() {
		if (decimalValueMap == null) {
			decimalValueMap = new HashMap<A, DecimalHolder>();
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

	private Map<A, TimestampHolder> getTimestampValueMap() {
		if (timestampValueMap == null) {
			timestampValueMap = new HashMap<A, TimestampHolder>();
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
	
		
	public <
		P extends fi.tnie.db.types.ReferenceType<P, D>, 
		G extends fi.tnie.db.ent.Entity<?,?,P,G,RH,?,D>, 
		RH extends fi.tnie.db.rpc.ReferenceHolder<?,?,P,G, RH, D>, 
		D extends fi.tnie.db.ent.EntityMetaData<?,?,P,G,RH,?,D>, 
		K extends fi.tnie.db.ent.value.EntityKey<R,T,E,M,P,G,RH,D,K>
	> RH getRef(K k) {
		return k.get(self());
	};
	

	private Map<A, IntervalHolder.YearMonth> getYearMonthIntervalValueMap() {
		if (yearMonthIntervalValueMap == null) {
			yearMonthIntervalValueMap = new HashMap<A, IntervalHolder.YearMonth>();			
		}

		return yearMonthIntervalValueMap;
	}

	private Map<A, IntervalHolder.DayTime> getDayTimeIntervalValueMap() {
		if (dayTimeIntervalValueMap == null) {
			dayTimeIntervalValueMap = new HashMap<A, IntervalHolder.DayTime>();
			
		}

		return dayTimeIntervalValueMap;
	};
	
	private Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> getRefs() {
		if (refs == null) {
			refs = new HashMap<R, ReferenceHolder<?, ?, ?, ?, ?, ?>>();			
		}

		return refs;
	};
	
//	public <P extends fi.tnie.db.types.ReferenceType<P>, G extends fi.tnie.db.ent.Entity<?,?,P,G>, H extends fi.tnie.db.rpc.ReferenceHolder<?,?,P,G>, K extends fi.tnie.db.ent.value.EntityKey<A,R,T,E,P,G,H,? extends K>> void setRef(K k, H newValue) {
//		getRefs().put(k.name(), newValue);
//	}
	
	public <
		P extends fi.tnie.db.types.ReferenceType<P, D>, 
		G extends fi.tnie.db.ent.Entity<?,?,P,G,RH,?, D>, 
		RH extends fi.tnie.db.rpc.ReferenceHolder<?,?,P,G, RH, D>, 
		D extends fi.tnie.db.ent.EntityMetaData<?,?,P,G,RH,?,D>, K extends fi.tnie.db.ent.value.EntityKey<R,T,E,M,P,G,RH,D,K>> void setRef(K k, RH newValue) {
		
		k.set(self(), newValue);	

	};
	
	public Entity<?, ?, ?, ?, ?, ?, ?> getRef(R k) {
		ReferenceHolder<?, ?, ?, ?, ?, ?> rh = getRefs().get(k);				
		return (rh == null) ? null : rh.value();		
	}
	

	
	public E copy() {
		M meta = getMetaData();
		EntityFactory<E, H, M, ?> ef = meta.getFactory();
				
		E src = self(); 
		E dest = ef.newInstance();
		
		for (A a : meta.attributes()) {
			PrimitiveKey<A, T, E, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.copy(src, dest);
		}
		
		for (R r : meta.relationships()) {						
			EntityKey<R, T, E, M, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
			ek.copy(src, dest);			
		}
		
		return dest;				
	}
	
	@Override
	public E unify(IdentityContext ctx) {	
		return getMetaData().unify(ctx, self());
	}
	
}
