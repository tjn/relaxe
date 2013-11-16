/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
