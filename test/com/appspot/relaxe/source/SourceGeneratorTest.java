/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.source;

import java.io.File;

import org.apache.log4j.Logger;

import com.appspot.relaxe.source.SourceGenerator;

import junit.framework.TestCase;

public class SourceGeneratorTest extends TestCase {

	private static Logger logger = Logger.getLogger(SourceGeneratorTest.class);
	
    public void testName() {
        SourceGenerator g = new SourceGenerator(new File("."), null);
        
        assertEquals("SourceDir", g.name("SOURCE_DIR"));
        assertEquals("SourceRootDir", g.name("SOURCE_ROOT_DIR"));
        assertEquals("SourceRootDir", g.name("_SOURCE_ROOT_DIR_"));
        assertEquals("Min", g.name("min"));
        assertEquals("Max", g.name("mAx"));
        assertEquals("Min", g.name("___min"));
        
        
    }
    
    public void testGetReference() {
    	SourceGenerator g = new SourceGenerator(new File("."), null);
    	
    	String expected = "LANGUAGE";
    	
    	{	    	
	    	String name = g.referenceName("FILM", "film_language_id_fkey");	    	
	    	logger().debug("name: " + name);
	    	assertEquals(expected, name);
	    }
    	
    	{	    	
	    	String name = g.referenceName("FILM", "fk_film_language_id_fkey");
	    	logger().debug("name: " + name);
	    	assertEquals(expected, name);
    	}
    	
    	{	    	
	    	String name = g.referenceName("FILM", "fk_film_language");
	    	logger().debug("name: " + name);
	    	assertEquals(expected, name);
    	}
    	
    	{	    	
	    	String name = g.referenceName("FILM", "fk_original_language");
	    	logger().debug("name: " + name);
	    	assertEquals("ORIGINAL_LANGUAGE", name);
    	}
    	
    	{	    	
	    	String name = g.referenceName("FILM", "fk_original_language_id");
	    	logger().debug("name: " + name);
	    	assertEquals("ORIGINAL_LANGUAGE", name);
	    	logger().debug("name: " + g.name(name));
	    	
    	}
    	
    }

    private static Logger logger() {
		return SourceGeneratorTest.logger;
	}
}
