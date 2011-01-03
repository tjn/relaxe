/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


public class AttributeNotPresentException
	extends RuntimeException {

	private Ref<?> attribute;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -1830981416966944158L;
		
	protected AttributeNotPresentException() {
		super();	
	}
		
	public AttributeNotPresentException(Ref<?> a) {
		if (a == null) {
			throw new NullPointerException("a");
		}
		
		this.attribute = a;
	}

	public Ref<?> getAttribute() {
		return attribute;
	}	
}
