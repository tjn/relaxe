/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

public class QueryGenerator
	extends TokenVisitor {
	
	private StringBuilder buffer = null;	
	private Token previous;
				
	public QueryGenerator(StringBuilder buffer) {
		super();		
		this.buffer = buffer;		
	}
	
	
//	private VisitContext append(Element e) {
//		return append(e, " ");
//	}
//
//	private VisitContext append(Element e, String suffix) {
//		String ts = e.getTerminalSymbol();
//		
//		if (ts != null) {
//			buffer.append(ts);
//			
//			if (suffix != null) {
//				buffer.append(suffix);
//			}
//		}			
//
//		
//		return null;
//	}
	
	
	
	
	

	@Override
	public VisitContext visit(Token next) {
		if (previous != null) {
			if (previous.isOrdinary() && next.isOrdinary()) {
				buffer.append(" ");
			}			
			
			String ts = previous.getTerminalSymbol();
			
			if (ts != null && ts.equals("?")) {
				buffer.append(" ");
			}
		}
		
		String ts = next.getTerminalSymbol();
		
		if (ts != null) {
			buffer.append(ts);
		}			
	
				
		this.previous = next;
		
		return null;
	}

	
	@Override
	public VisitContext start(VisitContext vc, SQLKeyword e) {
		if (previous != null && (!previous.isOrdinary())) {		
			buffer.append(' ');
		}
		
		return super.start(vc, e);
	}
	
	@Override
	public VisitContext start(VisitContext vc, Identifier e) {		
		// For MySQL: which
		
		if (previous != null && previous.isOrdinary()) {		
			buffer.append(' ');
		}
		
		return super.start(vc, e);
	}
	
}