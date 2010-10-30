/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


public class AttributeNotPresentException
	extends RuntimeException {

	private Identifiable attribute;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1830981416966944158L;
		
	protected AttributeNotPresentException() {
		super();	
	}
	
	public AttributeNotPresentException(Identifiable a) {
		if (a == null) {
			throw new NullPointerException("a was null");
		}
		
		this.attribute = a;
	}

	public Identifiable getAttribute() {
		return attribute;
	}	
}
