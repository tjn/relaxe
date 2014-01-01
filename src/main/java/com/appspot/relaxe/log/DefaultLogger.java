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
package com.appspot.relaxe.log;


public class DefaultLogger
	implements Logger {
	
	private Logger target;

	private static DefaultLogger instance;
	
	public static DefaultLogger getInstance() {
		if (instance == null) {
			instance = new DefaultLogger();			
		}
	
		return instance;
	}
	
	public static Logger getLogger() {
		DefaultLogger li = getInstance();
		return (li.target == null) ? li : li.target;
	}

	@Override
	public void debug(String msg) {	
	}

	@Override
	public void error(String msg) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void error(String msg, Throwable t) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void info(String msg) {
		// TODO Auto-generated method stub		
	}

	public Logger getTarget() {
		return this.target;
	}


	public void setTarget(Logger target) {
		this.target = target;
	}

	@Override
	public void fatal(String msg) {
//		Window.alert("FATAL: " + msg);
	}

	@Override
	public void warn(String msg) {
		
		
	}
}
