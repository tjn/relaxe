/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import fi.tnie.db.ent.JavaType;

/**
 * @author Administrator
 *
 */
public class GeneratedJavaType
	extends JavaType {
		
	public GeneratedJavaType(Class<?> clazz) {	    	    
	    String pkg = clazz.getPackage().getName();
	    String name = clazz.getName();
	    name = name.substring(pkg.length() + 1);
	    setName(pkg, name);
	}
			
	public GeneratedJavaType(String packageName, String unqualifiedName) {
		super();
		setName(packageName, unqualifiedName);
	}

	protected GeneratedJavaType() {	    
	}	
}