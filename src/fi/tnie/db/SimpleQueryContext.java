/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.NonJoinedTable;
import fi.tnie.db.expr.OrdinaryIdentifier;
import fi.tnie.db.expr.ValueElement;

public class SimpleQueryContext implements QueryContext {

	private int count = 0;	
	private Set<String> names;
	
	private Map<NonJoinedTable, OrdinaryIdentifier> nameMap;		
	private Map<ValueElement, Identifier> columnNameMap;	
	private Map<NonJoinedTable, String> correlationNamePrefixMap;
	
	
	private static Logger logger = Logger.getLogger(SimpleQueryContext.class);
	
	public SimpleQueryContext() {
		this(null);
	}
	
	public SimpleQueryContext(Map<NonJoinedTable, String> correlationNamePrefixMap) {
		super();
		this.correlationNamePrefixMap = correlationNamePrefixMap;
	}
	
//	private String uniqueTableRef(String n) {
//		return unique((n == null) ? "R" : n);
//	}
	
	private String tableRefPrefix(String p) {
		return (p == null) ? "R" : p;
	}
	
	private String unique(String n) {		
		StringBuffer nb = null;
		Set<String> nm = getNames();
		
		while(nm.contains(n)) {
			count++;
			
			if (nb == null) {
				nb = new StringBuffer(n);
			}
			else {
				nb.setLength(0);
				nb.append(n);
			}
			
			nb.append(count);
			n = nb.toString();
		}
		
		return n;
	}
	
	/* (non-Javadoc)
	 * @see fi.tnie.db.QueryContext#correlationName(fi.tnie.db.expr.AbstractTableReference)
	 */
	public OrdinaryIdentifier correlationName(NonJoinedTable tref) {
		if (tref == null) {
			throw new NullPointerException("'tref' must not be null");
		}
		
		OrdinaryIdentifier cn = getNameMap().get(tref);
		
		if (cn == null) {
			cn = register(tableRefPrefix(getPrefix(tref)));
			getNameMap().put(tref, cn);											
//			logger().debug("added symbol for: " + tref + ": " + n);
//			logger().debug("all names: " + getNames());
//			logger().debug("symbol-map: " + getNameMap());
		}
		else {
			logger().debug("symbol for: " + tref + ": " + cn);
		}
		
		return cn;
	}
	
	@Override	
	public Identifier generateColumnName(ValueElement	e) {
		if (e == null) {
			throw new NullPointerException("'e' must not be null");
		}
		
		Identifier cn = getColumnNameMap().get(e);
		
		if (cn == null) {			
			cn = register("C");
			getColumnNameMap().put(e, cn);
			
//			cn = new OrdinaryIdentifier(n);			
//			getNames().add(n);
//			getColumnNameMap().put(e, cn);			
//			logger().debug("added symbol for: " + tref + ": " + n);
//			logger().debug("all names: " + getNames());
//			logger().debug("symbol-map: " + getNameMap());
		}
		else {
			logger().debug("column name for: " + e + ": " + cn);
		}
		
		return cn;
	}	
	
	
	private Map<NonJoinedTable, OrdinaryIdentifier> getNameMap() {
		if (nameMap == null) {
			nameMap = new LinkedHashMap<NonJoinedTable, OrdinaryIdentifier>();			
		}
		
		return nameMap;
	}

	private Map<ValueElement, Identifier> getColumnNameMap() {
		if (columnNameMap == null) {
			columnNameMap = new LinkedHashMap<ValueElement, Identifier>();			
		}
		
		return columnNameMap;
	}
	
	private Set<String> getNames() {
		if (names == null) {
			names = new HashSet<String>();			
		}

		return names;
	}

	public static Logger logger() {
		return SimpleQueryContext.logger;
	}

	public Map<NonJoinedTable, String> getCorrelationNamePrefixMap() {
		if (correlationNamePrefixMap == null) {
			correlationNamePrefixMap = new HashMap<NonJoinedTable, String>();			
		}

		return correlationNamePrefixMap;
	}
	
	public String getPrefix(NonJoinedTable tref) {
		Map<NonJoinedTable, String> pm = this.correlationNamePrefixMap;		
		return (pm == null) ? null : pm.get(tref);
	}
	
	private OrdinaryIdentifier register(String prefix) {
		String n = unique(prefix);
		OrdinaryIdentifier cn = new OrdinaryIdentifier(n);		
		getNames().add(n);
		return cn;
	}	
}
