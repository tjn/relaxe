/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import fi.tnie.db.expr.NonJoinedTable;
import fi.tnie.db.expr.OrdinaryIdentifier;

public interface QueryContext {

	OrdinaryIdentifier correlationName(NonJoinedTable tref);

}