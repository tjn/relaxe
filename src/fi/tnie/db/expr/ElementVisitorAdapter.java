/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;
import fi.tnie.db.SimpleQueryContext;

public class ElementVisitorAdapter implements ElementVisitor {

	private QueryContext context;
	
	@Override
	public VisitContext start(VisitContext vc, Element e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Select e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, From e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Where e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Having e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, OrderBy e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Predicate e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Keyword e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, AbstractTableReference e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, JoinType e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, TableColumnExpr e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Symbol e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Name e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Parameter e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void end(Element e) {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Token e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Assignment e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, ValueExpression e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VisitContext start(VisitContext vc, Identifier e) {
		// TODO Auto-generated method stub
		return null;
	}
}
