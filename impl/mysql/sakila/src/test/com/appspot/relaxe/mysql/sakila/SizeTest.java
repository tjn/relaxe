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
package com.appspot.relaxe.mysql.sakila;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SizeTest
	extends AbstractMySQLSakilaTestCase {

	
	public void testSize1() throws Exception {
		Connection c = newConnection();
		Statement st = null;
		
	    try {
	    	
	    	
	    	st = c.createStatement();
	    	
	        st.execute("CREATE TABLE  IF NOT EXISTS conj73 (t tinyint(1))");
	        st.execute("insert into conj73 values(1)");
	        ResultSet rs = c.getMetaData().getColumns(c.getCatalog(), null, "conj73", "t");	        	        
	        assertTrue(rs.next());	        	        
	        assertEquals(1, rs.getInt("COLUMN_SIZE"));
	    } 
	    finally {
	    	close(st);
	    	close(c);	        
	    }		
		
		c.close();
	}

	public void testSize2() throws Exception {
		Connection c = newConnection();
		Statement st = null;
		
	    try {
	    	st = c.createStatement();
	    	
	        st.execute("CREATE TABLE  IF NOT EXISTS conj73 (t tinyint(2))");
	        st.execute("insert into conj73 values(22)");
	        ResultSet rs = c.getMetaData().getColumns(c.getCatalog(), null, "conj73", "t");	        	        
	        assertTrue(rs.next());	        	    
	        
	        int size = rs.getInt("COLUMN_SIZE");
	        boolean wasNull = rs.wasNull();
	        assertFalse(wasNull);
	        
	        assertEquals(2, size);
	    } 
	    finally {
	    	close(st);
	    	close(c);	        
	    }		
		
		c.close();
	}
	
	public void testSize4() throws Exception {
		Connection c = newConnection();
		Statement st = null;
		
	    try {
	    	st = c.createStatement();
	    	
	        st.execute("CREATE TABLE  IF NOT EXISTS conj73 (t tinyint(4))");
	        st.execute("insert into conj73 values(22)");
	        ResultSet rs = c.getMetaData().getColumns(c.getCatalog(), null, "conj73", "t");	        	        
	        assertTrue(rs.next());	        	        
	        assertEquals(4, rs.getInt("COLUMN_SIZE"));
	    } 
	    finally {
	    	close(st);
	    	close(c);	        
	    }		
		
		c.close();
	}
	
	public void testIntSize2() throws Exception {
		Connection c = newConnection();
		Statement st = null;
		
	    try {
	    	st = c.createStatement();
	    	
	        st.execute("CREATE TABLE  IF NOT EXISTS conj73 (t int(2))");
	        st.execute("insert into conj73 values(22)");
	        ResultSet rs = c.getMetaData().getColumns(c.getCatalog(), null, "conj73", "t");	        	        
	        assertTrue(rs.next());	        	        
	        assertEquals(2, rs.getInt("COLUMN_SIZE"));
	    } 
	    finally {
	    	close(st);
	    	close(c);	        
	    }		
		
		c.close();
	}

	
	
	
}
