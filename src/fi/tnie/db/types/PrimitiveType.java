/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import java.io.Serializable;

import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.LongHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;

public abstract class PrimitiveType<T extends PrimitiveType<T>>
	extends Type<T> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 5378281838745232322L;
	
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
			
	public static class SerializableType
		extends PrimitiveType<SerializableType> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 910829028354006706L;
		
		private int sqlType;
				
		/**
		 * No-argument constructor for GWT Serialization
		 */		
		private SerializableType() {	
		}
		
		public SerializableType(int sqlType, String name) {
			this();
			this.sqlType = sqlType;		
		}

		@Override
		public int getSqlType() {
			return sqlType;
		}

		@Override
		public SerializableType self() {
			return this;
		}
	}
	
	public static final class NullHolder<H>
		extends PrimitiveHolder<Serializable, SerializableType, NullHolder<H>>
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
			this.type = new SerializableType(sqlType, null);
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
		
		
		@Override
		public IntegerHolder asIntegerHolder() {
			return IntegerHolder.NULL_HOLDER;
		}
		
		@Override
		public DateHolder asDateHolder() {
			return DateHolder.NULL_HOLDER;
		}
		
		@Override
		public TimeHolder asTimeHolder() {
			return TimeHolder.NULL_HOLDER;
		}
		
		@Override
		public CharHolder asCharHolder() {			
			return CharHolder.NULL_HOLDER;
		}
		
		@Override
		public TimestampHolder asTimestampHolder() {
			return TimestampHolder.NULL_HOLDER;
		}
		
		@Override
		public LongHolder asLongHolder() {
			return LongHolder.NULL_HOLDER;
		}
		
		@Override
		public VarcharHolder asVarcharHolder() {
			return VarcharHolder.NULL_HOLDER;
		}

		@Override
		public NullHolder<H> self() {
			return this;
		}
	}
		
	public static PrimitiveHolder<?, ?, ?> nullHolder(int t) {
		PrimitiveHolder<?, ?, ?> nh = null;
		
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
	
	public OtherType<? extends T> asOtherType() {
		return null;
	}
	
	public ArrayType<? extends T, ?> asArrayType() {
		return null;
	}
	
	public abstract T self();
	
	
	public static String getSQLTypeName(int t) {
		switch (t) {
			case ARRAY:
				return "ARRAY";
			case BIGINT:
				return "BIGINT";
			case BINARY:
				return "BINARY";
			case BIT:
				return "BIT";
			case BLOB:
				return "BLOB";
			case BOOLEAN:
				return "BOOLEAN";
			case CHAR:
				return "CHAR";
			case CLOB:
				return "CLOB";
			case DATALINK:
				return "DATALINK";
			case DATE:
				return "DATE";
			case DECIMAL:
				return "DECIMAL";
			case DISTINCT:
				return "DISTINCT";
			case DOUBLE:
				return "DOUBLE";
			case FLOAT:
				return "FLOAT";
			case INTEGER:
				return "INTEGER";
			case JAVA_OBJECT:
				return "JAVA_OBJECT";
			case LONGNVARCHAR:
				return "LONGNVARCHAR";
			case LONGVARBINARY:
				return "LONGVARBINARY";
			case LONGVARCHAR:
				return "LONGVARCHAR";
			case NCHAR:
				return "NCHAR";
			case NCLOB:
				return "NCLOB";
			case STRUCT:
				return "STRUCT";
			case NULL:
				return "NULL";
			case NUMERIC:
				return "NUMERIC";
			case NVARCHAR:
				return "NVARCHAR";
			case OTHER:
				return "OTHER";
			case REAL:
				return "REAL";
			case REF:
				return "REF";
			case ROWID:
				return "ROWID";
			case SMALLINT:
				return "SMALLINT";
			case SQLXML:
				return "SQLXML";
			case TIME:
				return "TIME";
			case TIMESTAMP:
				return "TIMESTAMP";
			case TINYINT:
				return "TINYINT";
			case VARBINARY:
				return "VARBINARY";
			case VARCHAR:
				return "VARCHAR";				
		}
		
		return new StringBuilder("UNKNOWN (").append(t).append(")").toString();		
	}
}
