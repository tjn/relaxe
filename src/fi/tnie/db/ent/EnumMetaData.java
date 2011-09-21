/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.EnumMap;
import java.util.EnumSet;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class EnumMetaData<
	A extends Enum<A> & Attribute,
	R extends Enum<R> & Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,	
	F extends EntityFactory<E, H, M, F>,	
	M extends EntityMetaData<A, R, T, E, H, F, M>	
> 
	extends DefaultEntityMetaData<A, R, T, E, H, F, M> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8574938084185912154L;
	
	private String attributeTypeName;
	private String referenceTypeName;
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
		this.attributeTypeName = attributeType.getName();
		this.referenceTypeName = referenceType.getName();
	}

	@Override
	protected void populateAttributes(BaseTable table) {		
		EnumSet<A> as = EnumSet.allOf(attributeType);
		EnumMap<A, Column> am = new EnumMap<A, Column>(attributeType);		
		populateAttributes(as, am, table);
	}

	@Override
	protected void populateReferences(BaseTable table) {
		EnumSet<R> rs = EnumSet.allOf(referenceType);
		EnumMap<R, ForeignKey> rm = new EnumMap<R, ForeignKey>(referenceType);		
		populateReferences(rs, rm, table);		
	}
		
	public Class<A> getAttributeNameType() {
		return attributeType;
	}	
	
	public Class<R> getReferenceNameType() {	
		return this.referenceType;
	}
	
}
