/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.types.ReferenceType;

public interface GeneratedKeyHandler {
	<
	    A extends Attribute,
	    R extends Reference,
	    T extends ReferenceType<A, R, T, E, ?, ?, M>,
	    E extends Entity<A, R, T, E, ?, ?, M>,
		M extends EntityMetaData<A, R, T, E, ?, ?, M>
	>
	void processGeneratedKeys(InsertStatement insert, E target, ResultSet rs)
		throws SQLException, EntityException;
}
