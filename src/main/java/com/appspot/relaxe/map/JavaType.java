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
package com.appspot.relaxe.map;

/**
 * @author Administrator
 *
 */
public class JavaType {
	private String packageName; 
	private String unqualifiedName;
	private String qualifiedName;
			
	public JavaType(String packageName, String unqualifiedName) {
		super();
		setName(packageName, unqualifiedName);
	}

	protected JavaType() {	    
	}
	
    private void setName(String packageName, String unqualifiedName) {
        this.packageName = packageName;									
		this.unqualifiedName = unqualifiedName;
		
		if (this.packageName == null) {
			this.qualifiedName = this.unqualifiedName;
		}
		else {
			StringBuffer buf = new StringBuffer(this.packageName);
			buf.append(".");
			buf.append(unqualifiedName);
			this.qualifiedName = buf.toString();
		}
    }
	
	public String getPackageName() {
		return this.packageName;
	}
	
	public String getUnqualifiedName() {			
		return this.unqualifiedName;
	}	
	
	public String getQualifiedName() {
		return this.qualifiedName;
	}
}