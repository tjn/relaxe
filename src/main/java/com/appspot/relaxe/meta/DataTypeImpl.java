/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(140);
		buf.append(super.toString());
		buf.append(":");
		buf.append("{" );
		buf.append(", dataType: ").append(dataType);
		buf.append(", typeName: ").append(typeName);
		buf.append(", size: ").append(size);
		buf.append(", charOctetLength: ").append(charOctetLength);
		buf.append(", numPrecRadix: ").append(numPrecRadix);
		buf.append(", decimalDigits: ").append(decimalDigits);
		buf.append("}");
				
		return buf.toString();
	}
		
	
//	@Override
//	protected Object clone() throws CloneNotSupportedException {	
//		DataTypeImpl copy = (DataTypeImpl) super.clone();				
//		return copy;
//	}

}
