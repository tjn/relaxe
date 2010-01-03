/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public abstract class AbstractExtractor implements Extractor {

	private int column;	

	public AbstractExtractor(int column) {
		super();
		
		if (column < 1) {
			throw new IllegalArgumentException();
		}
		
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}	
}
