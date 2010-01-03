/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class DelimitedIdentifier
	extends SimpleElement implements Identifier {
	
	private String name;

	public DelimitedIdentifier(String name) {
		super();
		
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		if (name.length() == 0) {
			throw new NullPointerException("'name' must not be empty");
		}		
		
		String qm = "\""; 
		name.replace(qm, qm + qm);		
		this.name = qm + name + qm;
	}

	public String getName() {
		return name;
	}	

	@Override
	public boolean isOrdinary() {
		return false;
	}

	@Override
	public String getTerminalSymbol() {		
		return getName();
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}
}
