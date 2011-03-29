/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import java.io.Serializable;

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
	
	// public abstract PrimitiveHolder<?, ? extends T> nil();
	
	/**
	 * TODO: This is not a good idea. 
	 * Type code does not necessarily identify the type. 
	 * For example type OTHER is/might be shared between several distinct types.
	 * Since this seems to be actually used only to create null parameters, we should get rid of this quite easily.   
	 * 
	 * @param sqltype
	 * @return
	 */	
	@Deprecated
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
			// TODO: 
//			p = OtherType.TYPE;
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
	
	public static class SerializableType
		extends PrimitiveType<SerializableType> {
		
		private int sqlType;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */		
		private SerializableType() {	
		}
		
		public SerializableType(int sqlType) {
			this();
			this.sqlType = sqlType;
		}

		@Override
		public int getSqlType() {
			return sqlType;
		}	
	}
	
	public static final class NullHolder<H>
		extends PrimitiveHolder<Serializable, SerializableType>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1380943877418306324L;
		private SerializableType type;
		
		
		private static final NullHolder<SerializableType> ARRAY = new NullHolder<SerializableType>(PrimitiveType.ARRAY);
		private static final NullHolder<SerializableType> BIGINT = new NullHolder<SerializableType>(PrimitiveType.BIGINT);
		private static final NullHolder<SerializableType> BINARY = new NullHolder<SerializableType>(PrimitiveType.BINARY);
		private static final NullHolder<SerializableType> BIT = new NullHolder<SerializableType>(PrimitiveType.BIT);
		private static final NullHolder<SerializableType> BLOB = new NullHolder<SerializableType>(PrimitiveType.BLOB);
		private static final NullHolder<SerializableType> BOOLEAN = new NullHolder<SerializableType>(PrimitiveType.BOOLEAN);
		private static final NullHolder<SerializableType> CHAR = new NullHolder<SerializableType>(PrimitiveType.CHAR);
		private static final NullHolder<SerializableType> CLOB = new NullHolder<SerializableType>(PrimitiveType.CLOB);
		private static final NullHolder<SerializableType> DATALINK = new NullHolder<SerializableType>(PrimitiveType.DATALINK);
		private static final NullHolder<SerializableType> DATE = new NullHolder<SerializableType>(PrimitiveType.DATE);
		private static final NullHolder<SerializableType> DECIMAL = new NullHolder<SerializableType>(PrimitiveType.DECIMAL);
		private static final NullHolder<SerializableType> DISTINCT = new NullHolder<SerializableType>(PrimitiveType.DISTINCT);
		private static final NullHolder<SerializableType> DOUBLE = new NullHolder<SerializableType>(PrimitiveType.DOUBLE);
		private static final NullHolder<SerializableType> FLOAT = new NullHolder<SerializableType>(PrimitiveType.FLOAT);
		private static final NullHolder<SerializableType> INTEGER = new NullHolder<SerializableType>(PrimitiveType.INTEGER);
		private static final NullHolder<SerializableType> JAVA_OBJECT = new NullHolder<SerializableType>(PrimitiveType.JAVA_OBJECT);
		private static final NullHolder<SerializableType> LONGNVARCHAR = new NullHolder<SerializableType>(PrimitiveType.LONGNVARCHAR);
		private static final NullHolder<SerializableType> LONGVARBINARY = new NullHolder<SerializableType>(PrimitiveType.LONGVARBINARY);
		private static final NullHolder<SerializableType> LONGVARCHAR = new NullHolder<SerializableType>(PrimitiveType.LONGVARCHAR);
		private static final NullHolder<SerializableType> NCHAR = new NullHolder<SerializableType>(PrimitiveType.NCHAR);
		private static final NullHolder<SerializableType> NCLOB = new NullHolder<SerializableType>(PrimitiveType.NCLOB);
		private static final NullHolder<SerializableType> STRUCT = new NullHolder<SerializableType>(PrimitiveType.STRUCT);
		private static final NullHolder<SerializableType> NULL = new NullHolder<SerializableType>(PrimitiveType.NULL);
		private static final NullHolder<SerializableType> NUMERIC = new NullHolder<SerializableType>(PrimitiveType.NUMERIC);
		private static final NullHolder<SerializableType> NVARCHAR = new NullHolder<SerializableType>(PrimitiveType.NVARCHAR);
		private static final NullHolder<SerializableType> OTHER = new NullHolder<SerializableType>(PrimitiveType.OTHER);
		private static final NullHolder<SerializableType> REAL = new NullHolder<SerializableType>(PrimitiveType.REAL);
		private static final NullHolder<SerializableType> REF = new NullHolder<SerializableType>(PrimitiveType.REF);
		private static final NullHolder<SerializableType> ROWID = new NullHolder<SerializableType>(PrimitiveType.ROWID);
		private static final NullHolder<SerializableType> SMALLINT = new NullHolder<SerializableType>(PrimitiveType.SMALLINT);
		private static final NullHolder<SerializableType> SQLXML = new NullHolder<SerializableType>(PrimitiveType.SQLXML);
		private static final NullHolder<SerializableType> TIME = new NullHolder<SerializableType>(PrimitiveType.TIME);
		private static final NullHolder<SerializableType> TIMESTAMP = new NullHolder<SerializableType>(PrimitiveType.TIMESTAMP);
		private static final NullHolder<SerializableType> TINYINT = new NullHolder<SerializableType>(PrimitiveType.TINYINT);
		private static final NullHolder<SerializableType> VARBINARY = new NullHolder<SerializableType>(PrimitiveType.VARBINARY);
		private static final NullHolder<SerializableType> VARCHAR = new NullHolder<SerializableType>(PrimitiveType.VARCHAR);

				
		public NullHolder(int sqlType) {
			super();
			this.type = new SerializableType(sqlType);
		}

		@Override
		public int getSqlType() {	
			return type.getSqlType();
		}

		@Override
		public SerializableType getType() {		
			return this.type;
		}
		
		@Override
		public Serializable value() {
			return null;
		}		
	}
		
	public static PrimitiveHolder<?, ?> nullHolder(int t) {
		PrimitiveHolder<?, ?> nh = null;
		
		switch (t) {
		case ARRAY:
			nh = NullHolder.ARRAY;
			break;
		case BIGINT:
			nh = NullHolder.BIGINT;
			break;
		case BINARY:
			nh = NullHolder.BINARY;
			break;
		case BIT:
			nh = NullHolder.BIT;
			break;
		case BLOB:
			nh = NullHolder.BLOB;
			break;
		case BOOLEAN:
			nh = NullHolder.BOOLEAN;
			break;
		case CHAR:
			nh = NullHolder.CHAR;
			break;
		case CLOB:
			nh = NullHolder.CLOB;
			break;
		case DATALINK:
			nh = NullHolder.DATALINK;
			break;
		case DATE:
			nh = NullHolder.DATE;
			break;
		case DECIMAL:
			nh = NullHolder.DECIMAL;
			break;
		case DISTINCT:
			nh = NullHolder.DISTINCT;
			break;
		case DOUBLE:
			nh = NullHolder.DOUBLE;
			break;
		case FLOAT:
			nh = NullHolder.FLOAT;
			break;
		case INTEGER:
			nh = NullHolder.INTEGER;
			break;
		case JAVA_OBJECT:
			nh = NullHolder.JAVA_OBJECT;
			break;
		case LONGNVARCHAR:
			nh = NullHolder.LONGNVARCHAR;
			break;
		case LONGVARBINARY:
			nh = NullHolder.LONGVARBINARY;
			break;
		case LONGVARCHAR:
			nh = NullHolder.LONGVARCHAR;
			break;
		case NCHAR:
			nh = NullHolder.NCHAR;
			break;
		case NCLOB:
			nh = NullHolder.NCLOB;
			break;
		case STRUCT:
			nh = NullHolder.STRUCT;
			break;
		case NULL:
			nh = NullHolder.NULL;
			break;
		case NUMERIC:
			nh = NullHolder.NUMERIC;
			break;
		case NVARCHAR:
			nh = NullHolder.NVARCHAR;
			break;
		case OTHER:
			nh = NullHolder.OTHER;
			break;
		case REAL:
			nh = NullHolder.REAL;
			break;
		case REF:
			nh = NullHolder.REF;
			break;
		case ROWID:
			nh = NullHolder.ROWID;
			break;
		case SMALLINT:
			nh = NullHolder.SMALLINT;
			break;
		case SQLXML:
			nh = NullHolder.SQLXML;
			break;
		case TIME:
			nh = NullHolder.TIME;
			break;
		case TIMESTAMP:
			nh = NullHolder.TIMESTAMP;
			break;
		case TINYINT:
			nh = NullHolder.TINYINT;
			break;
		case VARBINARY:
			nh = NullHolder.VARBINARY;
			break;
		case VARCHAR:
			nh = NullHolder.VARCHAR;
			break;
		default:
			nh = new NullHolder<Serializable>(t);
			break;
		}		
		
		return nh;
	}	
}
