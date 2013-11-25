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
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.types.DataTypeDefinition;

public class AlterTableAddColumn
	extends SQLSchemaStatement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3990587941677143525L;
	private SchemaElementName tableName;	
	private Identifier columnName;
	private DataTypeDefinition dataType;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AlterTableAddColumn() {
	}
		
	public AlterTableAddColumn(SchemaElementName tableName, Identifier columnName, DataTypeDefinition dataType) {
		super(Name.ALTER_TABLE);
		
		if (tableName == null) {
            throw new NullPointerException("'tableName' must not be null");
        }
		
		if (columnName == null) {
            throw new NullPointerException("'columnName' must not be null");
        }		
		
		if (dataType == null) {
            throw new NullPointerException("'dataType' must not be null");
        }
		
		this.tableName = tableName;
		this.columnName = columnName;
		this.dataType = dataType;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.ALTER.traverse(vc, v);		
		SQLKeyword.TABLE.traverse(vc, v);
		getTableName().traverse(vc, v);
		SQLKeyword.ADD.traverse(vc, v);
		SQLKeyword.COLUMN.traverse(vc, v);
		this.columnName.traverse(vc, v);
		this.dataType.traverse(vc, v);
	}

    public SchemaElementName getTableName() {
        return tableName;
    }

//    private ElementList<BaseTableElement> getElementList() {
//        if (elementList == null) {
//            elementList = new ElementList<BaseTableElement>();            
//        }
//
//        return elementList;
//    }    
}
