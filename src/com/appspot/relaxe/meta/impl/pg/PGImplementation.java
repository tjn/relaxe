/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import com.appspot.relaxe.DefaultValueExtractorFactory;
import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultImplementation;
import com.appspot.relaxe.env.pg.PGEnvironment;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.meta.SerializableEnvironment;

public class PGImplementation
	extends DefaultImplementation<PGImplementation> {

	private SQLSyntax syntax;    
    private PGEnvironment environment;
    
	public PGImplementation() {
		environment = PGEnvironment.environment();
	}

	@Override
	public CatalogFactory catalogFactory() {
		return new PGCatalogFactory(this.environment);
	}
    
    @Override
    public String defaultDriverClassName() {
        return "org.postgresql.Driver";
    }


    public static class PGSyntax
        extends DefaultSQLSyntax {

    }

    @Override
    public SQLSyntax getSyntax() {
        if (syntax == null) {
            syntax = new PGSyntax();
        }

        return syntax;
    }

	public PGEnvironment getEnvironment() {
		return environment;
	}
	
	@Override
	protected DefaultValueExtractorFactory createValueExtractorFactory() {
		return new PGValueExtractorFactory();
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
		
		if (host == null) {
			host = "127.0.0.1"; 
		}
		
		StringBuilder buf = new StringBuilder();
		
		buf.append("jdbc:postgresql://");
		buf.append(host);
		
		if (port != null) {
			buf.append(":");	
			buf.append(port.intValue());
		}		
		
		buf.append("/");		
		buf.append(database);
		
		return buf.toString();
	}
	
	@Override
	public SerializableEnvironment environment() {
		return this.environment;		
	}


	@Override
	public PGImplementation self() {
		return this;
	}
}
