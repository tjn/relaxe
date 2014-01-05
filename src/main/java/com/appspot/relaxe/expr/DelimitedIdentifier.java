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
package com.appspot.relaxe.expr;


public class DelimitedIdentifier
	extends AbstractIdentifier {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4618716859483968962L;
	private String token;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DelimitedIdentifier() {
	}
	
	public DelimitedIdentifier(String content) {
		super(content);
	}	
	
	public String getToken() {
		if (token == null) {			
			String qm = "\"";
			StringBuilder buf = new StringBuilder(getContent().length() + 2);
			buf.append(qm);
			buf.append(getContent().replace(qm, qm + qm));
			buf.append(qm);
			this.token = buf.toString();
		}

		return token;
	}	

	@Override
	public boolean isOrdinary() {
		return false;
	}
	
	@Override
	public boolean isDelimited() {
		return true;
	}

	@Override
	public String getTerminalSymbol() {		
		return getToken();
	}
	
//	else {
//		this.ordinary = isValidOrdinary(token, null);
//		
//		if (this.ordinary) {
//			this.token = token;
//		}
//		else {
//			String qm = "\""; 
//			token.replace(qm, qm + qm);		
//			this.token = qm + token + qm;				
//		}
//	}		

}
