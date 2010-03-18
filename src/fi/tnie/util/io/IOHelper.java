/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import com.sun.xml.internal.ws.Closeable;

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

	public String read(File input) 
	    throws FileNotFoundException, IOException {
	    return read(new FileReader(input), 1024);
	}	
	
	public String read(File input, String encoding, int initialCapacity)
       throws IOException {
	    
	   if (input == null) {
	       throw new NullPointerException("'input' must not be null");
	   } 
	   
       InputStream in = new FileInputStream(input);
       String src = new IOHelper().read(in, encoding, 1024);
       return src;
   }

    public String read(InputStream in, String encoding, int initialCapacity) 
        throws IOException {
        if (in == null) {
            throw new NullPointerException();
        }
        
        return read(new InputStreamReader(in, encoding), initialCapacity);        
    }
    
    public String read(Reader in, int initialCapacity) 
        throws IOException {
        if (in == null) {
            throw new NullPointerException();
        }
        
        StringBuilder dest = new StringBuilder(initialCapacity); 
        
        try {
            int c;
            
            while((c = in.read()) != -1) {
                dest.append((char) c);
            }
        }
        finally {
            close(in);
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

    public static void store(Properties current, String path, String comments) 
        throws IOException {        
        FileOutputStream out = null;
        
        try {
            out = new FileOutputStream(path);
            current.store(out, comments);            
        } 
        finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
