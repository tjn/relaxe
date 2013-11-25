/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
import com.appspot.relaxe.meta.PrimaryKey;

public class AlterTableAddPrimaryKey
	extends SQLSchemaStatement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3990587941677143525L;
	private PrimaryKey primaryKey;
	private SchemaElementName tableName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AlterTableAddPrimaryKey() {
	}
		
	public AlterTableAddPrimaryKey(PrimaryKey primaryKey) {
		this(primaryKey, true);		
	}
	
	public AlterTableAddPrimaryKey(PrimaryKey primaryKey, boolean relative) {
		super(Name.ALTER_TABLE);
		
		if (primaryKey == null) {
			throw new NullPointerException("primaryKey");
		}
		
		SchemaElementName sen = primaryKey.getTable().getName();				
		this.tableName = relative ? sen.withoutCatalog() : sen;
		this.primaryKey = primaryKey;
		
	}
	 
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {		
		PrimaryKey pk = this.primaryKey;
		
		SQLKeyword.ALTER.traverse(vc, v);		
		SQLKeyword.TABLE.traverse(vc, v);
		
		this.tableName.traverse(vc, v);
		
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
