/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.Map;
import java.util.TreeMap;

public enum SQLKeyword 
	implements Keyword {	
	SELECT,
	FROM,
	WHERE,
	GROUP,
	HAVING,
	ORDER,
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
	CURRENT,
	USER,	
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
	GENERATED, 
	ALWAYS, 
	BY, 
	IDENTITY, 
	START, 
	WITH, 
	INCREMENT
	;
	
	private static Map<String, SQLKeyword> keywordMap = new TreeMap<String, SQLKeyword>(String.CASE_INSENSITIVE_ORDER); 
	
	static {
		for (SQLKeyword kw : SQLKeyword.values()) {
			keywordMap.put(kw.name(), kw);
		}		
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);		
	}
	
	@Override
	public String getTerminalSymbol() {
		return super.toString(); // super.toString().replace('_', ' ');
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
	
	public static boolean isKeyword(String s) {				
		return keywordMap.containsKey(s);
	}
}
