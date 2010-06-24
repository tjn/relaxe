/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;


public interface DataType {
	int getDataType();
//	short getSourceDataType();
	String getTypeName();
	int getCharOctetLength();
	int getDecimalDigits();
	int getNumPrecRadix();
	int getSize();	

}
