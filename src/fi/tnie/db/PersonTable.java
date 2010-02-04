/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;

public class PersonTable {

	public enum Column {
		NAME,
		DATE_OF_BIRTH	
	};
	
	public PersonTable() {		
	}
	
	void insert(Row<Column> row, Connection c) {
			
		
	}
	
	void update(Row<Column> row) {
		
		
	}
	
	void delete(Row<Column> row) {
		
	}
	
	
	
}
