/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.mysql;


import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.meta.PrimaryKey;

public class MySQLAlterTableDropPrimaryKey
	extends AlterTableDropConstraint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3990587941677143525L;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected MySQLAlterTableDropPrimaryKey() {
	}
		
	public MySQLAlterTableDropPrimaryKey(PrimaryKey primaryKey) {
		super(primaryKey);		
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.ALTER.traverse(vc, v);		
		SQLKeyword.TABLE.traverse(vc, v);		
		getTable().traverse(vc, v);						
		SQLKeyword.DROP.traverse(vc, v);
		SQLKeyword.PRIMARY.traverse(vc, v);
		SQLKeyword.KEY.traverse(vc, v);
		getConstraint().getUnqualifiedName().traverse(vc, v);		
	}
}
