/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools;

import java.io.IOException;
import java.sql.SQLException;

public class ToolException extends Exception {
    
    private int exitCode;    
    
    public static final int GENERAL_ERROR = -1;
    public static final int IO_ERROR = -2;    
    public static final int CONFIGURATION_ERROR = -3;
    public static final int SQL_ERROR = -4;
            
    public ToolException(String msg) {
        this(GENERAL_ERROR, msg, null);        
    }
    
    public ToolException(IOException cause) {
        this(IO_ERROR, cause.getMessage(), cause);        
    }
    
    public ToolException(SQLException cause) {
        this(SQL_ERROR, cause.getMessage(), cause);        
    }
    
    public ToolException(String msg, Throwable cause) {
        this(GENERAL_ERROR, msg, cause);        
    }
        
    protected ToolException(int exit, String msg) {
        this(exit, msg, null);        
    }

    protected ToolException(int exit, String msg, Throwable cause) {
        super(msg, cause);
        setExitCode(exit);
    }

    public int getExitCode() {
        return exitCode;
    }

    private void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
    
}
