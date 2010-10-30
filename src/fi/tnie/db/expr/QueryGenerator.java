/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr;

public class QueryGenerator
	extends TokenVisitor {
	
	private StringBuffer buffer = null;	
	private Token previous;
			
	public QueryGenerator(StringBuffer buffer) {
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
//			if (previous.isOrdinary() && next.isOrdinary()) {
//				buffer.append(" ");
//			}
		    
		    buffer.append(" ");
			
//			if (previous.isOrdinary()) {
//			    buffer.append(" ");
//			}
		}
		
//		logger().debug("token: " + next);
		
		String ts = next.getTerminalSymbol();
		
		if (ts != null) {
			buffer.append(ts);
		}				
		
//	String ts = e.getTerminalSymbol();
//	
//	if (ts != null) {
//		buffer.append(ts);
//		
//		if (suffix != null) {
//			buffer.append(suffix);
//		}
//	}		
				
		this.previous = next;
		
		return null;
	}
	
}