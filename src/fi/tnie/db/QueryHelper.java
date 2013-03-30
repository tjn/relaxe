/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

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
