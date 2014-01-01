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
package com.appspot.relaxe.model.cm;

import com.appspot.relaxe.model.Registration;
import com.appspot.relaxe.model.ValueModel;

public interface ConstrainedValueModel<V>
	extends ValueModel<V> {
	
	Registration addConstraint(Constraint ph);
	
	/**
	 * Creates a new proposition. 
	 * @param newValue
	 * @return
	 */	
	Proposition propose(ChangeSet cs, V newValue, Proposition impliedBy);
	
	/**
	 * Submits the proposition and returns it.  
	 * 
	 * @param cs
	 * @param p
	 * @return
	 */	
	Proposition submit(ChangeSet cs, Proposition p);
	
	/**
	 * If this model contains a proposition for this model within
	 * the specified change set, returns the proposed value. 
	 * Otherwise, returns the current value of the model. 
	 *   
	 * @param cs
	 * @return
	 */
	V proposed(ChangeSet cs);
}
