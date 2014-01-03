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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launcher {
        
    private File dir;
    private List<String> args;
            
    public RunResult exec() throws IOException, InterruptedException {
        return exec(args());
    }        

    public RunResult exec(List<String> args) throws IOException, InterruptedException {
        String[] aa = {};
        aa = args.toArray(aa);
        
        System.err.println(args.toString());
        
        Process p = Runtime.getRuntime().exec(aa, null, getDir());
        
//        System.err.println("waiting...");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        
        Thread ir = new Thread(new Pipe(p.getInputStream(), out, Pipe.Endpoint.IN));
        Thread er = new Thread(new Pipe(p.getErrorStream(), err, Pipe.Endpoint.IN));
        
        ir.start();
        er.start();
        
        int exit = p.waitFor();

        p.getOutputStream().close();                
        p.getErrorStream().close();
        
        return new RunResult(exit, out, err);
    }



    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }
    
    public void add(String arg) {
        if (arg == null) {
            throw new NullPointerException("'arg' must not be null");
        }
        
        args().add(arg);
    }
    
    public List<String> args() {
        if (args == null) {
            args = new ArrayList<String>();            
        }

        return args;
    }
    
    public static RunResult doExec(List<String> args) throws IOException, InterruptedException {
        return new Launcher().exec(args);
    }
}
