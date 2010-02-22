/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class Person 
	extends DefaultEntity<Person.Attribute, Person.Reference, Person.Query, Person> {
			
	protected Person(EntityMetaData<Attribute, Reference, Query, Person> meta) {
		super(meta);
	}

	public enum Attribute implements Identifiable {
		ID,
		FIRST_NAME,
		LAST_NAME,
		DATE_OF_BIRTH,
		ADDRESS_ID
		;

		@Override
		public String identifier() {		
			return name();
		}
	}
	
	public enum Reference implements Identifiable {
		ADDRESS_ID;
		
		@Override
		public String identifier() {		
			return name();
		}		
	}

	public enum Query implements Identifiable {
		;

		@Override
		public String identifier() {		
			return name();
		}		
	}
	
	public static void main(String[] args) {		
		
//		Person p = new Person(new DefaultEntityMetaData<Attribute, Reference, Query, Person>);		
//		p.set(Reference.ADDRESS_ID, null);
//		p.set(Attribute.DATE_OF_BIRTH, null);
// 		p.setValue(Attribute.FIRST_NAME, new Att);
	}
	
	
//	private static EntityFactory<Attribute, Person> factory; 
//		
//	@Override
//	public EntityFactory<Attribute, Person> getFactory() {
//		if (factory == null) {
//			factory = new DefaultEntityFactory<Attribute, Person>() {
//				@Override
//				public Person newInstance() {			
//					return new Person();
//				}
//			};
//		}
//		return factory;
//	}
	
}
