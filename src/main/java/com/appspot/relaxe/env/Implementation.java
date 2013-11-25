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
package com.appspot.relaxe.env;

import java.util.Properties;

import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.meta.SerializableEnvironment;

public interface Implementation<I extends Implementation<I>> {

	/** Creates a factory to build entire catalog in this environment. 
	 * 
	 * @return
	 */
	CatalogFactory catalogFactory();
	
	/**
	 * Returns fully qualified class name of the JDBC driver for this implementation. 
	 * 
	 * @return
	 */
	String defaultDriverClassName();
	SQLSyntax getSyntax();
	
	ValueExtractorFactory getValueExtractorFactory();			
	ValueAssignerFactory getValueAssignerFactory();
	
	String createJdbcUrl(String database);
	String createJdbcUrl(String host, String database);
	String createJdbcUrl(String host, Integer port, String database);
	
	SerializableEnvironment environment();
	
	I self();
	
	Properties getDefaultProperties();
	
}