/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;

public abstract class PrimitiveType<T extends PrimitiveType<T>>
	extends Type<T> {
		
	public static final int ARRAY = 2003;
	public static final int BIGINT = -5;
	public static final int BINARY = -2;
	public static final int BIT = -7;
	public static final int BLOB = 2004;
	public static final int BOOLEAN = 16;
	public static final int CHAR = 1;
	public static final int CLOB = 2005;
	public static final int DATALINK = 70;
	public static final int DATE = 91;
	public static final int DECIMAL = 3;
	public static final int DISTINCT = 2001;
	public static final int DOUBLE = 8;
	public static final int FLOAT = 6;
	public static final int INTEGER = 4;
	public static final int JAVA_OBJECT = 2000;
	public static final int LONGNVARCHAR = -16;
	public static final int LONGVARBINARY = -4;
	public static final int LONGVARCHAR = -1;
	public static final int NCHAR = -15;
	public static final int NCLOB = 2011;
	public static final int STRUCT = 2002;
	public static final int NULL = 0;
	public static final int NUMERIC = 2;
	public static final int NVARCHAR = -9;
	public static final int OTHER = 1111;
	public static final int REAL = 7;
	public static final int REF = 2006;
	public static final int ROWID = -8;
	public static final int SMALLINT = 5;
	public static final int SQLXML = 2009;
	public static final int TIME = 92;
	public static final int TIMESTAMP = 93;
	public static final int TINYINT = -6;
	public static final int VARBINARY = -3;
	public static final int VARCHAR = 12;

	protected PrimitiveType() {
		super();
	}

	@Override
	public final boolean isReferenceType() {	
		return false;
	}
	
	public abstract int getSqlType();
	
	public abstract PrimitiveHolder<?, T> nil();
	
	public static PrimitiveType<?> get(int sqltype) {
		PrimitiveType<?> p = null;
		
		switch (sqltype) {
		case PrimitiveType.CHAR:
			p = CharType.TYPE;
			break;
		case PrimitiveType.DATE:
			p = DateType.TYPE;
			break;
		case PrimitiveType.DECIMAL:
			p = DecimalType.TYPE;
			break;
		case PrimitiveType.DOUBLE:
			p = DoubleType.TYPE;
			break;
		case PrimitiveType.FLOAT:
			p = FloatType.TYPE;
			break;
		case PrimitiveType.INTEGER:
			p = IntegerType.TYPE;
			break;
		case PrimitiveType.LONGVARCHAR:
			p = LongVarcharType.TYPE;
			break;
		case PrimitiveType.OTHER:
			p = OtherType.TYPE;
			break;
		case PrimitiveType.TIMESTAMP:
			p = TimestampType.TYPE;
			break;
		case PrimitiveType.VARCHAR:
			p = VarcharType.TYPE;
			break;			
		default:
			break;
		}
		
		return p;
	}
}
