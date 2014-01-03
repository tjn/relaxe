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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class IOHelper {

    private static IOHelper instance = new IOHelper();
    
    public static Properties doLoad(String path) 
        throws IOException {
        return IOHelper.instance.load(path);
    }
    
    public static void doClose(Closeable c) {
        try {
            IOHelper.instance.close(c);
        } 
        catch (IOException e) {
            // suppressed:            
        }
    }
    
    public static void doClose(Closeable in, Closeable out) {
        doClose(in);
        doClose(out);
    }
    
    public Properties load(String path) 
        throws IOException {
        return load(path, null);
    }
    
            
	public Properties load(String path, Properties defaults)
		throws IOException {
		FileInputStream in = null;

		try {
			in = new FileInputStream(path);
			Properties p = new Properties(defaults);
			p.load(in);
			return p;
		} 
		finally {
			if (in != null) {
				in.close();
			}
		}
	}
	
	public static void doWrite(CharSequence source, File dest) 
	    throws IOException {
	    IOHelper.instance.write(source, dest);	    
	}
	
	public void write(CharSequence source, File dest)
		throws IOException {
		
	    Writer w = null;
	    
	    try {
    		w = new FileWriter(dest, false);
    		int len = source.length();
    		
    		for (int i = 0; i < len; i++) {
    			w.write(source.charAt(i));
    		}
	    }
	    finally {
	        close(w);    		
	    }
	}
	
	public static void doWrite(byte[] source, File dest) 
		throws IOException {
	    IOHelper.instance.write(source, dest);	    
	}
	
	public void write(byte[] input, File dest)
		throws IOException {
		
	    FileOutputStream os = null;
	    
	    try {
			os = new FileOutputStream(dest, false);			
			os.write(input);						
	    }
	    finally {
	        close(os);    		
	    }
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
    
    public String read(Reader in) 
        throws IOException {
        return read(in, 4096);
    }
    
    /**
     * Reads all from <code>in</code> 
     * @param in
     * @param initialCapacity
     * @return
     * @throws IOException
     */
    
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

    
    public <I extends InputStream> I close(I in) throws IOException {
        if (in != null) {
            in.close();
        }
        
        return null;
    }
        
    public void close(Closeable c) 
        throws IOException {
        if (c != null) {
            c.close();
        }
    }
    
    public void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } 
            catch (IOException e) {
                // suppressed
            }
        }
    }

    public <R extends Reader> R close(R r) throws IOException {
        if (r != null) {
            r.close();
        }
        return null;
    }    
    
    public <W extends Writer> W close(W w) throws IOException {
        if (w != null) {
            w.close();
        }
    
        return null;
    }
    
    public <O extends OutputStream> O close(O o) throws IOException {
        if (o != null) {
            o.close();
        }        
        return null;
    }
    
    public static void doStore(Properties current, String path, String comments) 
        throws IOException {
        IOHelper.instance.store(current, path, comments);
    }

    public void store(Properties current, String path, String comments) 
        throws IOException {        
        FileOutputStream out = null;
        
        try {
            out = new FileOutputStream(path);
            current.store(out, comments);            
        } 
        finally {
            close(out);
        }
    }

    public void copy(InputStream in, OutputStream out)
        throws IOException {
        
        int b = 0;
        
        try {
            in = new BufferedInputStream(in);
            out = new BufferedOutputStream(out);
            
            while ((b = in.read()) != -1) {
                out.write(b);
            }
                        
            out.flush();
        }
        catch(IOException e) {
            closeQuietly(in);
            closeQuietly(out);
            throw e;
        }
    }
}
