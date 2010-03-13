/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class JavaType {
	private String packageName; 
	private String unqualifiedName;
	private String qualifiedName;
			
	public JavaType(Class<?> clazz) {	    	    
	    String pkg = clazz.getPackage().getName();
	    String name = clazz.getName();
	    name = name.substring(pkg.length() + 1);
	    setName(pkg, name);
	}
			
	public JavaType(String packageName, String unqualifiedName) {
		super();
		setName(packageName, unqualifiedName);
	}

	protected JavaType() {	    
	}
	
    protected void setName(String packageName, String unqualifiedName) {
        this.packageName = packageName;									
		this.unqualifiedName = unqualifiedName;
		
		if (this.packageName == null) {
			this.qualifiedName = this.unqualifiedName;
		}
		else {
			StringBuffer buf = new StringBuffer(this.packageName);
			buf.append(".");
			buf.append(unqualifiedName);
			this.qualifiedName = buf.toString();
		}
    }
	
	public String getPackageName() {
		return this.packageName;
	}
	
	public String getUnqualifiedName() {			
		return this.unqualifiedName;
	}	
	
	public String getQualifiedName() {
		return this.qualifiedName;
	}
}