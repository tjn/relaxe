/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.meta.BaseTable;

public class PersonTable
	extends DefaultRowFactory<PersonTable.Column, MutableRow> {
	
	public enum Column {
		NAME,
		DATE_OF_BIRTH	
	}

//	protected PersonTable(Catalog catalog) {
//		this(catalog.schemas().get("public"), PersonTable.Column.class, Person.class);		
//	}

	private PersonTable(BaseTable table, Class<Column> columnNameType,
			Class<MutableRow> productType) {
		super(table, columnNameType, productType);	
	};
	
	
	
}
