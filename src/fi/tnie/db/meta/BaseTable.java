package fi.tnie.db.meta;

import java.util.Map;

public interface BaseTable
	extends Table {

	Map<String, ForeignKey> references();
	Map<String, ForeignKey> foreignKeys();	
	
	PrimaryKey getPrimaryKey();
	
}
