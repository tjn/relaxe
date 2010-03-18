/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.feature;

public class Dependency {

    private Feature dependent;
    private Feature dependency;
    
    private Integer minMajor;
    private Integer maxMajor;
    private Integer minMinor;
    private Integer maxMinor;
}
