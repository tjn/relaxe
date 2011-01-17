/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;



import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
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
	A,
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
	protected Map<A, PrimitiveHolder<?, ?>> values() {
		if (values == null) {
			values = new HashMap<A, PrimitiveHolder<?, ?>>();			
		}

		return values;
	}
	
	@Override
	protected Map<R, ReferenceHolder<?, ?, ?, ?>> references() {
		if (refs == null) {
			refs = new HashMap<R, ReferenceHolder<?,?,?,?>>();
		}

		return refs;
	}
    

//	@Override
//	public PrimitiveHolder<?, ?> get(String attribute) {
//		String cn = attribute;
//		EntityMetaData<A, R, T, E> meta = getMetaData();
//		Column c = meta.getBaseTable().columnMap().get(cn);
//		
//		if (c == null) {
//			throw new NullPointerException("no such column: " + cn); 			
//		}
//		
//		PrimitiveHolder<?, ?> v = get(c);
//		return v;
//	}
	    
}
