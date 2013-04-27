/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.appspot.relaxe.env.Implementation;

public class DriverVersionVariable<I extends Implementation<I>> {
	
	private ClassLoader loader; 
	
	public DriverVersionVariable(File driver) throws MalformedURLException {
		this(driver.toURI().toURL());
	}

	public DriverVersionVariable(URL url) {
		init(new URL[] { url });
	}

	private void init(URL[] urls) {
		this.loader = new URLClassLoader(urls, null);
	}

	
	
}
