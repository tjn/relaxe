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
package com.appspot.relaxe.source;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.source.SourceGenerator;

import junit.framework.TestCase;

public class SourceGeneratorTest extends TestCase {

	private static Logger logger = LoggerFactory.getLogger(SourceGeneratorTest.class);
	
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
