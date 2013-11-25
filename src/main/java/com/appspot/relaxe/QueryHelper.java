/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryHelper {
	
	private static QueryHelper instance = new QueryHelper();
	
	public static Statement doClose(Statement st) {
		instance.close(st);
		return null;
	}
	
	public static PreparedStatement doClose(PreparedStatement ps) {
		instance.close(ps);
		return null;
	}

	public static ResultSet doClose(ResultSet rs) {
		instance.close(rs);
		return null;
	}
	public Statement close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} 
			catch (SQLException e) {			
				closeError(st, e);
			}
		}
		
		return null;
	}

	public void closeError(Statement st, SQLException e) {		
	}

	public void rollbackError(Connection c, SQLException e) {      
	}
	

	public ResultSet close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} 
			catch (SQLException e) {			
				closeError(rs, e);
			}
		}
		
		return null;
	}

	public void closeError(ResultSet rs, SQLException e) {		
	}

    public static Connection doClose(Connection c) {
        if (c != null) {
            try {
                c.close();
            } 
            catch (SQLException e) {            
            }
        }
        
        return null;        
    }
    
    public static void doRollback(Connection c) {
        instance.rollback(c);        
    }

    
    public void rollback(Connection c) {
        if (c != null) {
            try {
                c.rollback();
            } 
            catch (SQLException e) {            
                rollbackError(c, e);
            }
        }
    }
}
