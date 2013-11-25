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
package com.appspot.relaxe;

import com.appspot.relaxe.log.Logger;

public class SLF4JLogger
	implements Logger {
	
	private org.slf4j.Logger inner;
		
	public static Logger getLogger(Class<?> clazz) {
		return new SLF4JLogger(org.slf4j.LoggerFactory.getLogger(clazz));
	}
	
	public SLF4JLogger(org.slf4j.Logger inner) {
		super();
		this.inner = inner;
	}

	@Override
	public void debug(String msg) {
		inner.debug(msg);		
	}

	@Override
	public void error(String msg) {
		inner.error(msg);
		
	}

	@Override
	public void error(String msg, Throwable t) {
		inner.error(msg, t);		
	}

	@Override
	public void fatal(String msg) {
		inner.error(msg);		
	}

	@Override
	public void info(String msg) {
		inner.info(msg);		
	}

	@Override
	public void warn(String msg) {
		inner.warn(msg);		
	}

	

}
