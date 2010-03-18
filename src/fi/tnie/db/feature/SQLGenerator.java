/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.feature;

import java.sql.Connection;
import fi.tnie.db.meta.Catalog;

public interface SQLGenerator {    
       
    
    /**
     * Generates new or modifies existing SQL Objects in the catalog. 
     * 
     * @param cat
     * @return
     * @throws SQLGenerationException
     */
    SQLGenerationResult modify(Catalog cat)
        throws SQLGenerationException;

    /**
     * Reverts the modifications made in the existing SQL Objects in the catalog. 
     * 
     * @param c
     * @param cat
     * @return
     * @throws SQLGenerationException
     */
    SQLGenerationResult revert(Catalog cat)
        throws SQLGenerationException;
}
