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
package com.appspot.relaxe.rdbms;

import com.appspot.relaxe.rdbms.hsqldb.AbstractHSQLDBImplementation;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBPersistenceContext;
import com.appspot.relaxe.rdbms.mariadb.MariaDBImplementation;
import com.appspot.relaxe.rdbms.mariadb.MariaDBPersistenceContext;
import com.appspot.relaxe.rdbms.mysql.MySQLImplementation;
import com.appspot.relaxe.rdbms.mysql.MySQLPersistenceContext;
import com.appspot.relaxe.rdbms.pg.PGImplementation;
import com.appspot.relaxe.rdbms.pg.PGPersistenceContext;

public class DefaultResolver
	implements ImplementationResolver, PersistenceContextResolver {

	@Override
	public Implementation<?> resolve(String jdbcURL) {
		if (jdbcURL == null) {
			throw new NullPointerException("jdbcURL");
		}
						
		if (jdbcURL.startsWith("jdbc:postgresql:")) {
			return new PGImplementation();
		}
		
		if (jdbcURL.startsWith("jdbc:mariadb:")) {
			return new MariaDBImplementation();
		}
		
		if (jdbcURL.startsWith("jdbc:mysql:")) {
			return new MySQLImplementation();
		}
		
		if (jdbcURL.startsWith("jdbc:hsqldb:")) {
			return AbstractHSQLDBImplementation.resolve(jdbcURL);
		}		
		
		return null;
	}

	@Override
	public PersistenceContext<?> resolveDefaultContext(String jdbcURL) {
		if (jdbcURL == null) {
			throw new NullPointerException("jdbcURL");
		}
				
		if (jdbcURL.startsWith("jdbc:postgresql:")) {
			return new PGPersistenceContext();
		}
		
		if (jdbcURL.startsWith("jdbc:mariadb:")) {
			return new MariaDBPersistenceContext();
		}
		
		if (jdbcURL.startsWith("jdbc:mysql:")) {
			return new MySQLPersistenceContext();
		}
		
		if (jdbcURL.startsWith("jdbc:hsqldb:")) {
			HSQLDBImplementation hi = AbstractHSQLDBImplementation.resolve(jdbcURL);
			
			if (hi != null) {
				return new HSQLDBPersistenceContext(hi);
			}
		}		
		
		return null;
	}

}
