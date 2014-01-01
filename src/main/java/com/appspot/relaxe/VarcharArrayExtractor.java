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

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.rpc.VarcharArrayHolder;
import com.appspot.relaxe.types.VarcharArrayType;


public class VarcharArrayExtractor
	extends ValueExtractor<StringArray, VarcharArrayType, VarcharArrayHolder>
{	
	
	public VarcharArrayExtractor(int column) {
		super(column);			
	}

	@Override
	public VarcharArrayHolder extractValue(ResultSet rs) throws SQLException {
		Array array = rs.getArray(getColumn());
		
		if (array == null) {
			return VarcharArrayHolder.NULL_HOLDER;
		}


		ResultSet as = null;
		List<String> elems = new ArrayList<String>();

		try {			
			as = array.getResultSet();
						
			while (as.next()) {
				String elem = as.getString(2);
				elems.add(elem);
			}
		}
		finally {
			as.close();
		}	
		
		// TODO: probably fails on PG, hide behind interface
		array.free();
		
		StringArray av = new StringArray(elems);		
		return new VarcharArrayHolder(av);			
	}
}