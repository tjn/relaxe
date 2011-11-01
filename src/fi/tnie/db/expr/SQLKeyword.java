/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.EnumSet;

public enum SQLKeyword implements Keyword {
	
	SELECT,
	FROM,
	WHERE,
	GROUP_BY,
	HAVING,
	ORDER_BY,
	LIMIT,
	OFFSET,
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
	ON,
	ALL,
	DISTINCT,
	AS,
	INSERT,	
	INTO,
	VALUES,
	UPDATE,
	DELETE,
	SET, 
	NULL, 
	DEFAULT,
	CREATE,
    DROP,
    ALTER,
    ADD,
	SCHEMA,
	TABLE,
	VIEW,
	COLUMN,
	TRUNCATE, 
	AUTHORIZATION, 
	CASCADE, 
	RESTRICT, 
	CURRENT_USER,
	CURRENT_DATE,
	CURRENT_TIME,
	CURRENT_TIMESTAMP, 
	CHARACTER, 
	VARCHAR, 
	CLOB, 
	BIGINT, 
	BIT, 
	BLOB,
	NUMERIC, 
	DECIMAL, 
	INTEGER, 
	SMALLINT, 
	FLOAT, 
	VARYING, 
	INT,
	TINYINT, 
	DATE, 
	TIME, 
	TIMESTAMP, 
	CONSTRAINT, 
	ASC, 
	DESC,
	IS, 
	GENERATED, ALWAYS, BY, IDENTITY, START, WITH, INCREMENT
	;
	
	private static EnumSet<SQLKeyword> keywords = EnumSet.allOf(SQLKeyword.class);
	
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
