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

import com.appspot.relaxe.value.AbstractValueHolder;
import com.appspot.relaxe.value.CharHolder;
import com.appspot.relaxe.value.DateHolder;
import com.appspot.relaxe.value.IntegerHolder;
import com.appspot.relaxe.value.LongHolder;
import com.appspot.relaxe.value.TimeHolder;
import com.appspot.relaxe.value.TimestampHolder;
import com.appspot.relaxe.value.ValueHolder;
import com.appspot.relaxe.value.VarcharHolder;


public abstract class AbstractValueType<
	T extends ValueType<T>
>
	extends AbstractType<T>
	implements ValueType<T> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 5378281838745232322L;

	protected AbstractValueType() {
		super();
	}

	@Override
	public final boolean isReferenceType() {	
		return false;
	}
	
	@Override
	public abstract int getSqlType();
			
	public static class SerializableType
		extends AbstractValueType<SerializableType> {
		
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
		
		
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			
			if (!(obj instanceof SerializableType)) {
				return false;
			}
			
			SerializableType t = (SerializableType) obj;		
									
			if (this.sqlType != t.getSqlType()) {
				return false;
			}
			
			if (this.name == null) {
				return (t.getName() == null);
			}
			
			return this.name.equals(t.getName());
		}
	}
	
	
	protected boolean contentEquals(T type) {
		if (getSqlType() != type.getSqlType()) {
			return false;
		}
		
		String n1 = getName();
		String n2 = type.getName();
		
		return (n1 == null) ? (n2 == null) : n1.equals(n2);
	}
	
	
	
	@Override
	public int hashCode() {
		int t = getSqlType();
		String n = getName();		
		return t ^ ((n == null) ? 0 : n.hashCode());
	}
	
	
	public static final class NullHolder<H>
		extends AbstractValueHolder<Serializable, SerializableType, NullHolder<H>>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1380943877418306324L;
		private SerializableType type;
		
		
		private static final NullHolder<SerializableType> ARRAY = new NullHolder<SerializableType>(AbstractValueType.ARRAY);
		private static final NullHolder<SerializableType> BIGINT = new NullHolder<SerializableType>(AbstractValueType.BIGINT);
		private static final NullHolder<SerializableType> BINARY = new NullHolder<SerializableType>(AbstractValueType.BINARY);
		private static final NullHolder<SerializableType> BIT = new NullHolder<SerializableType>(AbstractValueType.BIT);
		private static final NullHolder<SerializableType> BLOB = new NullHolder<SerializableType>(AbstractValueType.BLOB);
		private static final NullHolder<SerializableType> BOOLEAN = new NullHolder<SerializableType>(AbstractValueType.BOOLEAN);
		private static final NullHolder<SerializableType> CHAR = new NullHolder<SerializableType>(AbstractValueType.CHAR);
		private static final NullHolder<SerializableType> CLOB = new NullHolder<SerializableType>(AbstractValueType.CLOB);
		private static final NullHolder<SerializableType> DATALINK = new NullHolder<SerializableType>(AbstractValueType.DATALINK);
		private static final NullHolder<SerializableType> DATE = new NullHolder<SerializableType>(AbstractValueType.DATE);
		private static final NullHolder<SerializableType> DECIMAL = new NullHolder<SerializableType>(AbstractValueType.DECIMAL);
		private static final NullHolder<SerializableType> DISTINCT = new NullHolder<SerializableType>(AbstractValueType.DISTINCT);
		private static final NullHolder<SerializableType> DOUBLE = new NullHolder<SerializableType>(AbstractValueType.DOUBLE);
		private static final NullHolder<SerializableType> FLOAT = new NullHolder<SerializableType>(AbstractValueType.FLOAT);
		private static final NullHolder<SerializableType> INTEGER = new NullHolder<SerializableType>(AbstractValueType.INTEGER);
		private static final NullHolder<SerializableType> JAVA_OBJECT = new NullHolder<SerializableType>(AbstractValueType.JAVA_OBJECT);
		private static final NullHolder<SerializableType> LONGNVARCHAR = new NullHolder<SerializableType>(AbstractValueType.LONGNVARCHAR);
		private static final NullHolder<SerializableType> LONGVARBINARY = new NullHolder<SerializableType>(AbstractValueType.LONGVARBINARY);
		private static final NullHolder<SerializableType> LONGVARCHAR = new NullHolder<SerializableType>(AbstractValueType.LONGVARCHAR);
		private static final NullHolder<SerializableType> NCHAR = new NullHolder<SerializableType>(AbstractValueType.NCHAR);
		private static final NullHolder<SerializableType> NCLOB = new NullHolder<SerializableType>(AbstractValueType.NCLOB);
		private static final NullHolder<SerializableType> STRUCT = new NullHolder<SerializableType>(AbstractValueType.STRUCT);
		private static final NullHolder<SerializableType> NULL = new NullHolder<SerializableType>(AbstractValueType.NULL);
		private static final NullHolder<SerializableType> NUMERIC = new NullHolder<SerializableType>(AbstractValueType.NUMERIC);
		private static final NullHolder<SerializableType> NVARCHAR = new NullHolder<SerializableType>(AbstractValueType.NVARCHAR);
		private static final NullHolder<SerializableType> OTHER = new NullHolder<SerializableType>(AbstractValueType.OTHER);
		private static final NullHolder<SerializableType> REAL = new NullHolder<SerializableType>(AbstractValueType.REAL);
		private static final NullHolder<SerializableType> REF = new NullHolder<SerializableType>(AbstractValueType.REF);
		private static final NullHolder<SerializableType> ROWID = new NullHolder<SerializableType>(AbstractValueType.ROWID);
		private static final NullHolder<SerializableType> SMALLINT = new NullHolder<SerializableType>(AbstractValueType.SMALLINT);
		private static final NullHolder<SerializableType> SQLXML = new NullHolder<SerializableType>(AbstractValueType.SQLXML);
		private static final NullHolder<SerializableType> TIME = new NullHolder<SerializableType>(AbstractValueType.TIME);
		private static final NullHolder<SerializableType> TIMESTAMP = new NullHolder<SerializableType>(AbstractValueType.TIMESTAMP);
		private static final NullHolder<SerializableType> TINYINT = new NullHolder<SerializableType>(AbstractValueType.TINYINT);
		private static final NullHolder<SerializableType> VARBINARY = new NullHolder<SerializableType>(AbstractValueType.VARBINARY);
		private static final NullHolder<SerializableType> VARCHAR = new NullHolder<SerializableType>(AbstractValueType.VARCHAR);

				
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

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			
			if (!(obj instanceof NullHolder)) {
				return false;
			}
					
			NullHolder<?> nh = (NullHolder<?>) obj;			
			return this.type.equals(nh.getType());			
		}
		
		@Override
		public int hashCode() {
			return this.type.hashCode();
		}
	}
		
	public static ValueHolder<?, ?, ?> nullHolder(int t) {
		ValueHolder<?, ?, ?> nh = null;
		
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
