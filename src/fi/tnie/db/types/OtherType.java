/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class OtherType
	extends PrimitiveType<OtherType> {
	
	public static final OtherType TYPE = new OtherType();
	
	private OtherType() {
		super(Type.OTHER);
	}
}
