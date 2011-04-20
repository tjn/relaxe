/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.types.ReferenceType;

public interface AttributeWriterFactory {
	/**
	 *
	 * @param meta
	 * @param col
	 * @return
	 * @throws SQLException
	 */
	<
		A extends Attribute, 
		R,
		T extends ReferenceType<T>,		
		E extends Entity<A, R, T, E>
	>
	AttributeWriter<A, R, T, E, ?, ?, ?, ?> createWriter(EntityMetaData<A, R, T, E> em, DataObject.MetaData meta, int index);
}
