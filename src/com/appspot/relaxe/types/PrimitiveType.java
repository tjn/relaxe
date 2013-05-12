/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.types;


public interface PrimitiveType<
	T extends PrimitiveType<T>
> 
	extends Type<T> {
	
	int ARRAY = 2003;
	int BIGINT = -5;
	int BINARY = -2;
	int BIT = -7;
	int BLOB = 2004;
	int BOOLEAN = 16;
	int CHAR = 1;
	int CLOB = 2005;
	int DATALINK = 70;
	int DATE = 91;
	int DECIMAL = 3;
	int DISTINCT = 2001;
	int DOUBLE = 8;
	int FLOAT = 6;
	int INTEGER = 4;
	int JAVA_OBJECT = 2000;
	int LONGNVARCHAR = -16;
	int LONGVARBINARY = -4;
	int LONGVARCHAR = -1;
	int NCHAR = -15;
	int NCLOB = 2011;
	int STRUCT = 2002;
	int NULL = 0;
	int NUMERIC = 2;
	int NVARCHAR = -9;
	int OTHER = 1111;
	int REAL = 7;
	int REF = 2006;
	int ROWID = -8;
	int SMALLINT = 5;
	int SQLXML = 2009;
	int TIME = 92;
	int TIMESTAMP = 93;
	int TINYINT = -6;
	int VARBINARY = -3;
	int VARCHAR = 12;

	int getSqlType();
	OtherType<?> asOtherType();	
	ArrayType<?, ?> asArrayType();

}
