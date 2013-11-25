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
package com.appspot.relaxe.env.hsqldb;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultImplementation;
import com.appspot.relaxe.env.hsqldb.expr.HSQLDBArrayTypeDefinition;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.meta.SerializableEnvironment;
import com.appspot.relaxe.meta.impl.hsqldb.HSQLDBEnvironment;

public abstract class HSQLDBImplementation
	extends DefaultImplementation<HSQLDBImplementation> {

	private SQLSyntax syntax;
	
	public HSQLDBImplementation() {
	}

	@Override
	public CatalogFactory catalogFactory() {		
		return new HSQLDBCatalogFactory(HSQLDBEnvironment.environment());
	}

    @Override
    public String defaultDriverClassName() {
        return "org.hsqldb.jdbcDriver";
    }

    public static class HSQLDBSyntax
        extends DefaultSQLSyntax {
    	
    	@Override
    	public SQLTypeDefinition newArrayTypeDefinition(SQLTypeDefinition elementType) {
    		return new HSQLDBArrayTypeDefinition(elementType, null);
    	}
    }

    @Override
    public SQLSyntax getSyntax() {
        if (syntax == null) {
            syntax = new HSQLDBSyntax();
        }

        return syntax;
    }

	public HSQLDBEnvironment getEnvironment() {
		return HSQLDBEnvironment.environment();
	}
	
	@Override
	public String createJdbcUrl(String database) {
		return createJdbcUrl(null, database);		
	}
	
	@Override
	public String createJdbcUrl(String host, String database) {
		return createJdbcUrl(host, null, database);		
	}
	
	@Override
	public String createJdbcUrl(String host, Integer port, String database) {
		if (database == null) {
			throw new NullPointerException("database");
		}
		
		StringBuilder buf = new StringBuilder();
		
		buf.append("jdbc:hsqldb:");
		buf.append(subprotocol());
		buf.append(":");
		buf.append(database);
		
		return buf.toString();
	}
	
	
	public abstract String subprotocol();
	
	@Override
	public SerializableEnvironment environment() {
		return HSQLDBEnvironment.environment();		
	}
	
	@Override
	public HSQLDBImplementation self() {
		return this;
	}
	
	
	public static class Mem
		extends HSQLDBImplementation {

		@Override
		public String subprotocol() {
			return "mem";
		}
	}
	
	public static class File
		extends HSQLDBImplementation {

		@Override
		public String subprotocol() {
			return "file";
		}
	}
	
	public static abstract class ServerImplementation
		extends HSQLDBImplementation {	
		
		@Override
		public String createJdbcUrl(String host, Integer port, String database) {		
			if (database == null) {
				throw new NullPointerException("database");
			}
			
			StringBuilder buf = new StringBuilder();
			
			buf.append("jdbc:hsqldb:");
			buf.append(subprotocol());
			buf.append("//");
			buf.append(host);
			if (port != null) {
				buf.append(":");	
				buf.append(port.intValue());
			}
			buf.append("/");
			buf.append(database);
			
			return buf.toString();		
		}		
	}
	
	public static class HTTP
		extends ServerImplementation {

		@Override
		public String subprotocol() {
			return "http";
		}		
	}
	
	public static class HTTPS
		extends ServerImplementation {
	
		@Override
		public String subprotocol() {
			return "https";
		}		
	}

	public static class HSQL
		extends ServerImplementation {
	
		@Override
		public String subprotocol() {
			return "hsql";
		}		
	}

	public static class HSQLS
		extends ServerImplementation {
	
		@Override
		public String subprotocol() {
			return "hsqls";
		}		
	}	 
	
}
