/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

public interface EntityBuilder<
	E extends Entity<?, ?, ?, ?, ?, ?, ?, ?>> {
	
	E read(DataObject src);
}
