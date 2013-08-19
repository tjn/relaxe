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
	
	public DelimitedIdentifier(String name) {
		super(name);
	}	
	
	public String getToken() {
		if (token == null) {			
			String qm = "\"";
			StringBuilder buf = new StringBuilder(getName().length() + 2);
			buf.append(qm);
			buf.append(getName().replace(qm, qm + qm));
			buf.append(qm);
			this.token = buf.toString();
		}

		return token;
	}	

	@Override
	public boolean isOrdinary() {
		return false;
	}
	
	@Override
	public boolean isDelimited() {
		return true;
	}

	@Override
	public String getTerminalSymbol() {		
		return getToken();
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
