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
package com.appspot.relaxe.io;

import java.io.File;
import java.io.FileFilter;

public abstract class FileProcessor {   
    
    private boolean post;
    private FileFilter filter;
     
    public FileProcessor() {        
        this(false);
    }
    
    public FileProcessor(boolean post) {        
        this(null, post); 
    }
    
    public FileProcessor(FileFilter filter) {
        this(filter, false);
    }
    
    public FileProcessor(FileFilter filter, boolean post) {
        this.filter = filter;
        this.post = post;
    }
    
    public void traverse(File file) {
        if (file == null) {
            throw new NullPointerException("'file' must not be null");
        }
               
        if (pre()) {
            doApply(file);
        }
        
        File[] files = file.listFiles();
        
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                traverse(files[i]);
            }
        }
        
        if (post()) {
            doApply(file);
        }
    }    
    
    private boolean pre() {
        return (!this.post);
    }
    
    private boolean post() {
        return this.post;
    }
    
    private void doApply(File file) {
        if (this.filter == null || this.filter.accept(file)) {
            apply(file);
        }
    }
    
    
    public abstract void apply(File file);    
}
