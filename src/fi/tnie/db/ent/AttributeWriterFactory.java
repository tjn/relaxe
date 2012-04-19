/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

// import java.sql.SQLException;

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
		T extends ReferenceType<A, ?, T, E, ?, ?, M, ?>,
		E extends Entity<A, ?, T, E, ?, ?, M, ?>,
		M extends EntityMetaData<A, ?, T, E, ?, ?, M, ?> 
	>
	AttributeWriter<A, T, E, ?, ?, ?, ?> createWriter(M em, ColumnResolver cr, int index);
}
