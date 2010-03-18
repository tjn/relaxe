/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.util.io;

import java.io.File;

public abstract class FileProcessor {   
    
    private boolean post;
     
    public FileProcessor() {        
        this(false);
    }
    
    public FileProcessor(boolean post) {        
        this.post = post;
    }
    
    public void traverse(File file) {
        if (file == null) {
            throw new NullPointerException("'file' must not be null");
        }
        
        if (pre()) {
            apply(file);
        }
        
        File[] dir = file.listFiles();
        
        if (dir != null) {
            for (int i = 0; i < dir.length; i++) {
                traverse(dir[i]);
            }
        }
        
        if (post()) {
            apply(file);
        }
    }    
    
    private boolean pre() {
        return (!this.post);
    }
    
    private boolean post() {
        return this.post;
    }
    

    public abstract void apply(File file);
}
