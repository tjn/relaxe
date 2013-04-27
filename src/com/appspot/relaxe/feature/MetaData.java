/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.feature;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ddl.AlterTableAddColumn;
import com.appspot.relaxe.expr.ddl.Timestamp;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Environment;
import com.appspot.relaxe.meta.Schema;

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
        
        final Identifier n = env.getIdentifierRules().toIdentifier("created_at");
                
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
