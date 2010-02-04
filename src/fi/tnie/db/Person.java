/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class Person extends Entity<Person.Attribute, Person> {
		
	public enum Attribute {
		ID,
		FIRST_NAME,
		LAST_NAME,
		DATE_OF_BIRTH,
		ADDRESS_ID
		;
	}	
	
	private static EntityFactory<Attribute, Person> factory; 
		
	@Override
	public EntityFactory<Attribute, Person> getFactory() {
		if (factory == null) {
			factory = new DefaultEntityFactory<Attribute, Person>() {
				@Override
				public Person newInstance() {			
					return new Person();
				}
			};
		}
		return factory;
	}
	
}
