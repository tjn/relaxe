/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui.action;

import fi.tnie.db.model.BooleanModel;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.ui.HasDisplayName;
import fi.tnie.db.ui.HasEnablement;

public interface Action
	extends HasDisplayName, HasEnablement {
	
	ValueModel<String> displayName();
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
