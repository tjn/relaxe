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
package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class SQLTimeType
    extends SQLDataType {
       	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4432919211852344906L;
	private static final SQLTimeType TYPE = new SQLTimeType();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private SQLTimeType() {
	}
		
	public static SQLTimeType get() {
        return TYPE;
    }   
    	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		SQLKeyword.TIME.traverse(vc, v);
	}	
}
