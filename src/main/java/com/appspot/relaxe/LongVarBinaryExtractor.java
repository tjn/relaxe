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

import com.appspot.relaxe.ent.value.LongVarBinary;
import com.appspot.relaxe.rpc.LongVarBinaryHolder;
import com.appspot.relaxe.types.LongVarBinaryType;


public class LongVarBinaryExtractor
	extends ValueExtractor<LongVarBinary, LongVarBinaryType, LongVarBinaryHolder>
{
	public LongVarBinaryExtractor(int column) {
		super(column);			
	}

	@Override
	public LongVarBinaryHolder extractValue(ResultSet rs) throws SQLException {
		byte[] content = rs.getBytes(getColumn());
		
		if (content == null) {
			return LongVarBinaryHolder.NULL_HOLDER;
		}		
		
		LongVarBinary blob = new LongVarBinary.Builder(content).newLongVarBinary();
		LongVarBinaryHolder holder = LongVarBinaryHolder.valueOf(blob);		
		return holder;
	}
}