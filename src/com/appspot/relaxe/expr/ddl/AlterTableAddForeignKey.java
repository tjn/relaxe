/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;


import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;

public class AlterTableAddForeignKey
	extends SQLSchemaStatement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3990587941677143525L;
	private ForeignKey foreignKey;
	
	private SchemaElementName referencing;
	private SchemaElementName referenced;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AlterTableAddForeignKey() {
	}
	
	public AlterTableAddForeignKey(ForeignKey foreignKey) {
		this(foreignKey, true);
	}
		
	public AlterTableAddForeignKey(ForeignKey foreignKey, boolean relative) {
		super(Name.ALTER_TABLE);
		
		if (foreignKey == null) {
			throw new NullPointerException("foreignKey");
		}
		
		this.referencing = foreignKey.getReferencing().getName(relative);
		this.referenced = foreignKey.getReferenced().getName(relative);		
		this.foreignKey = foreignKey;		
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		
		ForeignKey fk = this.foreignKey;
		
		SQLKeyword.ALTER.traverse(vc, v);		
		SQLKeyword.TABLE.traverse(vc, v);
		
		this.referencing.traverse(vc, v);
		
		ColumnMap cm = fk.getColumnMap();
				
		SQLKeyword.ADD.traverse(vc, v);
		SQLKeyword.CONSTRAINT.traverse(vc, v);
		fk.getUnqualifiedName().traverse(vc, v);
		SQLKeyword.FOREIGN.traverse(vc, v);
		SQLKeyword.KEY.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		
		int cc = cm.size();
		Identifier[] rca = new Identifier[cc];
		
		for (int i = 0; i < cc; i++) {
			Column col = cm.get(i);
			rca[i] = fk.getReferenced(col).getUnqualifiedName();
			
			col.getColumnName().traverse(vc, v);
			
			if ((i + 1) < cc) {
				Symbol.COMMA.traverse(vc, v);
			}
		}		
		
		Symbol.PAREN_RIGHT.traverse(vc, v);
		SQLKeyword.REFERENCES.traverse(vc, v);
		this.referenced.traverse(vc, v);
		Symbol.PAREN_LEFT.traverse(vc, v);
		
		for (int i = 0; i < rca.length; i++) {
			Identifier col = rca[i];
			col.traverse(vc, v);
			
			if ((i + 1) < cc) {
				Symbol.COMMA.traverse(vc, v);
			}
		}		
		
		Symbol.PAREN_RIGHT.traverse(vc, v);		
	}
}
