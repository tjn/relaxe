/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.feature;

@SuppressWarnings("serial")
public class SQLGenerationException extends Exception {

    public SQLGenerationException() {
        super();
    }

    public SQLGenerationException(String msg, Throwable cause) {
        super(msg, cause);     
    }

    public SQLGenerationException(String msg) {
        super(msg);
    }    
}
