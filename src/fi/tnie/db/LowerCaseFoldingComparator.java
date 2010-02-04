/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


/**
 * PosgreSQL -compatible FoldingComparator. 
 * 
 * @author Administrator
 */
public class LowerCaseFoldingComparator
	extends FoldingComparator {

	@Override
	protected String fold(String ordinaryIdentifier) {		
		return ordinaryIdentifier.toLowerCase();
	}
}
