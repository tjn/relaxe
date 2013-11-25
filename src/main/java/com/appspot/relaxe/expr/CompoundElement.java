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
package com.appspot.relaxe.expr;

public abstract class CompoundElement implements Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7299007466010084749L;

	@Override
	public final String getTerminalSymbol() {
		return null;
	}
		
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		try {
			vc = v.start(vc, this);
			traverseContent(vc, v);
		}
		finally {
			v.end(this);
		}		
	}
	
	protected void traverseContent(VisitContext vc, ElementVisitor v) {		
	}
	
	protected void traverseNonEmpty(Element e, VisitContext vc, ElementVisitor v) {		
		if (e != null) {
			v.start(vc, this);
			e.traverse(vc, v);
			v.end(this);
		}
	}
		
	public String generate() {
		StringBuilder dest = new StringBuilder();
		ElementVisitor v = new QueryGenerator(dest);
		traverse(null, v);		
		return dest.toString();			
	}
}
