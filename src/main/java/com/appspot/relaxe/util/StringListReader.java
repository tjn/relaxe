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
package com.appspot.relaxe.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class StringListReader extends AbstractQueryProcessor {
		
	private Collection<String> destination;
	private int column;
	
	private int rowCount = 0; 
	
	public StringListReader(Collection<String> dest) {
		this(dest, 1);
	}
	
	public StringListReader(Collection<String> dest, int column) {
		super();
		
		if (dest == null) {
			throw new NullPointerException("'dest' must not be null");
		}
		
		this.destination = dest;
		this.column = column;
	}
	
	@Override
	public void prepare() {
	    rowCount = 0;	
	}
	

	@Override
	public void process(ResultSet rs, long ordinal) throws SQLException {
		String value = rs.getString(column);
		this.destination.add(value);
		rowCount++;
	}
	
	public int getRowCount() {
        return rowCount;
    }
}
