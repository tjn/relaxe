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
package com.appspot.relaxe.env.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.query.QueryException;


public abstract class AbstractQueryProcessor 
	extends QueryProcessorAdapter { 
	
	public void apply(ResultSet rs) 
		throws QueryException, SQLException {
		apply(rs, true);
	}
	
	public void apply(ResultSet rs, boolean close) 
		throws QueryException, SQLException {
	
		prepare();
		
		long ordinal = 0;
											
		try {							
			startResultSet(rs.getMetaData());
						
			while(rs.next()) {
				process(rs, ++ordinal);
			}			
	
			endResultSet();
		}
		catch (Exception e) {
			abort(e);			
		}
		finally {
			if (close) {
				QueryHelper.doClose(rs);
			}
			
			finish();
		}
	}

	@Override
	public void prepare() {
	}

	@Override
	public void finish() {
	}

	@Override
	public void process(ResultSet rs, long ordinal)
			throws QueryException, SQLException {
	}

	@Override
	public void startResultSet(ResultSetMetaData m)
			throws QueryException, SQLException {
	}

	@Override
	public void endResultSet() throws QueryException {
	}
}
