/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public abstract class DefaultEntityMetaData<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable, 
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, E>
>
	extends AbstractEntityMetaData<A, R, Q, E>
{	
	private Class<A> attributeType;
	private Class<R> referenceType;
	private Class<Q> queryType;
	private BaseTable baseTable;
	
	private EnumSet<A> attributes;
	private EnumMap<A, Column> attributeMap;
	private Map<Column, A> columnMap;
	private EnumMap<A, Column> pkdef;
	private Set<A> pkattrs;
	
	private EnumSet<R> references;	
	private EnumMap<R, ForeignKey> referenceMap;
	
	protected DefaultEntityMetaData(Class<A> atype, Class<R> rtype, Class<Q> qtype) {
		this.attributeType = atype;
		this.referenceType = rtype;
		this.queryType = qtype;
	}
	
	@Override
	public void bind(BaseTable table) {
		this.baseTable = table;
		populateAttributes(this.attributeType, table);
		populateReferences(this.referenceType, table);
	}
	

	
	private void populateAttributes(Class<A> atype, BaseTable table) {
		this.attributes = EnumSet.allOf(atype);		
		this.attributeMap = new EnumMap<A, Column>(atype);		
		this.columnMap = new HashMap<Column, A>();		
		this.pkdef = new EnumMap<A, Column>(atype);
		
		EnumSet<A> pka = EnumSet.noneOf(atype);
		
		for (A a : this.attributes) {			
			Column c = table.columnMap().get(a.identifier());
			
			this.attributeMap.put(a, c);
			this.columnMap.put(c, a);
			
			if (c.isPrimaryKeyColumn()) {
				this.pkdef.put(a, c);
				pka.add(a);
			}
		}
						
		this.pkattrs = Collections.unmodifiableSet(pka);
	}
	
	private void populateReferences(Class<R> rtype, BaseTable table) {
		this.references = EnumSet.allOf(rtype);				
		this.referenceMap = new EnumMap<R, ForeignKey>(rtype);
		
		for (R r : this.references) {			
			ForeignKey fk = table.foreignKeys().get(r.identifier());
			
			if (fk == null) {
				throw new NullPointerException("no such foreign key: " + r.identifier());
			}
			else {
				this.referenceMap.put(r, fk);
			}
		}		
	}
	
	public Set<A> attributes() {
		return Collections.unmodifiableSet(this.attributes);
	}
	
	public Set<R> relationships() {
		return Collections.unmodifiableSet(this.references);
	}

	@Override
	public Class<A> getAttributeNameType() {
		return this.attributeType;
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
	public Set<A> getPKDefinition() {
//		we can return this.pkattrs as is, because it is 
//		guaranteed to be unmodifiable here		 
		return this.pkattrs;
	}

	@Override
	public Class<Q> getQueryNameType() {
		return this.queryType;
	}

	@Override
	public Class<R> getRelationshipNameType() {	
		return this.referenceType;
	}

	@Override
	public ForeignKey getForeignKey(R r) {
		return this.referenceMap.get(r);
	}
	
}
