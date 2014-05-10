/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.types.SQLCharType;
import com.appspot.relaxe.expr.ddl.types.SQLIntType;
import com.appspot.relaxe.expr.ddl.types.SQLVarcharType;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.PrimaryKey;

public class CreateTable
	extends SQLSchemaStatement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5904813022152304570L;
	
	private SchemaElementName tableName;	
	private ElementList<BaseTableElement> elementList;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected CreateTable() {
	}
	
	public CreateTable(BaseTable table) {
		this(null, table, true, false);
	}
	
	public CreateTable(BaseTable table, boolean primaryKey) {
		this(null, table, true, primaryKey);
	}
	
	public CreateTable(DataTypeMap tm, BaseTable table, boolean relative, boolean primaryKey) {
		super(Name.CREATE_TABLE);
		
		if (table == null) {
			throw new NullPointerException("table");
		}
						
		List<BaseTableElement> elements = new ArrayList<BaseTableElement>();
		
		ColumnMap cm = table.getColumnMap();
		
		ElementList<ColumnConstraint> nnc = new ElementList<ColumnConstraint>(Collections.singleton(NotNull.NOT_NULL));
		Environment env = table.getEnvironment();
		
		if (tm == null) {			
			tm = env.getDataTypeMap();	
		}		
				
		for (Column col : cm.values()) {
			DataType t = col.getDataType();	
			ColumnDataType type = tm.getSQLType(t);
			
			if (type == null) {
				throw new RuntimeException(
						concat("Column ", table.getQualifiedName(), ".", col.getColumnName().getContent(), 
								": unsupported column type: ", t.getDataType(), " (", t.getTypeName(), ")"));  
			}
						
			ElementList<ColumnConstraint> cl = col.isDefinitelyNotNullable() ? nnc : null;			
			DefaultDefinition dd = env.newDefaultDefinition(col);								
									
			ColumnDefinition cd = new ColumnDefinition(col.getColumnName(), type, dd, cl);
			elements.add(cd);
		}
		
		if (primaryKey) {				
			PrimaryKey pk = table.getPrimaryKey();
			
			if (pk != null) {
				elements.add(new PrimaryKeyConstraint(pk));
			}
		}
				
		this.tableName = relative ? table.getName().withoutCatalog() : table.getName();		
		this.elementList = new ElementList<BaseTableElement>(elements);
	}
	
		
	private String concat(Object ... elems) {
		StringBuilder buf = new StringBuilder();
		
		for (Object e : elems) {
			buf.append(e);			
		}
		
		return buf.toString();
	}

	public CreateTable(SchemaElementName tableName, ElementList<BaseTableElement> elementList) {
		super(Name.CREATE_TABLE);
		
		if (tableName == null) {
            throw new NullPointerException("'tableName' must not be null");
        }
		
		if (elementList == null) {
			throw new NullPointerException("elementList");
		}
		
		this.tableName = tableName;
		this.elementList = elementList;
	}
	
	public static class Builder {
		
		private List<BaseTableElement> elementList;
		private SchemaElementName tableName;
				
		private List<BaseTableElement> getElementList() {
			if (elementList == null) {
				elementList = new ArrayList<BaseTableElement>();				
			}

			return elementList;
		}
		
		public Builder(SchemaElementName tableName) {
			super();			
			this.tableName = tableName;			
		}


		public void add(BaseTableElement element) {
	        getElementList().add(element);
	    }
		
		public SchemaElementName getTableName() {
			return tableName;
		}

		public void setTableName(SchemaElementName tableName) {
			this.tableName = tableName;
		}		

		public CreateTable newCreateTable() {
			ElementList<BaseTableElement> el = new ElementList<BaseTableElement>(getElementList());
			return new CreateTable(tableName, el);			
		}
		
		public void addInteger(Identifier name, boolean nullable) {			
            add(new ColumnDefinition(name, SQLIntType.get()));
        }
		
		public void add(Identifier name, ColumnDataType type, boolean nullable) {			
            add(new ColumnDefinition(name, type));
        }

		public void addVarchar(Identifier name, int charOctetSize, boolean nullable) {			
            add(name, SQLVarcharType.get(charOctetSize), nullable);
        }
		
		public void addChar(Identifier name, int charOctetSize, boolean nullable) {			
            add(name, SQLCharType.get(charOctetSize), nullable);
        }
	}
	
	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.CREATE.traverse(vc, v);		
		SQLKeyword.TABLE.traverse(vc, v);
		getTableName().traverse(vc, v);		
		Symbol.PAREN_LEFT.traverse(vc, v);
		getElementList().traverseContent(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
	}

    public SchemaElementName getTableName() {
        return tableName;
    }

    private ElementList<BaseTableElement> getElementList() {
        return elementList;
    }    
}
