package com.appspot.relaxe.feature;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ddl.AlterTableAddColumn;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.Schema;

public class ColumnGenerator
	extends AbstractSQLGenerator {
	
	private String columnName;
	private SQLDataType dataType;
	
	public ColumnGenerator(String columnName, SQLDataType dataType) {
		super();
		
		if (columnName == null) {
			throw new NullPointerException("columnName");
		}
		
		if (dataType == null) {
			throw new NullPointerException("dataType");
		}
				
		this.columnName = columnName;
		this.dataType = dataType;		
	}

	@Override
	public SQLGenerationResult modify(Catalog cat, DataTypeMap dataTypeMap) throws SQLGenerationException {
        Environment env = cat.getEnvironment();
        SQLGenerationResult result = new SQLGenerationResult();
        
        final Identifier n = env.getIdentifierRules().toIdentifier(this.columnName);                
                
        for (Schema s : cat.schemas().values()) {
            for (BaseTable t : s.baseTables().values()) {                
                Column c = t.getColumnMap().get(n);
                
                if (c == null) {
                    result.add(newAddColumnStatement(t, n, this.dataType));
                }
                else {	                	
                	SQLDataType ct = dataTypeMap.getSQLType(c.getDataType());
                	
                	if (this.dataType.equals(ct)) {
                		String tt = this.dataType.generate();	                		
                		result.add(String.format("Could not add column '%s' of type into table '%s'. Column already exists different type: %s", this.columnName, tt, t.getQualifiedName(), ct.generate()));
                	}	                		                		                	
                }
            }
        }
        
        return result;
	}
	
    private AlterTableAddColumn newAddColumnStatement(BaseTable t, Identifier n, SQLDataType dataType) {        
        return new AlterTableAddColumn(t.getName(), n, dataType);
    }

}