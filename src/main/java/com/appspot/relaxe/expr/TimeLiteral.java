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
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class TimeLiteral	
	extends DateTimeLiteral {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349786152502099361L;
	private String symbol;	
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private TimeLiteral() {
	}
	
	
	public TimeLiteral(int hh, int min, int ss, int sss, int precision) {
		StringBuilder buf = new StringBuilder(22);
		
		buf.append(SQLKeyword.TIME.getTerminalSymbol());
		buf.append(' ');
		buf.append('\'');
		appendTime(buf, hh, min, ss, sss, precision);
		buf.append('\'');
		
		this.symbol = buf.toString();		
	}	
	
	public TimeLiteral(int hh, int min, int ss) {
		StringBuilder buf = new StringBuilder(15);
		
		buf.append(SQLKeyword.TIME.getTerminalSymbol());
		buf.append(' ');
		buf.append('\'');
		appendTime(buf, hh, min, ss);
		buf.append('\'');
		
		this.symbol = buf.toString();		
	}
	
	@Override
	public String getTerminalSymbol() {
		return this.symbol;
	}
	
	
	@Override
	public int getType() {
		return PrimitiveType.TIME;
	}
}