/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.feature;

import java.util.Collections;
import java.util.Set;

public class MetaDataFeature implements Feature {

    @Override
    public Set<Dependency> dependencies() {
        return Collections.emptySet();
    }

    @Override
    public SQLGenerator getSQLGenerator() {
        return null;
    }

    @Override
    public int getVersionMajor() {
        return 0;
    }

    @Override
    public int getVersionMinor() {
        return 1;
    }

    @Override
    public String getName() {        
        return "EntityMetaData feature";
    }
}
