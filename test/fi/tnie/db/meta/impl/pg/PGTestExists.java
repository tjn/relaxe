/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.io.IOException;

public class PGTestExists
    extends PGJDBCTestCase {
    
    @Override
    protected void setUp() throws Exception {
    }

    public void testExists() {
        try {
            boolean e = databaseExists();
            logger().debug("database exists: " + e);
        } 
        catch (IOException e) {        
            logger().error(e.getMessage(), e);
        } 
        catch (InterruptedException e) {
            logger().error(e.getMessage(), e);
        }
    }

}
