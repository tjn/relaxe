/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.map;

/**
 * @author Administrator
 *
 */
public class JavaType {
	private String packageName; 
	private String unqualifiedName;
	private String qualifiedName;
			
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