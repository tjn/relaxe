package fi.tnie.db;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.AbstractTableReference;

public class QueryContext {

	private int count = 0;	
	private Map<AbstractTableReference, String> nameMap;	
	private Set<String> names;
	
	private static Logger logger = Logger.getLogger(QueryContext.class);
	
	public QueryContext() {
		super();
	}
	
	private String unique(String n) {
		if (n == null) {
			n = "r";
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
	
	public String name(AbstractTableReference tref, String prefix) {
		if (tref == null) {
			throw new NullPointerException("'tref' must not be null");
		}
		
		String n = getNameMap().get(tref);
		
		if (n == null) {
			n = unique(prefix);
			getNames().add(n);
			getNameMap().put(tref, n);			
			logger().debug("added name for: " + tref + ": " + n);
			logger().debug("all names: " + getNames());
			logger().debug("name-map: " + getNameMap());
			
			
		}
		else {
			logger().debug("name for: " + tref + ": " + n);
		}
		
		return n;
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
	
	private Map<AbstractTableReference, String> getNameMap() {
		if (nameMap == null) {
			nameMap = new LinkedHashMap<AbstractTableReference, String>();			
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
		return QueryContext.logger;
	}
}
