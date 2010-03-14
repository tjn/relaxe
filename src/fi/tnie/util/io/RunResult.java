/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.util.io;

import java.io.ByteArrayOutputStream;

public class RunResult {
    
    private int exitCode;
    private String output;
    private String error;
    
    public RunResult(int exitCode) {
        this(exitCode, null, null);
    }
            
    public RunResult(int exitCode, ByteArrayOutputStream output, ByteArrayOutputStream error) {
        this.exitCode = exitCode;        
        this.output = (output == null) ? "" : new String(output.toByteArray());
        this.error = (error == null) ? "" : new String(error.toByteArray());        
    }
    
    public boolean succeeded() {
        return (this.exitCode == 0);
    }
    
    public boolean failed() {
        return (this.exitCode != 0);
    }
    
    public int getExitCode() {
        return exitCode;
    }
    
    public String getOutput() {
        return this.output;
    }
        
    public String getError() {
        return this.error;
    }    
}
