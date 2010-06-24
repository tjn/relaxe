/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools;

public class ToolConfigurationException extends ToolException {
    public ToolConfigurationException(String msg) {
        this(msg, null);
    }

    public ToolConfigurationException(String msg, Throwable cause) {
        super(CONFIGURATION_ERROR, msg, cause);
    }
}
