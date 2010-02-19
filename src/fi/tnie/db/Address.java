/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class Address 
	extends DefaultEntity<
		Address.Attribute, 
		Address.Relationship, 
		Address.Query,
		Address>
	{

	public enum Attribute implements Identifiable {
		ID,
		FIRST_NAME,
		LAST_NAME,
		DATE_OF_BIRTH;

		@Override
		public String identifier() {
			return name();
		}
	}
	public enum Relationship implements Identifiable {
		;

		@Override
		public String identifier() {
			return name();
		}		
	}
	
	public enum Query implements Identifiable {
		;

		@Override
		public String identifier() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	@Override
	public void set(Attribute a, Object value) {
		super.set(a, value);
	}
	
//	public void test() {
////		getKey(null);
//	}

//	@Override
//	public EntityFactory<ColumnName, Address> getFactory() {
//		return new DefaultEntityFactory<ColumnName, Address>() {
//			@Override
//			public Address newInstance() {			
//				return new Address();
//			}			
//		};
//	}
}
