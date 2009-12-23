package fi.tnie.db;

import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.meta.BaseTable;

public class Person extends DBEntity<Person.ColumnName, Person> {
		
	public enum ColumnName {
		A,
		B,
		C		
		;		
		
		public void show() {			
		}
	}
	
	

	@Override
	public EntityFactory<ColumnName, Person> getFactory() {
		return new DefaultEntityFactory<ColumnName, Person>() {
				@Override
				public Person newInstance() {			
					return new Person();
				}			
		};
	}

	@Override
	public Person newInstance() {
		return new Person();
	}	
}
