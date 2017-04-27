package com.appspot.relaxe.feature;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ddl.DropSchema;
import com.appspot.relaxe.expr.ddl.DropTable;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Schema;

public abstract class AbstractSQLGenerator
	implements SQLGenerator {

	protected BaseTable getBaseTable(Schema schema, Identifier n) {
	    return (schema == null) ? null : schema.baseTables().get(n);            
	}

	public void dropSchema(SQLGenerationResult r, Schema schema) {
	    if (schema != null) {
	        r.add(new DropSchema(schema.getUnqualifiedName()));
	    }
	}

	protected void dropBaseTable(SQLGenerationResult result, Schema schema, Identifier name) {
	    BaseTable table = getBaseTable(schema, name);
	    
	    if (table != null) {
	        result.add(new DropTable(table.getName()));
	    }                                            
	}
	
	protected Identifier toIdentifier(Environment env, String identifier) {            
        return env.getIdentifierRules().toIdentifier(identifier);        
    }

}
