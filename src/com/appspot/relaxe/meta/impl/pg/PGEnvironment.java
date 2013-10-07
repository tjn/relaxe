/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.expr.DateLiteral;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.LongLiteral;
import com.appspot.relaxe.expr.NiladicFunction;
import com.appspot.relaxe.expr.StringLiteral;
import com.appspot.relaxe.expr.TimestampLiteral;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;
import com.appspot.relaxe.expr.ddl.SQLType;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.SerializableEnvironment;
import com.appspot.relaxe.types.PrimitiveType;

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
	
	public DefaultDefinition newDefaultDefinition(Column col) {
		return newDefaultDefinition(col.getColumnDefault(), col.getDataType().getDataType());
	}
	
	public DefaultDefinition newDefaultDefinition(String defval, int type) {
		if (defval == null) {
			return DefaultDefinition.NULL;
		}
		
		DefaultDefinition def = null;	
		
		int cop = defval.lastIndexOf("::");
		
		String head = (cop < 0) ? defval : defval.substring(0, cop);
		
		int t = type;
		
		if (SQLType.isTextType(t)) {
			boolean lit = (head.length() > 0) && (head.charAt(0) == '\'');

			if (lit) {
				def = new DefaultDefinition(new StringLiteral(head, true));	
			}
			else {
				// function call? not supported
			}
			
			return def;
		}
		
		if (SQLType.isIntegralType(t)) {
			boolean lit = head.matches("[+-]?[0-9]+");
			
			if (lit) {
				def = new DefaultDefinition(
						(t == PrimitiveType.BIGINT) ? 
						new LongLiteral(Long.parseLong(head)) : 		
						new IntLiteral(Integer.parseInt(head)));	
			}
			else {
				// function call? not supported
			}	
			
			return def;
		}
		
		if (t == PrimitiveType.DATE) {
			boolean lit = head.matches("'[0-9]{4}-[0-9]{2}-[0-9]{2}'");
			
			if (lit) {
				// 'yyyy-MM-dd'
				int yyyy = Integer.parseInt(head.substring(1, 5));				
				int mm = Integer.parseInt(head.substring(6, 8));
				int dd = Integer.parseInt(head.substring(9, 11));				
				def = new DefaultDefinition(new DateLiteral(yyyy, mm, dd));	
			}			
			else {
				// ('now'::text)::date				
				if (defval.equals("('now'::text)::date")) {
					def = new DefaultDefinition(NiladicFunction.CURRENT_DATE);
				}
			}
			
			return def;
		}
		
		if (t == PrimitiveType.TIMESTAMP) {
			boolean lit = head.matches("'[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]+)?'");
			
			if (lit) {
				// 'yyyy-MM-dd HH:mm:ss.SSS'
				int yyyy = Integer.parseInt(head.substring(1, 5));				
				int mm = Integer.parseInt(head.substring(6, 8));
				int dd = Integer.parseInt(head.substring(9, 11));				
				
				int h = Integer.parseInt(head.substring(12, 14));
				int m = Integer.parseInt(head.substring(15, 17));
				int s = Integer.parseInt(head.substring(18, 20));
				
				int sss = 0;
				int prec = 0;
				
				int hl = head.length();
				
				if (hl > 21) {
					prec = hl - 22;
					sss = Integer.parseInt(head.substring(21, hl - 1));
				}				
				
				def = new DefaultDefinition(new TimestampLiteral(yyyy, mm, dd, h, m, s, sss, prec));	
			}			
			else {
				// now()				
				if (defval.equals("now()")) {
					def = new DefaultDefinition(NiladicFunction.CURRENT_TIMESTAMP);
				}
			}
			
			return def;
		}
		
		return null;
	}

	
}
