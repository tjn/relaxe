/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.feature;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.ddl.AlterTableAddColumn;
import fi.tnie.db.expr.ddl.Timestamp;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.Schema;

public class MetaData
    extends AbstractFeature
    implements SQLGenerator {

    public MetaData() {
        super(MetaData.class, 0, 1);
    }

    @Override
    public String getName() {
        return "MetaData";
    }

    @Override
    public SQLGenerator getSQLGenerator() {
        return this;
    }

    @Override
    public SQLGenerationResult modify(Catalog cat)
            throws SQLGenerationException {
        
        Environment env = cat.getEnvironment();
        SQLGenerationResult result = new SQLGenerationResult();
        
        final Identifier n = env.createIdentifier("created_at");
        
        for (Schema s : cat.schemas().values()) {
            for (BaseTable t : s.baseTables().values()) {                
                Column c = t.columnMap().get(n);
                
                if (c == null) {                    
                    result.add(addStatement(t, n));
                }                                
            }
        }
        
        return result;
    }

    @Override
    public SQLGenerationResult revert(Catalog cat)
            throws SQLGenerationException {
        SQLGenerationResult result = new SQLGenerationResult();
        
        
        return result;
    }    
    
    private AlterTableAddColumn addStatement(BaseTable t, Identifier n) {        
        return new AlterTableAddColumn(t.getName(), n, Timestamp.get());
    }
 }
