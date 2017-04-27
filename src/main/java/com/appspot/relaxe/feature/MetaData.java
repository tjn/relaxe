package com.appspot.relaxe.feature;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.expr.VisitContext;
import com.appspot.relaxe.expr.ddl.BaseTableElement;
import com.appspot.relaxe.expr.ddl.CreateSchema;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaMap;
import com.appspot.relaxe.types.VarcharType;

public class MetaData
	extends AbstractFeature {
	
	
	public static final String SCHEMA_NAME = "meta";
	
	public enum TableName {
		META_SCHEMA,
		META_TABLE,
		META_COLUMN
	}

	public enum ColumnName {
		ID,
		NAME,
	}


	
	
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 1;
	

    public MetaData() {
	    super(MetaData.class, MAJOR_VERSION, MINOR_VERSION);
    }

	@Override
	public SQLGenerator getSQLGenerator(boolean revert) {
		return revert ? null : null;
	}
	
	
	
	public static class MetaDataGenerator
		extends AbstractSQLGenerator {

		@Override
		public SQLGenerationResult modify(Catalog cat, DataTypeMap dataTypeMap) throws SQLGenerationException {
			SQLGenerationResult gr = new SQLGenerationResult();
			
			Environment env = cat.getEnvironment();
			
			Identifier schemaName = toIdentifier(env, SCHEMA_NAME);
						
			Schema schema = cat.schemas().get(schemaName);
			
			if (schema == null) {
				gr.add(new CreateSchema(schemaName));
			}
			
						
			
			Identifier stnm = toIdentifier(env, TableName.META_SCHEMA.name());
			
			BaseTable st = getBaseTable(schema, stnm);
			
			if (st == null) {				
				SchemaElementName tn = new SchemaElementName(schemaName, stnm);
				
				List<BaseTableElement> cl = new ArrayList<BaseTableElement>();
				
								
				cl.add(newVarchar(env, ColumnName.NAME.toString(), 64));
				
				ElementList<BaseTableElement> elementList = ElementList.newElementList(cl);
				
				CreateTable ct = new CreateTable(tn, elementList);
				gr.add(ct);
			}
			
						
			
			// CreateTable tt = new CreateTable();
			
			
			
			
			
			
			return null;
		}
		
	}

}
