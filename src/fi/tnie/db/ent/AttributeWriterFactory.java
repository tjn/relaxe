/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

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
		E extends Entity<A, ?, ?, E, ?, ?, M, ?>,
		M extends EntityMetaData<A, ?, ?, E, ?, ?, M, ?>
	>
	AttributeWriter<A, E> createWriter(M em, ColumnResolver cr, int index);
}
