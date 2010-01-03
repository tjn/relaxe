/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.AbstractTableReference;
import fi.tnie.db.expr.OrdinaryIdentifier;

public class SimpleQueryContext implements QueryContext {

	private int count = 0;	
	private Map<AbstractTableReference, OrdinaryIdentifier> nameMap;
	private Set<String> names;
	
	private static Logger logger = Logger.getLogger(SimpleQueryContext.class);
	
	public SimpleQueryContext() {
		super();
	}
	
	private String unique(String n) {
		if (n == null) {
			n = "R";
		}
		
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
	public OrdinaryIdentifier correlationName(AbstractTableReference tref) {
		if (tref == null) {
			throw new NullPointerException("'tref' must not be null");
		}
		
		OrdinaryIdentifier cn = getNameMap().get(tref);
		
		if (cn == null) {
			String prefix = null; // TODO: derive from table name:
			String n = unique(prefix);
			cn = new OrdinaryIdentifier(n);			
			getNames().add(n);
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
	
//	public String start(AbstractTableReference tref) {
//		return start(tref, tref.getCorrelationNamePrefix());
//	}
	
//	public String start(AbstractTableReference tref, String cn) {
//		if (tref == null) {
//			throw new NullPointerException("'tref' must not be null");
//		}
//		
//		if (cn == null) {
//			cn = "r";
//		}
//		
//		String k = unique(cn);
////		getNameMap().put(k, tref);		
////		getNameMap().put(tref, k);
//		add(tref, k);
//		
//		return k;		
//	}
	
//	private void add(AbstractTableReference tref, String cn) {
//		getNameMap().put(tref, cn);
//		getNames().add(cn);
//	}
//	
//	private String remove(AbstractTableReference tref) {
//		String cn = getNameMap().remove(tref);
//		getNames().remove(cn);
//		return cn;
//	}	
	
//	public void end(AbstractTableReference tref) {
//		
////		AbstractTableReference removed = getNameMap().remove(tref);
////		
////		if (removed == null) {
////			throw new IllegalStateException("popping table " + tref + " which does not exist");
////		}
//		
//		String removed = remove(tref);
//		
//		if (removed == null) {
//			throw new IllegalStateException("popping table-ref " + tref + " which does not exist");
//		}
//	}

//	private Map<String, AbstractTableReference> getNameMap() {
//		if (refNameMap == null) {
//			refNameMap = new LinkedHashMap<String, AbstractTableReference>();			
//		}
//		
//		return refNameMap;
//	}
	
	private Map<AbstractTableReference, OrdinaryIdentifier> getNameMap() {
		if (nameMap == null) {
			nameMap = new LinkedHashMap<AbstractTableReference, OrdinaryIdentifier>();			
		}
		
		return nameMap;
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
}
