/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class EnumMetaData<
	A extends Enum<A> & Attribute,
	R extends Enum<R> & Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,	
	F extends EntityFactory<E, H, M, F, C>,	
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
> 
	extends DefaultEntityMetaData<A, R, T, E, H, F, M, C> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8574938084185912154L;
	
	private transient Class<A> attributeType;
	private transient Class<R> referenceType;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected EnumMetaData() {
	}
		
	public EnumMetaData(Class<A> attributeType, Class<R> referenceType) {
		super();
		this.attributeType = attributeType;
		this.referenceType = referenceType;
	}
	
	@Override
	protected Set<Column> populate(BaseTable table) {
		Set<Column> pkc = new HashSet<Column>();
		populateAttributes(table, pkc);
		populateReferences(table, pkc);
		return pkc;
	}
	
	protected void populateAttributes(BaseTable table, Set<Column> pkc) {		
		EnumSet<A> as = EnumSet.allOf(attributeType);
		EnumMap<A, Column> am = new EnumMap<A, Column>(attributeType);		
		populateAttributes(as, am, table, pkc);		
	}
	
	protected void populateReferences(BaseTable table, Set<Column> pkc) {
		EnumSet<R> rs = EnumSet.allOf(referenceType);
		EnumMap<R, ForeignKey> rm = new EnumMap<R, ForeignKey>(referenceType);		
		populateReferences(rs, rm, table, pkc);		
	}
		
	public Class<A> getAttributeNameType() {
		return attributeType;
	}	
	
	public Class<R> getReferenceNameType() {	
		return this.referenceType;
	}
}
