package com.appspot.relaxe.types;



public class BlobType
	extends AbstractValueType<BlobType> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -8907814574300729177L;
	
	public static final BlobType TYPE = new BlobType();
	
	private BlobType() {
	}
	
	@Override
	public int getSqlType() {
		return ValueType.BLOB;
	}
	
	@Override
	public BlobType self() {
		return this;
	}
	
	@Override
	public String getName() {
		return "BLOB";
	}

}
