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

public class CreateSchema
	extends SQLSchemaStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7627361956865390823L;
	private Identifier schemaName;	
	private Identifier authID;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected CreateSchema() {
	}
				
	public CreateSchema(Identifier name) {
	    this(name, null);
	}
	
	public CreateSchema(Identifier schemaName, Identifier authID) {
		super(Name.CREATE_SCHEMA);
		
		if (schemaName == null) {
            throw new NullPointerException("'schemaName' must not be null");
        }
		
		this.schemaName = schemaName;
		this.authID = authID;
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.CREATE.traverse(vc, v);		
		SQLKeyword.SCHEMA.traverse(vc, v);
		getSchemaName().traverse(vc, v);
		
		if (this.authID != null) {
		    SQLKeyword.AUTHORIZATION.traverse(vc, v);
		    this.authID.traverse(vc, v);
		}
		
		// TODO: schema-element-list
	}

    public Identifier getSchemaName() {
        return schemaName;
    }
}
