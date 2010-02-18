/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.util.io;

import java.io.FileInputStream;
import java.io.IOException;
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
	
	
}
