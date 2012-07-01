/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.tools;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaMap;
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
        
        SchemaMap sm = getCatalog().schemas();
        
        message("schemas (" + sm.values().size() + ") {");
        
        for (Schema s : sm.values()) {
        	message(s.getUnqualifiedName().getName());        		
		}
        
        message("}");
        
        
    }
    
    @Override
    protected void prepare(Parser p) {        
        super.prepare(p);
        addOption(p, OPTION_VERBOSE);        
    }
}
