/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class DefaultMutableMetaObject {
	
	private String name;
			
	public DefaultMutableMetaObject() {
		super();
	}

	public DefaultMutableMetaObject(String n) {
		super();
		setName(n);
	}

	public String getName() {
		return name;
	}	

	public abstract DefaultMutableMetaObject getParent();
	
	public String getQualifiedName() {
		return getQualifiedName(null);
	}
	
	public String getQualifiedName(boolean quote) {
		return getQualifiedName(quote ? "\"" : null);
	}

	private String getQualifiedName(String quote) {	
		StringBuffer buf = new StringBuffer();
		
		for (Iterator<DefaultMutableMetaObject> pi = path().iterator(); pi.hasNext();) {
			DefaultMutableMetaObject m = pi.next();
			
			if (quote != null) {
				buf.append(quote);
			}
			
			buf.append(m.getName());
			
			if (quote != null) {
				buf.append(quote);
			}			
			
			if (pi.hasNext()) {
				buf.append(".");				
			}
		}
				
		return buf.toString();
	}

	protected List<DefaultMutableMetaObject> path() {
		LinkedList<DefaultMutableMetaObject> p = new LinkedList<DefaultMutableMetaObject>();
		
		for(DefaultMutableMetaObject e = this; e != null; e = e.getParent()) {
			p.addFirst(e);
		}
		
		return p;
	}

	public void setName(String newName) {					
		if (newName == null) {
			throw new NullPointerException();
		}
		
		this.name = newName;
	}

//	void setParent(MetaObject parent) {
//		this.parent = parent;
//	}
	
	
	
}
