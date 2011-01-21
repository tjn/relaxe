/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;

public interface AttributeExtractorFactory {

	/**
	 *
	 * @param meta
	 * @param col
	 * @return
	 * @throws SQLException
	 */
	<A extends Attribute, E extends Entity<A, ?, ?, E>>
	AttributeExtractor<?, ?, ?, A, E, ?> createExtractor(A attribute, EntityMetaData<A, ?, ? , E> em, int sqltype, int col, ValueExtractorFactory vef);


//	IntegerAttributeExtractor<A, Entity<A,?,?,E>> createIntegerExtractor(int col);
//	VarcharExtractor createVarcharExtractor(int col);
}
