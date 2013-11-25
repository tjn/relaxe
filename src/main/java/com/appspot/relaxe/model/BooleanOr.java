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
package com.appspot.relaxe.model;

import java.util.Arrays;
import java.util.List;

public class BooleanOr
	extends AbstractBooleanOperatorModel {
			
	
	public BooleanOr(BooleanModel ... input) {
		this(Arrays.asList(input));
	}
	
	public BooleanOr(List<BooleanModel> input) {
		super(input);
	}
	
	/**
	 * Computes Boolean OR from the input.
	 * 
	 * Returns Boolean.TRUE if and only if <code>input</code> contains at least one model with <code>true</code> value.   
	 * Otherwise, returns <code>null</code> if <code>input</code> contains at least one model with <code>null</code> value.
	 * Otherwise, returns Boolean.FALSE.
	 * 
	 * As a consequence, Boolean.FALSE is returned if the input is empty.  
	 * 
	 * @throws NullPointerException if the input is <code>null</code>.
	 * @precondition: <code>input</code> does not contain null -elements. 
	 */
	@Override
	protected Boolean computeNewValue(List<BooleanModel> input)
		throws NullPointerException {
		Boolean result = Boolean.FALSE;
		
		for (BooleanModel m : input) {
			Boolean v = m.get();
			
			if (v == null) {
				result = null;
				continue;
			}
			
			if (v.booleanValue()) {
				result = Boolean.TRUE;
				break;
			}				
		}
		
		return result;
	}		
}