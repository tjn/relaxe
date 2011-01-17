/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.types.ReferenceType;

public interface GeneratedKeyHandler {	
	<
	    A,
	    R,
	    T extends ReferenceType<T>,
	    E extends Entity<A, R, T, E>
	>
	void processGeneratedKeys(InsertStatement insert, E target, ResultSet rs) 
		throws SQLException, EntityException;
}
