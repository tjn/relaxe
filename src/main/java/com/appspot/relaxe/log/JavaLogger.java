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
package com.appspot.relaxe.log;

import java.util.logging.Level;


public class JavaLogger
	implements Logger {
	
	private java.util.logging.Logger implementation;
		
	public static Logger getLogger(Class<?> clazz) {
		return new JavaLogger(java.util.logging.Logger.getLogger(clazz.getName()));
	}
	
	public static Logger getLogger(String name) {
		return new JavaLogger(java.util.logging.Logger.getLogger(name));
	}	
	
	public JavaLogger(java.util.logging.Logger implementation) {
		super();
		
		if (implementation == null) {
			throw new NullPointerException("implementation");
		}
		
		this.implementation = implementation;
	}

	@Override
	public void debug(String msg) {
		implementation.fine(msg);
	}

	@Override
	public void error(String msg) {
		implementation.log(Level.SEVERE, msg);		
	}

	@Override
	public void error(String msg, Throwable t) {
		implementation.log(Level.SEVERE, msg, t);		
	}

	@Override
	public void fatal(String msg) {
		implementation.log(Level.SEVERE, msg);		
	}

	@Override
	public void info(String msg) {
		implementation.log(Level.INFO, msg);		
	}

	@Override
	public void warn(String msg) {
		implementation.log(Level.WARNING, msg);		
	}

}
