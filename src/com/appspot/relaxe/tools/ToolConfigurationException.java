/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.tools;

public class ToolConfigurationException extends ToolException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6619050162462367830L;

	public ToolConfigurationException(String msg) {
        this(msg, null);
    }

    public ToolConfigurationException(String msg, Throwable cause) {
        super(CONFIGURATION_ERROR, msg, cause);
    }
}
