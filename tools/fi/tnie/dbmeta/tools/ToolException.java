/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools;

public class ToolException extends Exception {
    public ToolException(String msg) {
        super(msg);
    }

    public ToolException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
