/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.ent.im.EntityIdentityMap;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DecimalKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimeKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class DefaultEntityMetaData<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,	
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>
>
	extends AbstractEntityMetaData<A, R, T, E, H, F, M>
{
	private BaseTable baseTable;

	private Set<A> attributes;
	private Map<A, Column> attributeMap;
	private Map<Column, A> columnMap;
	private Set<Column> pkcols;

	private Set<R> relationships;
	private Map<R, ForeignKey> referenceMap;
	private Map<Column, Set<R>> columnReferenceMap;		
	private Map<A, PrimitiveKey<A, T, E, ?, ?, ?, ?>> keyMap;
	
	private Map<IdentityContext, EntityIdentityMap<A, R, T, E>> identityContextMap;
	
//	private Map<Column, Key<A, ?, ?, ?, E, ?>> columnKeyMap;

	protected DefaultEntityMetaData() {
	}

	@Override
	public void bind(BaseTable table) {
		this.baseTable = table;
		populateAttributes(table);
		populateReferences(table);
	}

	protected abstract void populateAttributes(BaseTable table);



	protected void populateAttributes(Set<A> attributes, Map<A, Column> attributeMap, BaseTable table) {

		// EnumSet<K> attributes = EnumSet.allOf(keyType);
		// EnumMap<K, Column> attributeMap = new EnumMap<K, Column>(keyType);

		//	this.attributes = EnumSet.allOf(keyType);
		//	this.attributeMap = new EnumMap<A, Column>(atype);
		Map<Column, A> columnMap = new HashMap<Column, A>();

		//	EnumSet<A> pka = EnumSet.noneOf(atype);

		Set<Column> pkc = new HashSet<Column>();

		for (A a : attributes) {
//			Column c = table.columnMap().get(a);
			Column c = map(table, a);

			if (c == null) {
				throw new NullPointerException(
						"no column for attribute: " + a + " in " +
						table.columns());
			}

			attributeMap.put(a, c);
			columnMap.put(c, a);

			if (c.isPrimaryKeyColumn()) {
				pkc.add(c);
			}
		}

		this.attributes = Collections.unmodifiableSet(attributes);
		this.attributeMap = attributeMap;
		this.pkcols = Collections.unmodifiableSet(pkc);
		this.columnMap = columnMap;
	}

	protected abstract Column map(BaseTable table, A a);

	protected abstract void populateReferences(BaseTable table);

	protected void populateReferences(Set<R> relationships, Map<R, ForeignKey> referenceMap, BaseTable table) {
//		this.references = EnumSet.allOf(rtype);
//		this.referenceMap = new EnumMap<R, ForeignKey>(rtype);
//
		Map<Column, Set<R>> rm = new HashMap<Column, Set<R>>();

		for (R r : relationships) {
			// ForeignKey fk = table.foreignKeys().get(r.identifier());
			ForeignKey fk = map(table, r);

			if (fk == null) {
				throw new NullPointerException("no such foreign key: " + r);
			}
			else {
				referenceMap.put(r, fk);
				populateColumnReferenceMap(fk, r, rm);
			}
		}

		// Ensure all the column-sets are unmodifiable after the call.
		// Column-sets which are size of 1 are expected to be created by
		// Collections.singleton and therefore unmodifiable.
		for (Map.Entry<Column, Set<R>> e : rm.entrySet()) {
			Set<R> cs = e.getValue();

			if (cs.size() > 1) {
				e.setValue(Collections.unmodifiableSet(cs));
			}
		}

		this.columnReferenceMap = rm;

		this.relationships = Collections.unmodifiableSet(relationships);
		this.referenceMap = referenceMap;
	}

	protected abstract ForeignKey map(BaseTable table, R r);

	private void populateColumnReferenceMap(ForeignKey fk, R r, Map<Column, Set<R>> dest) {
		for (Column fkcol : fk.columns().keySet()) {
			Set<R> rs = dest.get(fkcol);

			if (rs == null) {
				rs = Collections.singleton(r);
				dest.put(fkcol, rs);
			}
			else {
				if (rs.size() == 1) {
					rs = new HashSet<R>(rs);
					dest.put(fkcol, rs);
				}

				rs.add(r);
			}
		}
	}

	public Set<A> attributes() {
		return this.attributes;
	}

	public Set<R> relationships() {
		return this.relationships;
	}

	@Override
	public BaseTable getBaseTable() {
		return this.baseTable;
	}

	@Override
	public Column getColumn(A a) {
		return this.attributeMap.get(a);
	}

	@Override
	public A getAttribute(Column column) {
		if (column == null) {
			throw new NullPointerException("'column' must not be null");
		}

		return this.columnMap.get(column);
	}

	@Override
	public Set<Column> getPKDefinition() {
		return this.pkcols;
	}

	@Override
	public ForeignKey getForeignKey(R r) {
		return this.referenceMap.get(r);
	}

	@Override
	public Set<R> getReferences(Column c) {
		return this.columnReferenceMap.get(c);
	}

	@Override
	public Catalog getCatalog() {
	    BaseTable table = getBaseTable();
	    return (table == null) ? null : table.getSchema().getCatalog();
	}
	
	public fi.tnie.db.types.PrimitiveType<?> getAttributeType(A attribute) {
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}
		
		return attribute.type();
		
//		Column c = getColumn(attribute);
//		return PrimitiveType.get(c.getDataType().getDataType());		
	};

	
