/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.feature;

import java.util.Set;

public interface Feature {
    
    String getName();
    
    int getVersionMajor();
    int getVersionMinor();
    
    Set<Dependency> dependencies();    
    SQLGenerator getSQLGenerator(); 
    
    // SourceGenerator getSourceGenerator();
}
