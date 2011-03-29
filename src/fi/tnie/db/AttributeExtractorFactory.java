/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.types.ReferenceType;

public interface AttributeExtractorFactory {

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
	AttributeExtractor<A, R, T, E, ?, ?, ?, ?> createExtractor(A attribute, EntityMetaData<A, R, T, E> em, int sqltype, int col, ValueExtractorFactory vef);


//	IntegerAttributeExtractor<A, Entity<A,?,?,E>> createIntegerExtractor(int col);
//	VarcharExtractor createVarcharExtractor(int col);
}
