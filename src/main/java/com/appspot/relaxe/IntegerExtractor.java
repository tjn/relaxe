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
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.value.IntegerHolder;


public class IntegerExtractor
	extends ValueExtractor<Integer, IntegerType, IntegerHolder>
{	
	private static final IntegerExtractor[] PERMANENT = new IntegerExtractor[10];
	
	public static IntegerExtractor forColumn(int column) {
		int index = column - 1;
		
		IntegerExtractor e = (index >= 0 && index < PERMANENT.length) ? PERMANENT[index] : new IntegerExtractor(column);
		
		if (e == null) {
			PERMANENT[index] = e = new IntegerExtractor(column);			
		}
		
		return e;		
	}
	
	public IntegerExtractor(int column) {
		super(column);			
	}

	@Override
	public IntegerHolder extractValue(ResultSet rs) throws SQLException {
		int v = rs.getInt(getColumn());
		return rs.wasNull() ? IntegerHolder.NULL_HOLDER : IntegerHolder.valueOf(v);			
	}
}