/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;


public interface DataType {
	int getDataType();
	String getTypeName();
	Integer getCharOctetLength();
	Integer getDecimalDigits();
	Integer getNumPrecRadix();
	Integer getSize();
}
