/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.Entity;
import fi.tnie.db.EntityException;
import fi.tnie.db.Identifiable;
import fi.tnie.db.expr.InsertStatement;

public interface GeneratedKeyHandler {	
	<
	    A extends Enum<A> & Identifiable, 
	    R extends Enum<R> & Identifiable,
	    Q extends Enum<Q> & Identifiable,
	    E extends Entity<A, R, Q, ? extends E>
	>
	void processGeneratedKeys(InsertStatement insert, E target, ResultSet rs) 
		throws SQLException, EntityException;
}
