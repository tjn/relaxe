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
import com.appspot.relaxe.meta.IdentifierRules;

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
