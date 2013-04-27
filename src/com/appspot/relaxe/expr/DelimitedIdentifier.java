/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;


public class DelimitedIdentifier
	extends AbstractIdentifier {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4618716859483968962L;
	private String token;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DelimitedIdentifier() {
	}
	
	public DelimitedIdentifier(String name) 
		throws IllegalIdentifierException {
		super(name);
	}
	
	
	public String getToken() {
		if (token == null) {			
			String qm = "\""; 					
			this.token = qm + getName().replace(qm, qm + qm) + qm;
		}

		return token;
	}	

	@Override
	public boolean isOrdinary() {
		return false;
	}

	@Override
	public String getTerminalSymbol() {		
		return getToken();
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}
	
//	else {
//		this.ordinary = isValidOrdinary(token, null);
//		
//		if (this.ordinary) {
//			this.token = token;
//		}
//		else {
//			String qm = "\""; 
//			token.replace(qm, qm + qm);		
//			this.token = qm + token + qm;				
//		}
//	}		

}
