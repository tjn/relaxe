/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.meta.Column;
import fi.tnie.db.rpc.Holder;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityDiff<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,	
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
>
	implements EntityDiff<A, R, Q, T, E>
{	
	private E original;
	private E modified;
	
//	private static Logger logger = Logger.getLogger(AbstractEntityDiff.class);
	
	protected AbstractEntityDiff(E original, E modified) {
		super();
		this.original = original;
		this.modified = modified;		
	}

	public E getOriginal() {
		return original;
	}

	public E getModified() {
		return modified;
	}
	
	@Override
	public Change change() {		
		if (original == null && modified == null) {
			return null;			
		}	
		
		if (original == null) {
			return Change.ADDITION;
		}
		
		if (modified == null) {
			return Change.DELETION;
		}
		
		if (!attributes().isEmpty()) {
			return Change.MODIFICATION;
		}
		
		if (!references().isEmpty()) {
			return Change.MODIFICATION;
		}
				
		return null;
	}
	
	/**
	 * TODO: Make definition clearer. 
	 * 
	 * @param original
	 * @param modified
	 * @return
	 */
	
	protected Map<A, Change> attributes(E original, E modified) {
		EntityMetaData<A, R, Q, T, ? extends E> meta = original.getMetaData();
		EnumMap<A, Change> cm = new EnumMap<A, Change>(meta.getAttributeNameType());
		
		for (A a : meta.attributes()) {
			Holder<?, ?> o = original.get(a);			
			Holder<?, ?> m = modified.get(a);
			
			if ((o == null && m == null) || (o == m)) {
				continue;
			}
			
			if (o == null || o.isNull()) {
				cm.put(a, Change.ADDITION);				
				continue;
			}
			
			if (m == null || m.isNull()) {
				cm.put(a, Change.DELETION);
				continue;
			}						
			
			try {
				if (!o.equals(m)) {
					cm.put(a, Change.MODIFICATION);					
				}				
			}
			catch (ClassCastException e) {
				cm.put(a, Change.MODIFICATION);
				// TODO: should't we throw an exception
//				logger().info(e.getMessage());				
			}
		}
		
		return cm;		
	}
	
	protected Map<R, Change> references(E original, E modified) {
		EntityMetaData<A, R, Q, T, ? extends E> meta = original.getMetaData();
		EnumMap<R, Change> cm = new EnumMap<R, Change>(meta.getRelationshipNameType());
		
		for (R r : meta.relationships()) {
			Entity<?,?,?,?,?> o = original.get(r);			
			Entity<?,?,?,?,?> m = modified.get(r);
			
			if ((o == null && m == null) || (o == m)) {
				continue;
			}
			
			if (o == null) {
				cm.put(r, Change.ADDITION);
				continue;
			}
			
			if (m == null) {
				cm.put(r, Change.DELETION);
				continue;
			}
			
			if (o != m && primaryKeyDiffers(o, m)) {								
				cm.put(r, Change.MODIFICATION);
				continue;
			}			
		}
		
		return cm;		
	}
	
	private boolean primaryKeyDiffers(Entity<?,?,?,?,?> o, Entity<?,?,?,?,?> m) {
		Map<Column, Holder<?,?>> a = o.getPrimaryKey();
		Map<Column, Holder<?,?>> b = m.getPrimaryKey();
		
		if (a == null || b == null) {
			return a != b;
		}		
		
		return (!a.equals(b));
	}

//	public static Logger logger() {
//		return AbstractEntityDiff.logger;
//	}			
}
