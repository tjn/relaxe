/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.Symbol;
import com.appspot.relaxe.expr.VisitContext;

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
		
	public CreateTable(SchemaElementName tableName) {
		super(Name.CREATE_TABLE);
		
		if (tableName == null) {
            throw new NullPointerException("'tableName' must not be null");
        }
		
		this.tableName = tableName;		
	}
	
	public void add(BaseTableElement element) {
        getElementList().getContent().add(element);
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
        if (elementList == null) {
            elementList = new ElementList<BaseTableElement>();            
        }

        return elementList;
    }    
}
