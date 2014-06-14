/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
	DOMAIN,
	TYPE,
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
	CHAR,
	CHARACTER,
	LONG,
	LONGVARBINARY,
	VARBINARY,
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
	DOUBLE,
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
	INCREMENT,
	PRIMARY,
	PRECISION,
	FOREIGN,
	KEY,
	REFERENCES,
	TO, 
	ZONE,
	INITIALLY,
	DEFERRED,
	IMMEDIATE
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
