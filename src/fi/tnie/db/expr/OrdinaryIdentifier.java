/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


public class OrdinaryIdentifier
	extends AbstractIdentifier
{
	public OrdinaryIdentifier(String name) 
		throws IllegalIdentifierException {
		super(name);
		validateOrdinary(name);
	}

	@Override
	public boolean isOrdinary() {	
		return true;
	}

	@Override
	public String toString() {		
		return "[" + getName() + ": " + super.toString() + "]";
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);		
	}
	
	public static void validateOrdinary(String token) 
		throws IllegalIdentifierException {
		StringBuffer details = new StringBuffer();
		
		if (!isValidOrdinary(token, details)) {
			throw new IllegalIdentifierException(details.toString());
		}
	}	
	
	public static boolean isValidOrdinary(String token) {
		return isValidOrdinary(token, null);
	}
	
	private static boolean isValidOrdinary(String token, StringBuffer details) {		
		String p = "[A-Za-z][A-Za-z0-9_]*";
		
		if (!token.matches(p)) {
			return fail("token '" + token + "' doesn't match the pattern: " + p, details); 
		}
	
		if (Keyword.isKeyword(token.toUpperCase())) {
			return fail(
					"token '" + token + "' is identical to the keyword and " +
					"can not be used as a ordinary identifier", details);
		}	
		
		return true;
	}

	@Override
	public String getTerminalSymbol() {
		return getName();
	}	
}
