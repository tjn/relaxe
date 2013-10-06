/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.IdentifierRules;
import com.appspot.relaxe.types.PrimitiveType;

public class CreateTable
	extends Statement {
	
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
		super(Name.CREATE_TABLE);
		
		if (table == null) {
			throw new NullPointerException("table");
		}
		
		List<BaseTableElement> elements = new ArrayList<BaseTableElement>();
		
		
		ColumnMap cm = table.columnMap();
				
		for (Column col : cm.values()) {
			DataType data = col.getDataType();			
			SQLType type = getSQLType(data);
			
			if (type == null) {
				throw new RuntimeException("unsupported column type: " + data.getDataType() + " (" + data.getTypeName() + ")"); 
			}
			
			boolean nn = col.isDefinitelyNotNullable();
						
			ColumnDefinition cd = new ColumnDefinition(col.getColumnName(), type);
			elements.add(cd);
		}
		
		this.tableName = table.getName();
		this.elementList = new ElementList<BaseTableElement>(elements);
	}

	protected SQLType getSQLType(DataType data) {
		int t = data.getDataType();
		
		switch (t) {
		case PrimitiveType.INTEGER:
			return Int.INT;
		case PrimitiveType.BIGINT:
			return Int.BIG_INT;							
		case PrimitiveType.SMALLINT:
			return Int.SMALL_INT;							
		case PrimitiveType.TINYINT:
			return Int.TINY_INT;
		case PrimitiveType.VARCHAR:
		case PrimitiveType.LONGVARCHAR:
			return Varchar.get(data.getCharOctetLength());
		case PrimitiveType.CHAR:			
			return Char.get(data.getCharOctetLength());		
		default:
			break;
		}
		
		return null;
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
		private IdentifierRules identifierRules;
		
		private List<BaseTableElement> getElementList() {
			if (elementList == null) {
				elementList = new ArrayList<BaseTableElement>();				
			}

			return elementList;
		}
		
		public Builder(IdentifierRules identifierRules, SchemaElementName tableName) {
			super();
			this.identifierRules = identifierRules;
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
            add(new ColumnDefinition(name, new Int()));
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
