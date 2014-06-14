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

public abstract class DateTimeLiteral	
	extends AbstractLiteral {
	
	public static final int DEFAULT_TIME_PRECISION = 6;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349786152502099361L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected DateTimeLiteral() {
	}

	protected void appendDate(StringBuilder buf, int yyyy, int mm, int dd) {
		pad(buf, Integer.toString(yyyy), 4, '0');
		buf.append('-');
		pad(buf, Integer.toString(mm), 2, '0');
		buf.append('-');
		pad(buf, Integer.toString(dd), 2, '0');
	}
	
	protected void appendTime(StringBuilder buf, int hh, int mm, int ss) {
		pad(buf, Integer.toString(hh), 2, '0');
		buf.append(':');
		pad(buf, Integer.toString(mm), 2, '0');
		buf.append(':');
		pad(buf, Integer.toString(ss), 2, '0');
	}
	
	protected void appendTime(StringBuilder buf, int hh, int mm, int ss, int sss) {
		appendTime(buf, hh, mm, ss, sss, DEFAULT_TIME_PRECISION);
	}
	
	protected void appendTime(StringBuilder buf, int hh, int mm, int ss, int sss, int precision) {
		appendTime(buf, hh, mm, ss);
		
		if (precision > 0) {
			buf.append('.');
			String fp = Integer.toString(sss);
			fp = (fp.length() > precision) ? fp.substring(0, precision) : fp;			
			pad(buf, fp, precision, '0');
		}
	}

	protected int pad(StringBuilder dest, String value, int length, char pc) {				
		int n = length - value.length();
		
		for (int i = 0; i < n; i++) {
			dest.append(pc);
		}
		
		dest.append(value);
		
		return n;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		Token t = this;
		v.start(vc, t);
		v.end(t);		
	}
	
	@Override
	public abstract String getTerminalSymbol();
			
	@Override
	public Identifier getColumnName() {
		return null;
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
}