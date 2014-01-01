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
package com.appspot.relaxe.ui.action;

import com.appspot.relaxe.model.BooleanModel;
import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.ui.HasDisplayName;
import com.appspot.relaxe.ui.HasEnablement;

public interface Action
	extends HasDisplayName, HasEnablement {
	
	@Override
	ValueModel<String> displayName();
	@Override
	BooleanModel enablement();
	
	/**
	 * Performs some action if and only if this action is currently enabled according <code>isEnabled()</code>.
	 * 
	 * Example:
	 * 
	 * <code>
	 * 	if (!isEnabled()) {
	 * 		return false;
	 *  }
	 *  
	 *  <some action>  
	 *  
	 *  return true;
	 * 
	 * </code>
	 * 
	 * @return Whether this action was enabled at the time of the invocation.  
	 */
	boolean execute();
		
	/**
	 * Returns the boolean value equal to the following expression: <code>enablement().get() != null && enablement().get().booleanValue()</code> 
	 * 
	 * @return
	 */
	boolean isEnabled();
}
