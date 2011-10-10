/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.op;

import fi.tnie.db.expr.Predicate;

public class ParenthesizedPredicate
	extends Parenthesis<Predicate>
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6979122703202314520L;


	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ParenthesizedPredicate() {
	}
	
	public ParenthesizedPredicate(Predicate content) {
		super(content);		
	}


	@Override
	public Predicate parenthesize() {
		return new ParenthesizedPredicate(this);
	}

}
