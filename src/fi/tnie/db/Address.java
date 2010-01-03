/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class Address extends Entity<Address.ColumnName, Address> {

	public enum ColumnName {
		ID,
		FIRST_NAME,
		LAST_NAME,
		DATE_OF_BIRTH;		
	}
	
	public void test() {
//		getKey(null);
	}

	@Override
	public EntityFactory<ColumnName, Address> getFactory() {
		return new DefaultEntityFactory<ColumnName, Address>() {
			@Override
			public Address newInstance() {			
				return new Address();
			}			
		};
	}
}
