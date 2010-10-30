/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;

import fi.tnie.db.meta.DataType;

public class DataTypeImpl 
	implements DataType, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4891848179452614861L;
	private int dataType;	
	private String typeName;
	private int charOctetLength;	
	private int decimalDigits;
	private int numPrecRadix;
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
	
	public DataTypeImpl(DataTypeImpl source) {
		super();
		this.dataType = source.getDataType();
		this.typeName = source.getTypeName();
		// TODO: add others
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
	 * @see fi.tnie.db.meta.impl.DataType#getNumPrecRadix()
	 */
	public int getNumPrecRadix() {
		return numPrecRadix;
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

	public void setNumPrecRadix(int numPrecRadix) {
		this.numPrecRadix = numPrecRadix;
	}

	public void setSize(int size) {
		this.size = size;
	}
		
	
//	@Override
//	protected Object clone() throws CloneNotSupportedException {	
//		DataTypeImpl copy = (DataTypeImpl) super.clone();				
//		return copy;
//	}

}
