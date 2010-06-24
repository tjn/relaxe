/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.dbmeta.tools;

import fi.tnie.db.expr.Identifier;
import fi.tnie.util.cli.Parser;

public class CatalogInfo
    extends CatalogTool {
        
    /**
     * @param args
     */
    public static void main(String[] args) {        
        System.exit(new CatalogInfo().run(args));
    }
    
    @Override
    public void run() {
        Identifier n = getCatalog().getName();
        String cat = (n == null) ? null : n.getName();        
        message("Catalog loaded: " + ((cat == null) ? "<unnamed>" : cat));
    }
    
    @Override
    protected void prepare(Parser p) {        
        super.prepare(p);
        addOption(p, OPTION_VERBOSE);        
    }
}
