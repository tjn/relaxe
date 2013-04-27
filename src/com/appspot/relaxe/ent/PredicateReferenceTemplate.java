/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;

/**
 *  
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public abstract class PredicateReferenceTemplate<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	RA extends Attribute,
	RR extends Reference,	
	RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
	RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
	RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
	RF extends EntityFactory<RE, RH, RM, RF, RC>,
	RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
	RC extends Content,
	K extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, K>
>
	implements EntityQueryPredicate<A> {
	
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4009167582842494268L;
	private R reference;
	
	protected PredicateReferenceTemplate() {
	}
		
	public PredicateReferenceTemplate(R reference) {
		super();
		this.reference = reference;
	}


//	public static class Null<A extends Attribute>
//		extends PredicateReferenceTemplate<A> {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 5183112198249471108L;
//
//		/**
//		 * No-argument constructor for GWT Serialization
//		 */
//		@SuppressWarnings("unused")
//		private Null() {	
//		}
//		
//		public Null(A attribute) {
//			super(attribute);			
//		}
//				
//		@Override
//		public Predicate predicate(ColumnReference cr) {
//			return new com.appspot.relaxe.expr.op.IsNull(cr);
//		}		
//	}

		

		@Override
	public abstract Predicate predicate(TableReference tref, ColumnReference cr);
	
	
	@Override
	public A attribute() {
		return null;
//		return this.attribute;
	}
	
//	public static 
//	<A extends Attribute>
//	PredicateReferenceTemplate<A> eq(A attribute, Integer value) {
//		if (value == null) {
//			return new Null<A>(attribute);
//		}
//		else {
//			return new Equals<A>(attribute, new IntLiteral(value.intValue()));
//		}		
//	}
}
