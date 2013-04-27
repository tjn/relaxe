/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.source;

import java.io.File;

import com.appspot.relaxe.source.SourceGenerator;

import junit.framework.TestCase;

public class SourceGeneratorTest extends TestCase {

    public void testName() {
        SourceGenerator g = new SourceGenerator(new File("."), null);
        
        assertEquals("SourceDir", g.name("SOURCE_DIR"));
        assertEquals("SourceRootDir", g.name("SOURCE_ROOT_DIR"));
        assertEquals("SourceRootDir", g.name("_SOURCE_ROOT_DIR_"));
        assertEquals("Min", g.name("min"));
        assertEquals("Max", g.name("mAx"));
        assertEquals("Min", g.name("___min"));
        
        
    }
    
}
