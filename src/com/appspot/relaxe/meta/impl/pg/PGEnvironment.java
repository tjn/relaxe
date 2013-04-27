/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.meta.SerializableEnvironment;

public class PGEnvironment	
	implements SerializableEnvironment {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 6323320738822722962L;
	
	private static PGEnvironment environment = new PGEnvironment();
	private final PGIdentifierRules identifierRules = new PGIdentifierRules();
	
	public static PGEnvironment environment() {
		return PGEnvironment.environment;
	}

	private PGEnvironment() {
	}
    
//    public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
//        // TODO: support big:     
//        Identifier n = toIdentifier(columnName);                
//        return new ColumnDefinition(n, new Serial());
//    }
//    
//	private static class Serial
//		extends SimpleElement
//		implements ColumnDataType, Token {
//
//	    /**
//		 * 
//		 */
//		private static final long serialVersionUID = -1177806475050170208L;
//
//		@Override
//	    public String getTerminalSymbol() {
//	        return "serial";
//	    }
//	
//	    @Override
//	    public void traverse(VisitContext vc, ElementVisitor v) {
//	        v.start(vc, this);
//	        v.end(this);
//	    }
//	
//	    @Override
//	    public boolean isOrdinary() {
//	        return true;
//	    }
//	}
	
	@Override
	public PGIdentifierRules getIdentifierRules() {
		return identifierRules;
	}
	
}
