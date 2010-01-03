/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.meta.DataType;

public class DataTypeImpl 
	implements DataType {

	private int dataType;	
	private String typeName;
	private int charOctetLength;	
	private int decimalDigits;
	private int numPrefixRadix;
	private int size;
	
//	private short sourceDataType;

	public DataTypeImpl() {
		super();
	}
	
	public DataTypeImpl(int dataType, String typeName) {
		super();
		this.dataType = dataType;
		this.typeName = typeName;		
	}


//	@Override
//	public AbstractMetaObject getParent() {
//		return getSchema();
//	}

	/* (non-Javadoc)
	 * @see fi.tnie.db.meta.impl.DataType#getDataType()
	 */
	public int getDataType() {
		return dataType;
	}


	/* (non-Javadoc)
	 * @see fi.tnie.db.meta.impl.DataType#getTypeName()
	 */
	public String getTypeName() {
		return typeName;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	//	@Override
	//	public AbstractMetaObject getParent() {
	//		return getSchema();
	//	}
	
		/* (non-Javadoc)
		 * @see fi.tnie.db.meta.impl.DataType#getCharOctetLength()
		 */
		public int getCharOctetLength() {
			return charOctetLength;
		}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	/* (non-Javadoc)
	 * @see fi.tnie.db.meta.impl.DataType#getNumPrefixRadix()
	 */
	public int getNumPrefixRadix() {
		return numPrefixRadix;
	}

	/* (non-Javadoc)
	 * @see fi.tnie.db.meta.impl.DataType#getSize()
	 */
	public int getSize() {
		return size;
	}

	public void setCharOctetLength(int charOctetLength) {
		this.charOctetLength = charOctetLength;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public void setNumPrefixRadix(int numPrefixRadix) {
		this.numPrefixRadix = numPrefixRadix;
	}

	public void setSize(int size) {
		this.size = size;
	}
		
//	@Override
//	public Schema getSchema() {
//		return this.schema;
//	}

//	@Override
//	public DefaultMutableMetaObject getParent() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
//	public static DataType varchar(int size) {		
////		DataType		
////		return new DataType();		
//	}

}
