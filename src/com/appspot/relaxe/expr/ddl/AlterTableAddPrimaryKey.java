/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;


import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.PrimaryKey;

public class AlterTableAddPrimaryKey
	extends Statement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3990587941677143525L;
	private PrimaryKey primaryKey;	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AlterTableAddPrimaryKey() {
	}
		
	public AlterTableAddPrimaryKey(PrimaryKey primaryKey) {
		super(Name.ALTER_TABLE);
		
		if (primaryKey == null) {
			throw new NullPointerException("primaryKey");
		}
		
		this.primaryKey = primaryKey;
		
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		PrimaryKey pk = this.primaryKey;
		
		SQLKeyword.ALTER.traverse(vc, v);		
		SQLKeyword.TABLE.traverse(vc, v);
		
		pk.getTable().getName().traverse(vc, v);
		
		ColumnMap cm = pk.getColumnMap();
				
		SQLKeyword.ADD.traverse(vc, v);
		
		Identifier cn = pk.getUnqualifiedName();
		
		if (cn != null) {
			SQLKeyword.CONSTRAINT.traverse(vc, v);
			cn.traverse(vc, v);	
		}		
		
		SQLKeyword.PRIMARY.traverse(vc, v);
		SQLKeyword.KEY.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		
		int cc = cm.size();
				
		for (int i = 0; i < cc; i++) {
			Column col = cm.get(i);
			col.getColumnName().traverse(vc, v);
			
			if ((i + 1) < cc) {
				Symbol.COMMA.traverse(vc, v);
			}
		}		
		
		Symbol.PAREN_RIGHT.traverse(vc, v);		
	}
}
