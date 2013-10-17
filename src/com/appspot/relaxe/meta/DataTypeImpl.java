/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.io.Serializable;


public class DataTypeImpl 
	implements DataType, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4891848179452614861L;
	private int dataType;	
	private String typeName;
	private Integer charOctetLength;	
	private Integer decimalDigits;
	private Integer numPrecRadix;
	private Integer size;
	
//	private short sourceDataType;

	public DataTypeImpl() {
		super();
	}
	
	public DataTypeImpl(int dataType, String typeName) {
		super();
		this.dataType = dataType;
		this.typeName = typeName;		
	}
	
	public DataTypeImpl(int dataType, String typeName, Integer charOctetLength, Integer decimalDigits, Integer numPrecRadix, Integer size) {
		this.dataType = dataType;
		this.typeName = typeName;		
		this.charOctetLength = charOctetLength;
		this.decimalDigits = decimalDigits;
		this.numPrecRadix = numPrecRadix;
		this.size = size;
	}
	
	public DataTypeImpl(DataType source) {
		super();
		this.dataType = source.getDataType();
		this.typeName = source.getTypeName();		
		this.charOctetLength = source.getCharOctetLength();
		this.decimalDigits = source.getDecimalDigits();
		this.numPrecRadix = source.getNumPrecRadix();
		this.size = source.getSize();
	}


//	@Override
//	public AbstractMetaObject getParent() {
//		return getSchema();
//	}

	/* (non-Javadoc)
	 * @see com.appspot.relaxe.meta.impl.DataType#getDataType()
	 */
	@Override
	public int getDataType() {
		return dataType;
	}


	/* (non-Javadoc)
	 * @see com.appspot.relaxe.meta.impl.DataType#getTypeName()
	 */
	@Override
	public String getTypeName() {
		return typeName;
	}

//	public void setDataType(int dataType) {
//		this.dataType = dataType;
//	}
//
//
//	public void setTypeName(String typeName) {
//		this.typeName = typeName;
//	}

	//	@Override
	//	public AbstractMetaObject getParent() {
	//		return getSchema();
	//	}
	
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.meta.impl.DataType#getCharOctetLength()
	 */
	@Override
	public Integer getCharOctetLength() {
		return charOctetLength;
	}

	@Override
	public Integer getDecimalDigits() {
		return decimalDigits;
	}

	/* (non-Javadoc)
	 * @see com.appspot.relaxe.meta.impl.DataType#getNumPrecRadix()
	 */
	@Override
	public Integer getNumPrecRadix() {
		return numPrecRadix;
	}

	/* (non-Javadoc)
	 * @see com.appspot.relaxe.meta.impl.DataType#getSize()
	 */
	@Override
	public Integer getSize() {
		return size;
	}

//	public void setCharOctetLength(int charOctetLength) {
//		this.charOctetLength = Integer.valueOf(charOctetLength;
//	}
//
//	public void setDecimalDigits(int decimalDigits) {
//		this.decimalDigits = decimalDigits;
//	}
//
//	public void setNumPrecRadix(int numPrecRadix) {
//		this.numPrecRadix = numPrecRadix;
//	}
//
//	public void setSize(int size) {
//		this.size = size;
//	}
		
	
//	@Override
//	protected Object clone() throws CloneNotSupportedException {	
//		DataTypeImpl copy = (DataTypeImpl) super.clone();				
//		return copy;
//	}

}
