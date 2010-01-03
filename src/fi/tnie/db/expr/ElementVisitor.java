/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public interface ElementVisitor {
	
	QueryContext getContext();
	
	VisitContext start(VisitContext vc, Element e);	
	VisitContext start(VisitContext vc, Select e);
	VisitContext start(VisitContext vc, From e);	
	VisitContext start(VisitContext vc, Where e);
	VisitContext start(VisitContext vc, GroupBy e);
	VisitContext start(VisitContext vc, Having e);
	VisitContext start(VisitContext vc, OrderBy e);	
	VisitContext start(VisitContext vc, Predicate e);
	VisitContext start(VisitContext vc, Keyword e);
	VisitContext start(VisitContext vc, AbstractTableReference e);
	VisitContext start(VisitContext vc, JoinType e);	
	VisitContext start(VisitContext vc, TableColumnExpr e);
	VisitContext start(VisitContext vc, Symbol e);
	VisitContext start(VisitContext vc, Name e);
	VisitContext start(VisitContext vc, Parameter e);
	VisitContext start(VisitContext vc, Token e);
	VisitContext start(VisitContext vc, Identifier e);
	VisitContext start(VisitContext vc, Assignment e);
	VisitContext start(VisitContext vc, ValueExpression e);
	
	void end(Element e);
}
