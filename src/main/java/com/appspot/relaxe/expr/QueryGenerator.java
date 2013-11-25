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
/**
 * 
 */
package com.appspot.relaxe.expr;

public class QueryGenerator
	extends TokenVisitor {
	
	private StringBuilder buffer = null;	
	private Token previous;
				
	public QueryGenerator(StringBuilder buffer) {
		super();		
		this.buffer = buffer;		
	}
	
	
//	private VisitContext append(Element e) {
//		return append(e, " ");
//	}
//
//	private VisitContext append(Element e, String suffix) {
//		String ts = e.getTerminalSymbol();
//		
//		if (ts != null) {
//			buffer.append(ts);
//			
//			if (suffix != null) {
//				buffer.append(suffix);
//			}
//		}			
//
//		
//		return null;
//	}
	
	
	
	
	

	@Override
	public VisitContext visit(Token next) {
		if (previous != null) {
			if (previous.isOrdinary() && next.isOrdinary()) {
				buffer.append(" ");
			}			
			
			String ts = previous.getTerminalSymbol();
			
			if (ts != null && ts.equals("?")) {
				buffer.append(" ");
			}
		}
		
		String ts = next.getTerminalSymbol();
		
		if (ts != null) {
			buffer.append(ts);
		}			
	
				
		this.previous = next;
		
		return null;
	}

	
	@Override
	public VisitContext start(VisitContext vc, SQLKeyword e) {
		if (previous != null && (!previous.isOrdinary())) {		
			buffer.append(' ');
		}
		
		return super.start(vc, e);
	}
	
	@Override
	public VisitContext start(VisitContext vc, Identifier e) {		
		// For MySQL: which
		
		if (previous != null && previous.isOrdinary()) {		
			buffer.append(' ');
		}
		
		return super.start(vc, e);
	}
	
}