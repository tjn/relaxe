/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public abstract class AbstractExtractor implements Extractor {

	private int ordinal;	

	public AbstractExtractor(int ordinal) {
		super();
		
		if (ordinal < 1) {
			throw new IllegalArgumentException();
		}
		
		this.ordinal = ordinal;
	}
	
	public int getOrdinal() {
		return ordinal;
	}	
}