//	@Override
//	public void addKey(IntegerKey<A, E> key) {
//		add(key);
//	}

	private void add(fi.tnie.db.ent.value.PrimitiveKey<A,T,E,?,?,?,?> key) {
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		getKeyMap().put(key.name(), key);
	}
	
	
	public fi.tnie.db.ent.value.PrimitiveKey<A,T,E,?,?,?,?> getKey(A attribute) {
		if (attribute == null) {
			throw new NullPointerException("attribute");
		}
				
		return getKeyMap().get(attribute);
	}
	
	protected 
	<K extends PrimitiveKey<A, T, E, ?, ?, ?, ?>>
	void addAttributeKey(K key, Map<A, K> am) {
		add(key);
		
		if (am != null) {
			am.put(key.name(), key);
		}
	}
	
//	private java.util.Map<A, fi.tnie.db.ent.value.VarcharKey<A, E>> varcharKeyMap = new java.util.HashMap<A, fi.tnie.db.ent.value.VarcharKey<A, E>>();
//	private java.util.Map<A, fi.tnie.db.ent.value.IntegerKey<A, E>> integerKeyMap = new java.util.HashMap<A, fi.tnie.db.ent.value.IntegerKey<A, E>>();
//	private java.util.Map<A, fi.tnie.db.ent.value.DateKey<A, E>> dateKeyMap = new java.util.HashMap<A, fi.tnie.db.ent.value.DateKey<A, E>>();
//	private java.util.Map<A, fi.tnie.db.ent.value.TimestampKey<A, E>> timestampKeyMap = new java.util.HashMap<A, fi.tnie.db.ent.value.TimestampKey<A, E>>();
//	private java.util.Map<A, fi.tnie.db.ent.value.DoubleKey<A, E>> doubleKeyMap = new java.util.HashMap<A, fi.tnie.db.ent.value.DoubleKey<A, E>>();
//	private java.util.Map<A, fi.tnie.db.ent.value.CharKey<A, E>> charKeyMap = new java.util.HashMap<A, fi.tnie.db.ent.value.CharKey<A, E>>();
	
	public fi.tnie.db.ent.value.IntegerKey<A, T, E> getIntegerKey(A name) {
		return key(name, getIntegerKeyMap());
	}
	
	public fi.tnie.db.ent.value.IntervalKey.YearMonth<A, T, E> getYearMonthIntervalKey(A name) {		
		return key(name, getYearMonthIntervalKeyMap());		
	}
	
	public fi.tnie.db.ent.value.IntervalKey.DayTime<A, T, E> getDayTimeIntervalKey(A name) {		
		return key(name, getDayTimeIntervalKeyMap());		
	}	

	public fi.tnie.db.ent.value.VarcharKey<A, T, E> getVarcharKey(A name) {		 
		return key(name, getVarcharKeyMap());
	}
	
	public fi.tnie.db.ent.value.TimestampKey<A, T, E> getTimestampKey(A name) {		
		return key(name, getTimestampKeyMap());
	}

	public fi.tnie.db.ent.value.TimeKey<A, T, E> getTimeKey(A name) {		
		return key(name, getTimeKeyMap());
	}
	
	public fi.tnie.db.ent.value.DateKey<A, T, E> getDateKey(A name) {		
		return key(name, getDateKeyMap());
	}
	
	public fi.tnie.db.ent.value.DoubleKey<A, T, E> getDoubleKey(A name) {		
		return key(name, getDoubleKeyMap());
	}
	
	public fi.tnie.db.ent.value.DecimalKey<A, T, E> getDecimalKey(A name) {		
		return key(name, getDecimalKeyMap());
	}	
	
	public fi.tnie.db.ent.value.CharKey<A, T, E> getCharKey(A name) {		
		return key(name, getCharKeyMap());
	}
	
		
	protected java.util.Map<A, IntegerKey<A, T, E>> getIntegerKeyMap() {
		return null;
	}

	
	
	protected java.util.Map<A, IntervalKey.YearMonth<A, T, E>> getYearMonthIntervalKeyMap() {
		return null;
	}
	
	protected java.util.Map<A, IntervalKey.DayTime<A, T, E>> getDayTimeIntervalKeyMap() {
		return null;
	}
		
	protected java.util.Map<A, VarcharKey<A, T, E>> getVarcharKeyMap() {
		return null;
	}
	protected java.util.Map<A, CharKey<A, T, E>> getCharKeyMap() {
		return null;
	}	
	
	protected java.util.Map<A, DateKey<A, T, E>> getDateKeyMap() {
		return null;
	}
	
	protected java.util.Map<A, TimestampKey<A, T, E>> getTimestampKeyMap() {
		return null;
	}

	protected Map<A, TimeKey<A, T, E>> getTimeKeyMap() {
		return null;
	}	
	
	protected java.util.Map<A, DoubleKey<A, T, E>> getDoubleKeyMap() {
		return null;
	}
	
	protected java.util.Map<A, DecimalKey<A, T, E>> getDecimalKeyMap() {
		return null;
	}
	
    @Override
    public void addKey(DoubleKey<A, T, E> key) {
    	addAttributeKey(key, getDoubleKeyMap());    	
    }
    
    @Override
    public void addKey(DecimalKey<A, T, E> key) {
    	addAttributeKey(key, getDecimalKeyMap());    	
    }
		
    @Override
    public void addKey(IntegerKey<A, T, E> key) {
    	addAttributeKey(key, getIntegerKeyMap());
    }
    
    @Override
    public void addKey(CharKey<A, T, E> key) {        	
    	addAttributeKey(key, getCharKeyMap());    	
    }
    
    @Override
    public void addKey(VarcharKey<A, T, E> key) {        	
    	addAttributeKey(key, getVarcharKeyMap());    	
    }    

    @Override
    public void addKey(DateKey<A, T, E> key) {        	
    	addAttributeKey(key, getDateKeyMap());    	
    }

    @Override
    public void addKey(TimestampKey<A, T, E> key) {        	
    	addAttributeKey(key, getTimestampKeyMap());    	
    }

    @Override
    public void addKey(TimeKey<A, T, E> key) {        	
    	addAttributeKey(key, getTimeKeyMap());    	
    }

    @Override
    public void addKey(IntervalKey.YearMonth<A, T, E> key) {        	
    	addAttributeKey(key, getYearMonthIntervalKeyMap());    	
    }

    @Override
    public void addKey(IntervalKey.DayTime<A, T, E> key) {        	
    	addAttributeKey(key, getDayTimeIntervalKeyMap());    	
    }    

	private Map<A, PrimitiveKey<A, T, E, ?, ?, ?, ?>> getKeyMap() {
		if (keyMap == null) {
			keyMap = createKeyMap();
			
			if (keyMap == null) {
				throw new NullPointerException(getClass() + ".createKeyMap()");
			}
		}

		return keyMap;
	}
	
	protected Map<A, PrimitiveKey<A, T, E, ?, ?, ?, ?>> createKeyMap() {
		return new HashMap<A, PrimitiveKey<A, T, E, ?, ?, ?, ?>>();
	}

	protected <
		V extends Serializable, 
		P extends PrimitiveType<P>,
		PH extends PrimitiveHolder<V, P>,
		K extends PrimitiveKey<A, T, E, V, P, PH, K>
	>
	K key(K key, A name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
								
		return key.name().equals(name) ? key : null;
	}

