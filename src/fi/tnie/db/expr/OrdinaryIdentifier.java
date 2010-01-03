/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class OrdinaryIdentifier
	extends SimpleElement
	implements Identifier
{
	private String name;

	public OrdinaryIdentifier(String name) {
		super();
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		if (name.length() == 0) {
			throw new NullPointerException("'name' must not be empty");
		}
		
		name = name.trim().toUpperCase();
		
		String p = "[A-Z][A-Z0-9_]*";
		
		if (!name.matches(p)) {
			throw new IllegalArgumentException("name '" + name + "' doesn't match the pattern: " + p); 
		}
	
		if (Keyword.isKeyword(name)) {
			throw new IllegalArgumentException("name '" + name + "' is identical to the keyword");
		}
		
		this.name = name;
	}

	@Override
	public String getTerminalSymbol() {
		return getName();
	}

	@Override
	public boolean isOrdinary() {	
		return true;
	}

	public String getName() {
		return this.name;
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
}
