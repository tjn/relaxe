/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.types.ReferenceType;

public abstract class DefaultEntityMetaData<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable, 
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, E>
>
	extends AbstractEntityMetaData<A, R, Q, T, E>
{	
	private Class<A> attributeType;
	private Class<R> referenceType;
	private Class<Q> queryType;
	private BaseTable baseTable;
	
	private EnumSet<A> attributes;
	private EnumMap<A, Column> attributeMap;
	private Map<Column, A> columnMap;
//	private Set<A> pkattrs;
	private Set<Column> pkcols;
	
	private EnumSet<R> references;	
	private EnumMap<R, ForeignKey> referenceMap;
	private Map<Column, Set<R>> columnReferenceMap;
	
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
		
//		EnumSet<A> pka = EnumSet.noneOf(atype);
		
		Set<Column> pkc = new HashSet<Column>();
		
		for (A a : this.attributes) {
			Column c = table.columnMap().get(a.identifier());
			
			if (c == null) {
				throw new NullPointerException(
						"no column for attribute: " + a.identifier() + " in " + 
						table.columns());
			}
			
			this.attributeMap.put(a, c);
			this.columnMap.put(c, a);
			
			if (c.isPrimaryKeyColumn()) {
				pkc.add(c);
//				pka.add(a);
			}
		}
		
		this.pkcols = Collections.unmodifiableSet(pkc);						
//		this.pkattrs = Collections.unmodifiableSet(pka);
	}
	
	private void populateReferences(Class<R> rtype, BaseTable table) {
		this.references = EnumSet.allOf(rtype);				
		this.referenceMap = new EnumMap<R, ForeignKey>(rtype);
		
		Map<Column, Set<R>> rm = new HashMap<Column, Set<R>>();
		
		for (R r : this.references) {			
			ForeignKey fk = table.foreignKeys().get(r.identifier());
			
			if (fk == null) {
				throw new NullPointerException("no such foreign key: " + r.identifier());
			}
			else {
				this.referenceMap.put(r, fk);				
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
	}

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
	public Set<Column> getPKDefinition() {
		return this.pkcols;
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
	
	@Override
	public Set<R> getReferences(Column c) {
		return this.columnReferenceMap.get(c);
	}
	
	@Override
	public Catalog getCatalog() {
	    BaseTable table = getBaseTable();	    
	    return (table == null) ? null : table.getSchema().getCatalog();
	}
}
