/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.VisitContext;

public class NotPredicate
	implements Predicate {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 2494775816356406506L;
	private Predicate inner;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NotPredicate() {
	}
	
	public NotPredicate(Predicate inner) {
		super();		
		
		if (inner == null) {
			throw new NullPointerException("'inner' must not be null");
		}
		
		this.inner = inner;
	}

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("( NOT ");
//		inner.generate(qc, dest);
//		dest.append(")");
//	}

	@Override
	public String getTerminalSymbol() {
		return null;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		SQLKeyword.NOT.traverse(vc, v);
		inner.traverse(vc, v);		
	}
	
	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}
}
