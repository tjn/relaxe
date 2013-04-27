/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.feature;

import java.util.Collections;
import java.util.Set;

public abstract class AbstractFeature
    implements Feature {

    private int versionMajor;
    private int versionMinor;
    private String name;
    
    public AbstractFeature(Class<? extends Feature> featureType, int versionMajor, int versionMinor) {
        super();
        
        if (featureType == null) {
            throw new NullPointerException("'featureType' must not be null");
        } 
        
        String n = featureType.getCanonicalName();
        
        if (n == null) {
            throw new NullPointerException("canonical name 'featureType' must not be null");
        }
        
        this.name = n;
        
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
    }

    public int getVersionMajor() {
        return versionMajor;
    }

    public int getVersionMinor() {
        return versionMinor;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public Set<Dependency> dependencies() {        
        return Collections.emptySet();
    }
}
