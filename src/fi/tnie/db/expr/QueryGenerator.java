/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr;

import org.apache.log4j.Logger;

public class QueryGenerator
	extends TokenVisitor {
	
	private static Logger logger = Logger.getLogger(QueryGenerator.class);
	
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
			if (previous.isOrdinary() && next.isOrdinary()) {
				buffer.append(" ");
			}
		}
		
		logger().debug("token: " + next);
		
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
	
	public static Logger logger() {
		return QueryGenerator.logger;
	}
}