/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public interface VisitContext {
	VisitContext getParent();
	VisitContext open(Element e);
	void close();
}
