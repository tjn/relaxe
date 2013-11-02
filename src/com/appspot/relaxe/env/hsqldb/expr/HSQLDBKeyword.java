/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.hsqldb.expr;

import java.util.Map;
import java.util.TreeMap;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.Token;
import com.appspot.relaxe.expr.VisitContext;

public enum HSQLDBKeyword
    implements Token {
    
    SHUTDOWN,
    ARRAY,
//    ,
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
        return super.toString();
    }

    @Override
    public boolean isOrdinary() {
        return true;
    }
    
	public static boolean isKeyword(String s) {				
		return keywordMap.containsKey(s);
	}
}
