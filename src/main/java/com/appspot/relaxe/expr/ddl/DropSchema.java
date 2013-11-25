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
package com.appspot.relaxe.expr.ddl;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class DropSchema
	extends SQLSchemaStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8328836316629524899L;
	private Identifier schemaName;
	private SQLKeyword cascade;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DropSchema() {
	}
				
	public DropSchema(Identifier schemaName) {
	    this(schemaName, null);
	}
	
	public DropSchema(Identifier schemaName, Boolean cascade) {
		super(Name.DROP_SCHEMA);
		
		if (schemaName == null) {
            throw new NullPointerException("'schemaName' must not be null");
        }
		
		this.schemaName = schemaName;
		this.cascade = cascade(cascade);
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.DROP.traverse(vc, v);		
		SQLKeyword.SCHEMA.traverse(vc, v);
		getSchemaName().traverse(vc, v);		
		traverseNonEmpty(this.cascade, vc, v);
	}

    public Identifier getSchemaName() {
        return schemaName;
    }	
}
