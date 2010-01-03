/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.EnumSet;

public enum Keyword implements Token {
	
	SELECT,
	FROM,
	WHERE,
	GROUP_BY,
	HAVING,
	ORDER_BY,
	IN,
	LIKE,
	BETWEEN,
	EXISTS,
	AND,
	OR,
	NOT,
	UNION,
	EXCEPT,
	INTERSECT,
	INNER,
	LEFT,
	RIGHT,
	OUTER,	
	FULL,
	JOIN,
	ALL,
	DISTINCT,
	AS,
	INSERT,	
	INTO,
	VALUES,
	UPDATE,
	SET, 
	NULL, 
	DEFAULT	
	;
	
	
	private static EnumSet<Keyword> keywords = EnumSet.allOf(Keyword.class);
	
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append(toString());
//	}

	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);		
	}
	
	@Override
	public String getTerminalSymbol() {
		return super.toString().replace('_', ' ');
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
	
	public static boolean isKeyword(String s) {
		s = s.trim().toUpperCase();		
		return keywords.contains(s);
	}
}
