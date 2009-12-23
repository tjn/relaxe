package fi.tnie.db.meta;

import java.util.Map;

public interface Schema
	extends MetaObject {

	public Catalog getCatalog();	
	Map<String, Table> tables();
		
}
