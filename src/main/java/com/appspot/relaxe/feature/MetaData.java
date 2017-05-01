package com.appspot.relaxe.feature;

import java.util.Arrays;
import java.util.List;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.BaseTableElement;
import com.appspot.relaxe.expr.ddl.ColumnDefinition;
import com.appspot.relaxe.expr.ddl.CreateSchema;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.PrimaryKeyConstraint;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.Schema;

public class MetaData
	extends AbstractFeature {
		
	public static final String SCHEMA_NAME = "meta";
	
	
	enum Kind {
		SCHEMA,
		TABLE,
		COLUMN,
		CONSTRAINT,
		ENTITY,
		ATTRIBUTE,		
	}
	
	public enum TableName {
		META_SCHEMA,		
		META_BASE_TABLE,
		META_VIEW,
		META_COLUMN,
		META_UNIQUE_CONSTRAINT,
		META_FOREIGN_KEY_CONSTRAINT,
		META_KEY_COLUMN,
	}

	public enum ColumnName {
		ID,
		NAME,
		SCHEMA_ID
	}
	
	
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 1;

	private TableMapper tableMapper;
	

    public MetaData(TableMapper tableMapper) {
	    super(MetaData.class, MAJOR_VERSION, MINOR_VERSION);
	    
	    if (tableMapper == null) {
			throw new NullPointerException("tableMapper");
		}
	    
		this.tableMapper = tableMapper;	    
    }

	@Override
	public SQLGenerator getSQLGenerator(boolean revert) {
		return revert ? new MetaDataGenerator() : null;
	}
}
