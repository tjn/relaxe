/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;
import fi.tnie.db.SimpleQueryContext;

public class TokenVisitor implements ElementVisitor {

	private QueryContext context;
	
	@Override
	public void end(Element e) {
	}

	@Override
	public VisitContext start(VisitContext vc, Element e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Select e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, From e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Where e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Having e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, OrderBy e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Predicate e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Keyword e) {
		return visit(e);
	}

	@Override
	public VisitContext start(VisitContext vc, AbstractTableReference e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, JoinType e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, TableColumnExpr e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Symbol e) {
		return visit(e);
	}

	@Override
	public VisitContext start(VisitContext vc, Name e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Parameter e) {
		return visit(e);
	}
	
	public VisitContext visit(Token e) {
		return null;
	}

	@Override
	public QueryContext getContext() {
		if (context == null) {
			context = new SimpleQueryContext();			
		}

		return context;
	}

	@Override
	public VisitContext start(VisitContext vc, GroupBy e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Identifier e) {
		return visit(e);		
	}

	@Override
	public VisitContext start(VisitContext vc, Token e) {
		return visit(e);
	}

	@Override
	public VisitContext start(VisitContext vc, Assignment e) {
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, ValueExpression e) {
		return null;
	}
}
