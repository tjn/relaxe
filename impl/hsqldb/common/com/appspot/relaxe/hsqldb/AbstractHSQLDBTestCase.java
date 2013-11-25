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
package com.appspot.relaxe.hsqldb;

import java.io.IOException;
import java.util.Properties;

import com.appspot.relaxe.AbstractUnitTest;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.hsqldb.HSQLDBImplementation;
import com.appspot.relaxe.env.hsqldb.HSQLDBPersistenceContext;


public abstract class AbstractHSQLDBTestCase
	extends AbstractUnitTest<HSQLDBImplementation> {
	
	@Override
	protected PersistenceContext<HSQLDBImplementation> createPersistenceContext() {
		return new HSQLDBPersistenceContext();
	}
			
	@Override
	protected Properties getJdbcConfig() throws IOException {		
		return getJdbcConfigForDatabase();
	}

	@Override
	protected String implementationTag() {
		return "hsqldb";
	}
}