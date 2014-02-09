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
package com.appspot.relaxe.rdbms.pg;

import com.appspot.relaxe.DefaultValueExtractorFactory;
import com.appspot.relaxe.env.pg.PGEnvironment;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.meta.SerializableEnvironment;
import com.appspot.relaxe.rdbms.CatalogFactory;
import com.appspot.relaxe.rdbms.DefaultImplementation;

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
