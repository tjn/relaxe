/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import fi.tnie.db.feature.Features;
import fi.tnie.db.meta.impl.pg.PGEnvironment;
import junit.framework.TestCase;

public class BuilderTest extends TestCase {

    public void testGeneration() {
                
        PGEnvironment env = new PGEnvironment();
        Builder b = new Builder(env);
        
        final Features f = new Features();            
        b.setFeatures(f);
//        b.run(c, config)        
        
        
    }
    
    
}
