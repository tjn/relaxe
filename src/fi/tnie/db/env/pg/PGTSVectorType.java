/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg;

import fi.tnie.db.types.OtherType;

public class PGTSVectorType
	extends OtherType<PGTSVectorType> {

	
	public static final PGTSVectorType TYPE = new PGTSVectorType();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7152862866657378339L;

		@Override
	public String getName() {
		return "tsvector";
	}

	@Override
	public PGTSVectorType self() {
		return this;
	}
}
