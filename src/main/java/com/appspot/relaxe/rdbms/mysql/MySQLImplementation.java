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
package com.appspot.relaxe.rdbms.mysql;

import java.util.Properties;

import com.appspot.relaxe.env.mysql.MySQLEnvironment;
import com.appspot.relaxe.env.mysql.MySQLSyntax;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.rdbms.CatalogFactory;
import com.appspot.relaxe.rdbms.DefaultImplementation;
import com.appspot.relaxe.rdbms.PersistenceContext;

/**
 * @author Administrator
 *
 */
public class MySQLImplementation
	extends DefaultImplementation<MySQLImplementation> {

	private MySQLSyntax syntax;
    
	@Override
	public CatalogFactory catalogFactory() {
		return new MySQLCatalogFactory(this.environment());
	}

    @Override
    public String defaultDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public SQLSyntax getSyntax() {
        if (syntax == null) {
            syntax = new MySQLSyntax();
        }

        return syntax;
    }

    @Override
	public String createJdbcUrl(String database) {
    	return createJdbcUrl(null, database);
    }

	@Override
	public String createJdbcUrl(String host, String database) {
		host = (host == null) ? "" : host;
		return "jdbc:mysql://" + host + "/" + database;

	}

	@Override
	public String createJdbcUrl(String host, Integer port, String database) {
		host = (host == null) ? "" : host;
		return "jdbc:mysql://" + host + ":" + port + "/" + database;
	}
	
	@Override
	public MySQLEnvironment environment() {
		return MySQLEnvironment.environment();
	}
	
	@Override
	public MySQLImplementation self() {
		return this;
	}
	
	@Override
	public Properties getProperties() {
		Properties cfg = super.getProperties();
		
		cfg.setProperty("nullCatalogMeansCurrent", "false");
		cfg.setProperty("nullNamePatternMatchesAll", "false");
		cfg.setProperty("sessionVariables", "sql_mode='ANSI'");
		
		return cfg;
	}

	@Override
	public PersistenceContext<MySQLImplementation> newDefaultPersistenceContext() {
		return new MySQLPersistenceContext(this);
	}
	
	
}
