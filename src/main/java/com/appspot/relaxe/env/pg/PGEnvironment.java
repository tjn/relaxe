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
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.env.SerializableEnvironment;
import com.appspot.relaxe.expr.DateLiteral;
import com.appspot.relaxe.expr.IntLiteral;
import com.appspot.relaxe.expr.LongLiteral;
import com.appspot.relaxe.expr.NiladicFunction;
import com.appspot.relaxe.expr.StringLiteral;
import com.appspot.relaxe.expr.TimestampLiteral;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.types.ValueType;

public class PGEnvironment
	implements SerializableEnvironment {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 6323320738822722962L;
	
	private static PGEnvironment environment = new PGEnvironment();
	private transient PGIdentifierRules identifierRules;
	private transient DataTypeMap dataTypeMap;
	
	public static PGEnvironment environment() {
		return PGEnvironment.environment;
	}

	protected PGEnvironment() {
	}    
	
	@Override
	public PGIdentifierRules getIdentifierRules() {
		if (identifierRules == null) {
			identifierRules = new PGIdentifierRules();
			
		}
		
		return identifierRules;
	}
	
	@Override
	public DefaultDefinition newDefaultDefinition(Column col) {
		return newDefaultDefinition(col.getColumnDefault(), col.getDataType().getDataType());
	}
	
	public DefaultDefinition newDefaultDefinition(String defval, int type) {
		if (defval == null) {
			return DefaultDefinition.NULL;
		}
		
		DefaultDefinition def = null;	
		
		int cop = defval.lastIndexOf("::");
		
		String head = (cop < 0) ? defval : defval.substring(0, cop);
		
		int t = type;
		
		if (SQLDataType.isTextType(t)) {
			boolean lit = (head.length() > 0) && (head.charAt(0) == '\'');

			if (lit) {
				def = new DefaultDefinition(new StringLiteral(head, true));	
			}
			else {
				// function call? not supported
			}
			
			return def;
		}
		
		if (SQLDataType.isBinaryIntegerType(t)) {
			boolean lit = head.matches("[+-]?[0-9]+");
			
			if (lit) {
				def = new DefaultDefinition(
						(t == ValueType.BIGINT) ? 
						new LongLiteral(Long.parseLong(head)) : 		
						new IntLiteral(Integer.parseInt(head)));	
			}
			else {
				// function call? not supported
			}	
			
			return def;
		}
		
		if (t == ValueType.DATE) {
			boolean lit = head.matches("'[0-9]{4}-[0-9]{2}-[0-9]{2}'");
			
			if (lit) {
				// 'yyyy-MM-dd'
				int yyyy = Integer.parseInt(head.substring(1, 5));				
				int mm = Integer.parseInt(head.substring(6, 8));
				int dd = Integer.parseInt(head.substring(9, 11));				
				def = new DefaultDefinition(new DateLiteral(yyyy, mm, dd));	
			}			
			else {
				// ('now'::text)::date				
				if (defval.equals("('now'::text)::date")) {
					def = new DefaultDefinition(NiladicFunction.CURRENT_DATE);
				}
			}
			
			return def;
		}
		
		if (t == ValueType.TIMESTAMP) {
			boolean lit = head.matches("'[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}(\\.[0-9]+)?'");
			
			if (lit) {
				// 'yyyy-MM-dd HH:mm:ss.SSS'
				int yyyy = Integer.parseInt(head.substring(1, 5));				
				int mm = Integer.parseInt(head.substring(6, 8));
				int dd = Integer.parseInt(head.substring(9, 11));				
				
				int h = Integer.parseInt(head.substring(12, 14));
				int m = Integer.parseInt(head.substring(15, 17));
				int s = Integer.parseInt(head.substring(18, 20));
				
				int sss = 0;
				int prec = 0;
				
				int hl = head.length();
				
				if (hl > 21) {
					prec = hl - 22;
					sss = Integer.parseInt(head.substring(21, hl - 1));
				}				
				
				def = new DefaultDefinition(new TimestampLiteral(yyyy, mm, dd, h, m, s, sss, prec));	
			}			
			else {
				// now()				
				if (defval.equals("now()")) {
					def = new DefaultDefinition(NiladicFunction.CURRENT_TIMESTAMP);
				}
			}
			
			return def;
		}
		
		return null;
	}

	@Override
	public DataTypeMap getDataTypeMap() {
		if (dataTypeMap == null) {
			dataTypeMap = new PGDataTypeMap();			
		}
		
		return dataTypeMap;
	}
}
