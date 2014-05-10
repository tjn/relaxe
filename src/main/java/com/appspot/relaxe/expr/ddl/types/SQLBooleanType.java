package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class SQLBooleanType
    extends SQLDataType {
       	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	
	private static final SQLBooleanType TYPE = new SQLBooleanType();
	
	public static SQLBooleanType get() {
		return SQLBooleanType.TYPE;
	}	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private SQLBooleanType() {
	}
		
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.BIT.traverse(vc, v);
	}		

}