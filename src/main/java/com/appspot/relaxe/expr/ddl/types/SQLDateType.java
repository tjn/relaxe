package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public final class SQLDateType
	extends SQLDataType {
   	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	
	private static final SQLDateType TYPE = new SQLDateType(); 
	
	public static SQLDateType get() {
		return SQLDateType.TYPE;
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private SQLDateType() {
	}
		
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.DATE.traverse(vc, v);		
	}    
}