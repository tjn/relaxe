/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
