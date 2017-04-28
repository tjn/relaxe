package com.appspot.relaxe.feature;

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.BaseTableElement;
import com.appspot.relaxe.expr.ddl.ColumnDataType;
import com.appspot.relaxe.expr.ddl.ColumnDefinition;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.DropSchema;
import com.appspot.relaxe.expr.ddl.DropTable;
import com.appspot.relaxe.expr.ddl.types.SQLIntType;
import com.appspot.relaxe.expr.ddl.types.SQLVarcharType;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
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
	
	
	public List<BaseTable> getBaseTables(Catalog cat) {
		
		List<BaseTable> tables = new ArrayList<BaseTable>();
		
        for (Schema s : cat.schemas().values()) {
            tables.addAll(s.baseTables().values());
        }
        
        return tables;
	}

	protected BaseTable getBaseTable(Schema schema, String tableName) {			
		return (schema == null) ? null : schema.baseTables().get(tableName);
	}
	
	protected ColumnDefinition newInt(Environment env, String name) {
		return newColumn(env, name, SQLIntType.get());
	}

	protected ColumnDefinition newVarchar(Environment env, String name, int length) {
		return newColumn(env, name, SQLVarcharType.get(length));
	}

	protected ColumnDefinition newColumn(Environment env, String name, ColumnDataType dataType) {
		return new ColumnDefinition(toIdentifier(env, name), dataType);
	}

	protected CreateTable newCreateTable(SchemaElementName name, List<BaseTableElement> elements) {
		ElementList<BaseTableElement> elementList = ElementList.newElementList(elements);				
		CreateTable ct = new CreateTable(name, elementList);
		return ct;
	}

}
