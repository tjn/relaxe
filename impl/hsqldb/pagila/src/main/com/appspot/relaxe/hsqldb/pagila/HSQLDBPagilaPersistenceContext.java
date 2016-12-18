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
package com.appspot.relaxe.hsqldb.pagila;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ArrayAssignment;
import com.appspot.relaxe.AssignContext;
import com.appspot.relaxe.ParameterAssignment;
import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.common.pagila.types.YearType;
import com.appspot.relaxe.env.IdentifierRules;
import com.appspot.relaxe.env.hsqldb.HSQLDBTextArrayHolder;
import com.appspot.relaxe.env.hsqldb.HSQLDBTextArrayType;
import com.appspot.relaxe.env.pg.PGTextType;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;
import com.appspot.relaxe.expr.ddl.types.SQLVarBinaryType;
import com.appspot.relaxe.expr.ddl.types.SQLVarcharType;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.DefaultDataTypeMap;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBPersistenceContext;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.VarcharType;
import com.appspot.relaxe.value.ArrayHolder;
import com.appspot.relaxe.value.ArrayValue;
import com.appspot.relaxe.value.StringArray;
import com.appspot.relaxe.value.ValueHolder;

public class HSQLDBPagilaPersistenceContext
	extends HSQLDBPersistenceContext {
	
	private static Logger logger = LoggerFactory.getLogger(HSQLDBPagilaPersistenceContext.class);
		
	private ValueAssignerFactory assignerFactory;

	public HSQLDBPagilaPersistenceContext() {
	}	
	
	@Override
	protected DataTypeMap newDataTypeMap() {
		final DataTypeMap tm = super.newDataTypeMap();
		final HSQLDBImplementation imp = getImplementation();								
		final DataTypeMap ptm = new HSQDBPagilaDataTypeMap(tm, imp);	
		
		return ptm;
	}
	
	
	
	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		if (assignerFactory == null) {
			ValueAssignerFactory vaf = super.getValueAssignerFactory();
			assignerFactory = new HSQLDBPagilaValueAssignerFactory(vaf);			
		}
		
		return assignerFactory;
	}
	
	private final class HSQDBPagilaDataTypeMap extends DefaultDataTypeMap {
		private final DataTypeMap tm;
		private final HSQLDBImplementation imp;

		private HSQDBPagilaDataTypeMap(DataTypeMap tm, HSQLDBImplementation imp) {
			this.tm = tm;
			this.imp = imp;
		}

		@Override
		public ValueType<?> getType(DataType type) {
			int t = type.getDataType();
			String n = type.getTypeName();
			
			if (t == ValueType.DISTINCT && YearType.TYPE.getName().equals(n)) {
				return YearType.TYPE;											
			}
			
			if (t == ValueType.OTHER && "tsvector".equals(n)) {
				return VarcharType.TYPE;											
			}				
			
			if (t == ValueType.VARCHAR && "text".equals(n)) {
				return PGTextType.TYPE;											
			}
			
			return tm.getType(type);
		}

		@Override
		public SQLDataType getSQLType(DataType type) {
			SQLDataType def = tm.getSQLType(type);
			
			if (def == null) {
				int t = type.getDataType();
				
				if (t == ValueType.ARRAY && type.getTypeName().equals("_text")) {
					def = imp.getSyntax().newArrayType(SQLVarcharType.get(1024));
				}
				
				if (t == ValueType.BINARY && type.getTypeName().equals("bytea")) {												
					def = SQLVarBinaryType.get(type.getSize());
				}
				
				if (SQLDataType.isBinaryType(t)) {
					def = SQLVarBinaryType.get(type.getSize());
				}
				
				if (t == ValueType.DISTINCT) {
					IdentifierRules ir = imp.environment().getIdentifierRules();
					def = newUserDefinedType(ir, type);						
				}
				
				String n = type.getTypeName();
				
				if (t == ValueType.OTHER && "tsvector".equals(n)) {
					def = SQLVarcharType.get(1024);
				}
									
				if (t == ValueType.VARCHAR && "text".equals(n)) {
					def = SQLVarcharType.get(1000000);									
				}
			}
			
			if (def == null) {
				logger.debug("unmapped: " + type.getTypeName() + ": " + type.getDataType());
			}								
			
			return def;
		}
	}

	private final class HSQLDBPagilaValueAssignerFactory 
		implements ValueAssignerFactory {
		
		private ValueAssignerFactory defaultFactory;
		
		public HSQLDBPagilaValueAssignerFactory(ValueAssignerFactory vaf) {
			defaultFactory = vaf;
		}

		@Override
		public <T extends ValueType<T>, H extends ValueHolder<?, T, H>> ParameterAssignment create(H ph, DataType type, AssignContext actx) {
			
			ParameterAssignment pa = defaultFactory.create(ph, type, actx);
			
			if (pa == null) {				
				if (ph.getSqlType() == ValueType.ARRAY) {
					ArrayHolder<?, ?, ?, ?, ?> ah = ph.asArrayHolder();
					
					if (ah != null) {
						HSQLDBTextArrayHolder hah = HSQLDBTextArrayHolder.NULL_HOLDER; 
						
						if (!ah.isNull()) {
							ArrayValue<?> av = ah.value();
							int size = av.size();
							String[] content = new String[size];
							
							for (int i = 0; i < size; i++) {
								Object elem = av.get(i);
								content[i] = (elem == null) ? null : av.toString();
							}
							
							StringArray sa = new StringArray(content);
							hah = HSQLDBTextArrayHolder.valueOf(sa);
						}
						
						pa = new HSQLDBTextArrayAssignment(hah);
					}
				}
			}
			
			return pa;
		}
	}
	
	private static class HSQLDBTextArrayAssignment
		extends ArrayAssignment<String, VarcharType, StringArray, HSQLDBTextArrayType, HSQLDBTextArrayHolder> {
	
		public HSQLDBTextArrayAssignment(HSQLDBTextArrayHolder value) {
			super(value);
		}
				
		@Override
		protected HSQLDBTextArrayType getType() {
			return HSQLDBTextArrayType.TYPE;
		}		
	}
}
