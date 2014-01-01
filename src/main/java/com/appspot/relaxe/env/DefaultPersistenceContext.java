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
package com.appspot.relaxe.env;

import java.util.Properties;

import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.service.DataAccessContext;

/**
 * TODO: This needs to be fixed. DefaultImplementation should not extend a DefaultEnvironment.
 * We should not to require implementation object to be serializable.
 */

public abstract class DefaultPersistenceContext<I extends Implementation<I>>	
	implements PersistenceContext<I> {
	
	private I implementation;
	
	protected DefaultPersistenceContext() {
	}
	
	public DefaultPersistenceContext(I implementation) {
		super();
		
		if (implementation == null) {
			throw new NullPointerException("implementation");
		}
		
		this.implementation = implementation;
	}
	
	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		return getImplementation().getValueAssignerFactory();
	}

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		return getImplementation().getValueExtractorFactory();		
	}

	@Override
	public I getImplementation() {
		return this.implementation;
	}
	
	@Override
	public DataAccessContext newDataAccessContext(String jdbcURL, Properties jdbcConfig) {		
		return new DefaultDataAccessContext<I>(this, jdbcURL, jdbcConfig);
	}


}
