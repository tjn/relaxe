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
	A,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AbstractEntityMetaData<A, R, T, E>
{	
	private BaseTable baseTable;
	
	private Set<A> attributes;
	private Map<A, Column> attributeMap;
	private Map<Column, A> columnMap;
	private Set<Column> pkcols;
	
	private Set<R> relationships;	
	private Map<R, ForeignKey> referenceMap;
	private Map<Column, Set<R>> columnReferenceMap;
	
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
}
