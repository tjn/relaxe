/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Launcher {

    public static RunResult exec(List<String> args) throws IOException, InterruptedException {
        String[] aa = {};
        aa = args.toArray(aa);
        
        System.err.println(args.toString());
        
        Process p = Runtime.getRuntime().exec(aa);
        
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
}
