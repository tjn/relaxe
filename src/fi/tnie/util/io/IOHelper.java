/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;

public class IOHelper {

	public static Properties load(String path) 
		throws IOException {
		FileInputStream in = null;

		try {
			in = new FileInputStream(path);
			Properties p = new Properties();
			p.load(in);
			return p;
		} 
		finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public static void write(CharSequence source, File dest)
		throws IOException {
		
		Writer w = new FileWriter(dest, false);
		int len = source.length();
		
		for (int i = 0; i < len; i++) {
			w.write(source.charAt(i));
		}
		
		w.close();
	}

    public String read(InputStream in, String encoding, int initialCapacity) 
        throws IOException {
        if (in == null) {
            throw new NullPointerException();
        }
        InputStreamReader r = null;
        StringBuilder dest = new StringBuilder(initialCapacity); 
        
        try {
            r = new InputStreamReader(in, encoding);           
            
            int c;
            
            while((c = r.read()) != -1) {
                dest.append((char) c);
            }
        }
        finally {
            close(r);
        }
        
        return dest.toString();
    }

    protected void close(Reader r) {
        if (r != null) {
            try {
                r.close();
            } 
            catch (IOException e) {
            }
        }        
    }
	
	
}
