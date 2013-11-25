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
package com.appspot.relaxe.env.hsqldb.expr;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.VisitContext;

public class Shutdown
	extends Statement {
	
	public static final Shutdown STATEMENT = new Shutdown();	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4505440690619367270L;	
	
	public Shutdown() {
		super();
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {

		HSQLDBKeyword.SHUTDOWN.traverse(vc, v);
	}

	


}
