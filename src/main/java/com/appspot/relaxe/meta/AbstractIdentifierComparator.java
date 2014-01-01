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
package com.appspot.relaxe.meta;

import java.io.Serializable;
import java.util.Comparator;

import com.appspot.relaxe.expr.Identifier;


/**
 * SQL Standard -compatible FoldingComparator.
 * 
 * @author Administrator
 */

public abstract class AbstractIdentifierComparator
	implements Comparator<Identifier>, Serializable
	{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6545832196162079657L;	
			
	
	
	public AbstractIdentifierComparator() {
		super();
	}

	@Override
	public int compare(Identifier o1, Identifier o2) {				
		return compare(name(o1), name(o2));
	}
	
	protected abstract int compare(String n1, String n2);
			
	protected abstract String name(Identifier ident);
}
