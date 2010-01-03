/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class Person extends Entity<Person.ColumnName, Person> {
		
	public enum ColumnName {
		ID,
		FIRST_NAME,
		LAST_NAME,
		DATE_OF_BIRTH,
		ADDRESS_ID
		;
	}	
	
	private static EntityFactory<ColumnName, Person> factory; 
		
	@Override
	public EntityFactory<ColumnName, Person> getFactory() {
		if (factory == null) {
			factory = new DefaultEntityFactory<ColumnName, Person>() {
				@Override
				public Person newInstance() {			
					return new Person();
				}
			};
		}
		return factory;
	}
	
}
