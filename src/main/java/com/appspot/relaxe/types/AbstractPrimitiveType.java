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
package com.appspot.relaxe.types;

import java.io.Serializable;

import com.appspot.relaxe.rpc.CharHolder;
import com.appspot.relaxe.rpc.DateHolder;
import com.appspot.relaxe.rpc.IntegerHolder;
import com.appspot.relaxe.rpc.LongHolder;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.TimeHolder;
import com.appspot.relaxe.rpc.TimestampHolder;
import com.appspot.relaxe.rpc.VarcharHolder;


public abstract class AbstractPrimitiveType<
	T extends PrimitiveType<T>
>
	extends AbstractType<T>
	implements PrimitiveType<T> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 5378281838745232322L;

	protected AbstractPrimitiveType() {
		super();
	}

	@Override
	public final boolean isReferenceType() {	
		return false;
	}
	
	@Override
	public abstract int getSqlType();
			
	public static class SerializableType
		extends AbstractPrimitiveType<SerializableType> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 910829028354006706L;
		
		private int sqlType;
		private String name;
				
		/**
		 * No-argument constructor for GWT Serialization
		 */		
		private SerializableType() {	
		}
		
		public SerializableType(int sqlType, String name) {
			this();
			this.sqlType = sqlType;
			this.name = name;
		}

		@Override
		public int getSqlType() {
			return sqlType;
		}
		
		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public SerializableType self() {
			return this;
		}
	}
	
	public static final class NullHolder<H>
		extends AbstractPrimitiveHolder<Serializable, SerializableType, NullHolder<H>>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1380943877418306324L;
		private SerializableType type;
		
		
		private static final NullHolder<SerializableType> ARRAY = new NullHolder<SerializableType>(AbstractPrimitiveType.ARRAY);
		private static final NullHolder<SerializableType> BIGINT = new NullHolder<SerializableType>(AbstractPrimitiveType.BIGINT);
		private static final NullHolder<SerializableType> BINARY = new NullHolder<SerializableType>(AbstractPrimitiveType.BINARY);
		private static final NullHolder<SerializableType> BIT = new NullHolder<SerializableType>(AbstractPrimitiveType.BIT);
		private static final NullHolder<SerializableType> BLOB = new NullHolder<SerializableType>(AbstractPrimitiveType.BLOB);
		private static final NullHolder<SerializableType> BOOLEAN = new NullHolder<SerializableType>(AbstractPrimitiveType.BOOLEAN);
		private static final NullHolder<SerializableType> CHAR = new NullHolder<SerializableType>(AbstractPrimitiveType.CHAR);
		private static final NullHolder<SerializableType> CLOB = new NullHolder<SerializableType>(AbstractPrimitiveType.CLOB);
		private static final NullHolder<SerializableType> DATALINK = new NullHolder<SerializableType>(AbstractPrimitiveType.DATALINK);
		private static final NullHolder<SerializableType> DATE = new NullHolder<SerializableType>(AbstractPrimitiveType.DATE);
		private static final NullHolder<SerializableType> DECIMAL = new NullHolder<SerializableType>(AbstractPrimitiveType.DECIMAL);
		private static final NullHolder<SerializableType> DISTINCT = new NullHolder<SerializableType>(AbstractPrimitiveType.DISTINCT);
		private static final NullHolder<SerializableType> DOUBLE = new NullHolder<SerializableType>(AbstractPrimitiveType.DOUBLE);
		private static final NullHolder<SerializableType> FLOAT = new NullHolder<SerializableType>(AbstractPrimitiveType.FLOAT);
		private static final NullHolder<SerializableType> INTEGER = new NullHolder<SerializableType>(AbstractPrimitiveType.INTEGER);
		private static final NullHolder<SerializableType> JAVA_OBJECT = new NullHolder<SerializableType>(AbstractPrimitiveType.JAVA_OBJECT);
		private static final NullHolder<SerializableType> LONGNVARCHAR = new NullHolder<SerializableType>(AbstractPrimitiveType.LONGNVARCHAR);
		private static final NullHolder<SerializableType> LONGVARBINARY = new NullHolder<SerializableType>(AbstractPrimitiveType.LONGVARBINARY);
		private static final NullHolder<SerializableType> LONGVARCHAR = new NullHolder<SerializableType>(AbstractPrimitiveType.LONGVARCHAR);
		private static final NullHolder<SerializableType> NCHAR = new NullHolder<SerializableType>(AbstractPrimitiveType.NCHAR);
		private static final NullHolder<SerializableType> NCLOB = new NullHolder<SerializableType>(AbstractPrimitiveType.NCLOB);
		private static final NullHolder<SerializableType> STRUCT = new NullHolder<SerializableType>(AbstractPrimitiveType.STRUCT);
		private static final NullHolder<SerializableType> NULL = new NullHolder<SerializableType>(AbstractPrimitiveType.NULL);
		private static final NullHolder<SerializableType> NUMERIC = new NullHolder<SerializableType>(AbstractPrimitiveType.NUMERIC);
		private static final NullHolder<SerializableType> NVARCHAR = new NullHolder<SerializableType>(AbstractPrimitiveType.NVARCHAR);
		private static final NullHolder<SerializableType> OTHER = new NullHolder<SerializableType>(AbstractPrimitiveType.OTHER);
		private static final NullHolder<SerializableType> REAL = new NullHolder<SerializableType>(AbstractPrimitiveType.REAL);
		private static final NullHolder<SerializableType> REF = new NullHolder<SerializableType>(AbstractPrimitiveType.REF);
		private static final NullHolder<SerializableType> ROWID = new NullHolder<SerializableType>(AbstractPrimitiveType.ROWID);
		private static final NullHolder<SerializableType> SMALLINT = new NullHolder<SerializableType>(AbstractPrimitiveType.SMALLINT);
		private static final NullHolder<SerializableType> SQLXML = new NullHolder<SerializableType>(AbstractPrimitiveType.SQLXML);
		private static final NullHolder<SerializableType> TIME = new NullHolder<SerializableType>(AbstractPrimitiveType.TIME);
		private static final NullHolder<SerializableType> TIMESTAMP = new NullHolder<SerializableType>(AbstractPrimitiveType.TIMESTAMP);
		private static final NullHolder<SerializableType> TINYINT = new NullHolder<SerializableType>(AbstractPrimitiveType.TINYINT);
		private static final NullHolder<SerializableType> VARBINARY = new NullHolder<SerializableType>(AbstractPrimitiveType.VARBINARY);
		private static final NullHolder<SerializableType> VARCHAR = new NullHolder<SerializableType>(AbstractPrimitiveType.VARCHAR);

				
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
			break;
		}		
		
		return nh;
	}
	
	
	@Override
	public ArrayType<?, ?> asArrayType() {
		return null;
	}
	
	
	
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
	
	@Override
	public OtherType<?> asOtherType() {
		return null;
	}
}
