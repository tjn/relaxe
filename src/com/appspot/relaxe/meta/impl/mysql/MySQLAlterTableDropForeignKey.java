/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.mysql;


import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.meta.ForeignKey;

public class MySQLAlterTableDropForeignKey
	extends AlterTableDropConstraint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3990587941677143525L;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected MySQLAlterTableDropForeignKey() {
	}
		
	public MySQLAlterTableDropForeignKey(ForeignKey foreignKey) {
		super(foreignKey);		
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		SchemaElementName cn = getConstraint();		
		SQLKeyword.ALTER.traverse(vc, v);		
		SQLKeyword.TABLE.traverse(vc, v);		
		getTable().traverse(vc, v);						
		SQLKeyword.DROP.traverse(vc, v);
		SQLKeyword.FOREIGN.traverse(vc, v);
		SQLKeyword.KEY.traverse(vc, v);
		cn.getUnqualifiedName().traverse(vc, v);		
	}
}
