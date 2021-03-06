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
package com.appspot.relaxe.pg.pagila.hsqldb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.hsqldb.pagila.HSQLDBPagilaPersistenceContext;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.impl.hsqldb.HSQLDBTest;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.rdbms.hsqldb.AbstractHSQLDBImplementation;
// import com.appspot.relaxe.rdbms.hsqldb.HSQLDBFileImplementation;
import com.appspot.relaxe.rdbms.hsqldb.HSQLDBMemImplementation;
import com.appspot.relaxe.rdbms.pg.PGImplementation;

public class PagilaHSQLDBPortGeneratorTest
	extends AbstractPagilaTestCase {
	
	private static Logger logger = LoggerFactory.getLogger(PagilaHSQLDBPortGeneratorTest.class);
		
	public PagilaHSQLDBPortGeneratorTest() {		
	}
	
	public void testTransform() throws Exception {
		logger.debug("testTransform - enter");
		
		try {						
			TestContext<PGImplementation> tc = getCurrent();
			Catalog cat = tc.getCatalog();
			transform(cat);
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		finally {
			logger.debug("testTransform - exit");
		}		
	}
	
	
	public void transform(Catalog cat) throws Exception {
		
//		final AbstractHSQLDBImplementation hi = new HSQLDBFileImplementation();
		final AbstractHSQLDBImplementation hi = new HSQLDBMemImplementation();
		String tdc = hi.defaultDriverClassName();
		
		if (tdc != null) {
			Class.forName(tdc);
		}				
		
		HSQLDBPagilaPersistenceContext hpc = new HSQLDBPagilaPersistenceContext();
		
		TestContext<PGImplementation> tc = getCurrent();
		
		HSQLDBTest ht = new HSQLDBTest();
				
		String dataDir = ht.getDatabase();		
		String destUrl = hi.createJdbcUrl(dataDir);
		boolean copyData = true;
				
		PagilaHSQLDBPortGenerator gen = new PagilaHSQLDBPortGenerator(
				tc.getJdbcURL(), tc.getJdbcConfig(), hpc, destUrl, hi.getProperties(), copyData);
						
		gen.transform(cat);
	}	
}
