/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.VisitContext;

public class CreateTable
	extends Statement {
	
	private SchemaElementName tableName;	
	private ElementList<BaseTableElement> elementList;
		
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
		Keyword.CREATE.traverse(vc, v);		
		Keyword.TABLE.traverse(vc, v);
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
