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
package com.appspot.relaxe.rdbms.hsqldb;

import com.appspot.relaxe.env.SerializableEnvironment;
import com.appspot.relaxe.env.hsqldb.HSQLDBEnvironment;
import com.appspot.relaxe.env.hsqldb.expr.HSQLDBArrayTypeDefinition;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;
import com.appspot.relaxe.rdbms.CatalogFactory;
import com.appspot.relaxe.rdbms.DefaultImplementation;

public abstract class AbstractHSQLDBImplementation
	extends DefaultImplementation<HSQLDBImplementation>
	implements HSQLDBImplementation {

	private SQLSyntax syntax;
	
	private static AbstractHSQLDBImplementation[] alternatives = new AbstractHSQLDBImplementation[] {
		new HSQLDBFileImplementation(),
		new HSQLDBMemImplementation(),
		new HSQLImplementation(),
		new HSQLSImplementation(),
		new HTTPImplementation(),
		new HTTPSImplementation()
	};
	
	public AbstractHSQLDBImplementation() {
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
    	public SQLDataType newArrayTypeDefinition(SQLDataType elementType) {
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

	@Override
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
		return null;
	}
	
	
	public static HSQLDBImplementation resolve(String jdbcURL) {
		if (!jdbcURL.startsWith("jdbc:hsqldb:")) {
			return null;
		}
		
		String tail = jdbcURL.substring("jdbc:hsqldb:".length());
				
		for (AbstractHSQLDBImplementation hi : alternatives) {
			if (tail.startsWith(hi.subprotocol())) {
				return hi;
			}
		}		
		
		return null;
	}
	
}
