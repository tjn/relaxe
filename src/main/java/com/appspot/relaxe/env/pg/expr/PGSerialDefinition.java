package com.appspot.relaxe.env.pg.expr;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.types.AbstractIntegalNumberType;
import com.appspot.relaxe.expr.pg.PostgreSQLKeyword;

public class PGSerialDefinition
	extends AbstractIntegalNumberType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5624139529068959618L;
	
	private static final PGSerialDefinition TYPE = new PGSerialDefinition();
	
	

	/**
	 * No-argument constructor for GWT Serialization
	 */
	public PGSerialDefinition() {
	}
	
	
	public static PGSerialDefinition get() {
		return TYPE;
	}
		
	@Override
	protected void traverseName(VisitContext vc, ElementVisitor v) {
		PostgreSQLKeyword.SERIAL.traverse(vc, v);
	}
	
	
}