//	private Map<Column, Key<A, ?, ?, ?, E, ?>> getColumnKeyMap() {
//		if (columnKeyMap == null) {
//			columnKeyMap = new HashMap<Column, Key<A,?,?,?,E,?>>();
//			
//			for (Key<A,?,?,?,E,?> k : getKeyMap().values()) {
//				add(k, columnKeyMap);			
//			}
//		}
//
//		return columnKeyMap;
//	}
	
//	@Override
//	public Key<A, ?, ?, ?, E, ?> getKey(Column column) {
//		if (column == null) {
//			throw new NullPointerException("column");
//		}
//		
//		return getColumnKeyMap().get(column);
//	}
	

	
//	private void add(Key<A, ?, ?, ?, E, ?> key, Map<Column, Key<A,?,?,?,E,?>> cm) {
//		cm.put(getColumn(key.name()), key);
//	}
	
	
	
	

	protected <
		V extends Serializable, 
		P extends PrimitiveType<P>,
		PH extends PrimitiveHolder<V, P>,
		K extends PrimitiveKey<A, T, E, V, P, PH, K>
	>
	K key(A name, Map<A, K> src) {
		if (name == null) {
			throw new NullPointerException("name");
		}
				
		return (src == null) ? null : src.get(name);
	}
	
	protected EntityIdentityMap<A, R, T, E> createIdentityMap() {
		return new DefaultIdentityMap();
	}

	@Override
	public EntityIdentityMap<A, R, T, E> getIdentityMap(final IdentityContext ctx) {
		if (ctx == null) {
			throw new NullPointerException("ctx");
		}
				
		final Map<IdentityContext, EntityIdentityMap<A, R, T, E>> icm = getIdentityContextMap();
		EntityIdentityMap<A, R, T, E> im = icm.get(ctx);
		
		if (im == null) {
			im = createIdentityMap();			
			icm.put(ctx, im);
			ctx.add(new ContextRegistration() {				
				@Override
				public void remove() {
					icm.remove(ctx);
				}
			});
		}
				
		return im;
	}
	
	
	private Map<IdentityContext, EntityIdentityMap<A, R, T, E>> getIdentityContextMap() {
		if (identityContextMap == null) {
			identityContextMap = new HashMap<IdentityContext, EntityIdentityMap<A,R,T,E>>();			
		}

		return identityContextMap;
	}

	
	private class DefaultIdentityMap
		implements EntityIdentityMap<A, R, T, E> {

		@Override
		public E get(E v) {
			return v;
		}
		
	}
	
	public E unify(IdentityContext ctx, E e) {
		EntityIdentityMap<A, R, T, E> im = getIdentityMap(ctx);
		return im.get(e);
	}
	
	@Override
	public void dispose(IdentityContext ctx) {
		if (ctx == null) {
			throw new NullPointerException("ctx");
		}
		
		getIdentityContextMap().remove(ctx);
	}

	
	
	
}