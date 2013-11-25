/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.expr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//import org.slf4j.Logger;


public class SimpleQueryContext implements QueryContext {

	private int count = 0;	
	private Set<String> names;
	
	private Map<NonJoinedTable, OrdinaryIdentifier> nameMap;		
	private Map<ValueElement, Identifier> columnNameMap;	
	private Map<NonJoinedTable, String> correlationNamePrefixMap;
	
	
//	private static Logger logger = LoggerFactory.getLogger(SimpleQueryContext.class);
	
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
	 * @see com.appspot.relaxe.QueryContext#correlationName(com.appspot.relaxe.expr.AbstractTableReference)
	 */
	@Override
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
//			logger().debug("symbol for: " + tref + ": " + cn);
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
//			logger().debug("column name for: " + e + ": " + cn);
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

//	public static Logger logger() {
//		return SimpleQueryContext.logger;
//	}

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
