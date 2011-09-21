/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.CompoundElement;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.SelectQuery;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.VisitContext;

public class Exists
	extends CompoundElement
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7282243674920197042L;
	private SelectQuery query;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Exists() {
	}

	public Exists(SelectQuery query) {
		super();
		
		if (query == null) {
			throw new NullPointerException("'query' must not be null");
		}
		
		this.query = query;
	}

	//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("EXISTS (");
//		fullSelect.generate(qc, dest);
//		dest.append(") ");
//	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		
		Keyword.EXISTS.traverse(vc, v);		
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.query.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
		
		v.end(this);
	}
}
