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

import java.util.Properties;

import com.appspot.relaxe.BlobExtractorFactory;
import com.appspot.relaxe.DefaultBlobExtractorFactory;
import com.appspot.relaxe.DefaultValueAssignerFactory;
import com.appspot.relaxe.DefaultValueExtractorFactory;
import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;

public abstract class DefaultImplementation<I extends Implementation<I>>	
	implements Implementation<I> {

	private ValueExtractorFactory valueExtractorFactory; 
	private ValueAssignerFactory valueAssignerFactory;	
	private BlobExtractorFactory blobExtractorFactory;
	
	@Override
	public abstract CatalogFactory catalogFactory();

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		if (valueExtractorFactory == null) {			
			valueExtractorFactory = createValueExtractorFactory();			
		}

		return valueExtractorFactory;
	}
	
	protected ValueExtractorFactory createValueExtractorFactory() {
		return new DefaultValueExtractorFactory();
	}
	
	@Override
	public BlobExtractorFactory getBlobExtractorFactory() {
		if (blobExtractorFactory == null) {
			blobExtractorFactory = createBlobExtractorFactory();
			
		}

		return blobExtractorFactory;
	}
	
	protected BlobExtractorFactory createBlobExtractorFactory() {
		return new DefaultBlobExtractorFactory();
	}
	
	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		if (valueAssignerFactory == null) {
			this.valueAssignerFactory = createValueAssignerFactory();			
		}

		return valueAssignerFactory;
	}

	protected ValueAssignerFactory createValueAssignerFactory() {
		return new DefaultValueAssignerFactory();
	}
	
	@Override
	public Properties getProperties() {
		return new Properties();
	}
	
	
}
