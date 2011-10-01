/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;


import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.VisitContext;

public class AlterTableAddColumn
	extends Statement {
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
