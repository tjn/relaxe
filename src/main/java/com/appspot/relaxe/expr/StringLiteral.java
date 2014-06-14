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

import com.appspot.relaxe.types.ValueType;

public class StringLiteral	
	extends AbstractLiteral {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349786152502099361L;
	private String quoted;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public StringLiteral() {
		this("");
	}
	
	public StringLiteral(String value) {
		this(value, false);
	}
	
	public StringLiteral(String value, boolean quoted) {
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		if (quoted) {
			this.quoted = value;
		}
		else {
			StringBuilder buf = new StringBuilder(value.length() + 2 + 1);		
			buf.append('\'');
			buf.append(value.replaceAll("'", "''"));
			buf.append('\'');			
			this.quoted = buf.toString();
		}
	}

	@Override
	public String getTerminalSymbol() {
		return quoted;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {		
		v.start(vc, (Token) this);
		v.end(this);			
	}
	
	@Override
	public int getType() {
		return ValueType.CHAR;
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
	
	@Override
	public Identifier getColumnName() {
		return null;
	}
}