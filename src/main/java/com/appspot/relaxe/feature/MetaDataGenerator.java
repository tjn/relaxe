package com.appspot.relaxe.feature;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.ddl.AlterTableAddForeignKey;
import com.appspot.relaxe.expr.ddl.BaseTableElement;
import com.appspot.relaxe.expr.ddl.ColumnDefinition;
import com.appspot.relaxe.expr.ddl.CreateSchema;
import com.appspot.relaxe.expr.ddl.CreateTable;
import com.appspot.relaxe.expr.ddl.PrimaryKeyConstraint;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.Schema;

public class MetaDataGenerator
	extends AbstractSQLGenerator {
	
	public static final String SCHEMA_NAME = "meta";
	
	
	public final static int NAME_LENGTH = 128;
	public final static int DEFAULT_VALUE_LENGTH = 1024;
	
	public enum TableName {
		META_SCHEMA,		
		META_BASE_TABLE,
		META_DATA_TYPE,
		META_COLUMN,
		META_VIEW,
		META_TABLE_CONSTRAINT,
		META_KEY_COLUMN,
		
	}

	public enum ColumnName {
		ID,
		NAME,
		SCHEMA_ID,
		TABLE_ID,		
		DATA_TYPE_ID,
		COLUMN_ID,
		REFERENCED_COLUMN_ID,
		TABLE_CONSTRAINT_ID,
		SIZE,
		ORDINAL,
		TYPE,		
		NUM_PREC_RADIX,
		CHAR_OCTET_LENGTH,		
		DECIMAL_DIGITS,
		DEFAULT_VALUE
	}

	

	public MetaDataGenerator() {		
	}

	@Override
	public SQLGenerationResult modify(Catalog cat, DataTypeMap dataTypeMap) throws SQLGenerationException {
		SQLGenerationResult gr = new SQLGenerationResult();
		
		Environment env = cat.getEnvironment();
		
		final Identifier schemaName = toIdentifier(env, MetaData.SCHEMA_NAME);		
		final Schema schema = cat.schemas().get(schemaName);		
		final Identifier schemaTableName = toIdentifier(env, TableName.META_SCHEMA);
						
		if (schema == null) {
			gr.add(new CreateSchema(schemaName));
		}
		
		BaseTable st = getBaseTable(schema, schemaTableName);
		
		if (st == null) {	
			CreateTable ct = newSchemaTable(env, schemaTableName);				
			gr.add(ct);
		}
		
		final Identifier dataTypeTableName = toIdentifier(env, TableName.META_DATA_TYPE);
		
		{
			
			BaseTable t = getBaseTable(schema, dataTypeTableName);
			
			if (t == null) {
				gr.add(newDataTypeMetaTable(env, dataTypeTableName));
			}
		}
		
		Identifier baseTableTableName = toIdentifier(env, TableName.META_BASE_TABLE);
		
		{			
			BaseTable t = getBaseTable(schema, baseTableTableName);
			
			if (t == null) {
				gr.add(newBaseTableMetaTable(env, baseTableTableName));				
				gr.add(newForeignKey(env, baseTableTableName, schemaTableName, ColumnName.SCHEMA_ID, ColumnName.ID));				
			}
		}
		
		final Identifier viewTableName = toIdentifier(env, TableName.META_VIEW);
		
		{			
			BaseTable t = getBaseTable(schema, viewTableName);
			
			if (t == null) {
				gr.add(newViewMetaTable(env, viewTableName));				
				gr.add(newForeignKey(env, viewTableName, schemaTableName, ColumnName.SCHEMA_ID, ColumnName.ID));				
			}
		}	
		
		final Identifier columnTableName = toIdentifier(env, TableName.META_COLUMN);
		
		{
			
			BaseTable t = getBaseTable(schema, columnTableName);
			
			if (t == null) {
				gr.add(newColumnMetaTable(env, columnTableName));				
				gr.add(newForeignKey(env, columnTableName, baseTableTableName, ColumnName.TABLE_ID, ColumnName.ID));
				gr.add(newForeignKey(env, columnTableName, dataTypeTableName, ColumnName.DATA_TYPE_ID, ColumnName.ID));
			}
		}
		
		
		final Identifier tableConstraintTableName = toIdentifier(env, TableName.META_TABLE_CONSTRAINT);
		
		{	
			BaseTable t = getBaseTable(schema, tableConstraintTableName);
			
			if (t == null) {
				gr.add(newTableConstraintMetaTable(env, tableConstraintTableName));				
				gr.add(newForeignKey(env, tableConstraintTableName, baseTableTableName, ColumnName.TABLE_ID, ColumnName.ID));				
			}
		}
		
		{
			Identifier keyColumnTableName = toIdentifier(env, TableName.META_KEY_COLUMN);
			
			BaseTable t = getBaseTable(schema, keyColumnTableName);
			
			if (t == null) {
				gr.add(newKeyColumnMetaTable(env, keyColumnTableName ));				
				gr.add(newForeignKey(env, keyColumnTableName, tableConstraintTableName, ColumnName.TABLE_CONSTRAINT_ID, ColumnName.ID));				
				gr.add(newForeignKey(env, keyColumnTableName, columnTableName, ColumnName.COLUMN_ID, ColumnName.ID));
				gr.add(newForeignKey(env, keyColumnTableName, columnTableName, ColumnName.REFERENCED_COLUMN_ID, ColumnName.ID));
			}
		}
		
		
		
		List<BaseTable> tables = getBaseTables(cat);
		
		for (BaseTable t : tables) {			
			
			
			
		}
		
		
		
		
			
		
		
		return gr;
	}

	private Statement newForeignKey(Environment env, Identifier baseTableTableName, Identifier schemaTableName,
			ColumnName col, ColumnName referenced) {
		
		final Identifier colname = toIdentifier(env, col);
		
		String fk = "fk_" + baseTableTableName.getContent() + "_" + colname.getContent();
		Identifier fkname = toIdentifier(env, fk);				
		
		Map<Identifier, Identifier> cm = Collections.singletonMap(colname, toIdentifier(env, referenced));
		
		AlterTableAddForeignKey afk = new AlterTableAddForeignKey(
				newMetaSchemaElementName(env, baseTableTableName), newMetaSchemaElementName(env, schemaTableName), fkname, cm);
		
		return afk;
	}

	private CreateTable newSchemaTable(Environment env, Identifier name) {
		SchemaElementName tn = newMetaSchemaElementName(env, name);
									
		CreateTable ct = newCreateTable(tn, Arrays.<BaseTableElement>asList(
				newId(env),
				newName(env),
				newIdPrimaryKey(env, name)));
		
		return ct;
	}
	
	private BaseTableElement newIdPrimaryKey(Environment env, Identifier tableName) {
		return newPrimaryKey(env, tableName, ColumnName.ID);
	}

	private BaseTableElement newPrimaryKey(Environment env, Identifier tableName, ColumnName column) {
		String pkname = "pk_" + tableName.getContent();
		Identifier pknm = toIdentifier(env, pkname);
		BaseTableElement pkc = new PrimaryKeyConstraint(pknm, toIdentifier(env, column));
		return pkc;
	}
	
	private CreateTable newBaseTableMetaTable(Environment env, Identifier tableName) {
		SchemaElementName tn = newMetaSchemaElementName(env, tableName);
							
		CreateTable ct = newCreateTable(tn, Arrays.<BaseTableElement>asList(
				newId(env),
				newName(env),				
				newSchemaRef(env),
				newIdPrimaryKey(env, tableName)));
		
		return ct;
	}
	
	private CreateTable newViewMetaTable(Environment env, Identifier tableName) {
		SchemaElementName tn = newMetaSchemaElementName(env, tableName);
							
		CreateTable ct = newCreateTable(tn, Arrays.<BaseTableElement>asList(
				newId(env),
				newName(env),				
				newSchemaRef(env),
				newIdPrimaryKey(env, tableName)));
		
		return ct;
	}
	
	private CreateTable newDataTypeMetaTable(Environment env, Identifier tableName) {
		SchemaElementName tn = newMetaSchemaElementName(env, tableName);
							
		CreateTable ct = newCreateTable(tn, Arrays.<BaseTableElement>asList(
				newId(env),
				newName(env),
				newVarchar(env, ColumnName.DEFAULT_VALUE, DEFAULT_VALUE_LENGTH),
				newInt(env, ColumnName.TYPE),
				newInt(env, ColumnName.SIZE),
				newInt(env, ColumnName.ORDINAL),
				newInt(env, ColumnName.NUM_PREC_RADIX),
				newInt(env, ColumnName.CHAR_OCTET_LENGTH),
				newInt(env, ColumnName.DECIMAL_DIGITS),
				newIdPrimaryKey(env, tableName)));
		
		return ct;
	}	
	
	private CreateTable newColumnMetaTable(Environment env, Identifier tableName) {
		SchemaElementName tn = newMetaSchemaElementName(env, tableName);
							
		CreateTable ct = newCreateTable(tn, Arrays.<BaseTableElement>asList(
				newId(env),
				newTableRef(env),				
				newDataTypeRef(env),
				newName(env),
				newIdPrimaryKey(env, tableName)));
		
		return ct;
	}
	
	private CreateTable newTableConstraintMetaTable(Environment env, Identifier tableName) {
		SchemaElementName tn = newMetaSchemaElementName(env, tableName);
							
		CreateTable ct = newCreateTable(tn, Arrays.<BaseTableElement>asList(
				newId(env),
				newTableRef(env),
				newName(env),
				newIdPrimaryKey(env, tableName)));
		
		return ct;
	}
	
	private CreateTable newKeyColumnMetaTable(Environment env, Identifier tableName) {
		SchemaElementName tn = newMetaSchemaElementName(env, tableName);
							
		CreateTable ct = newCreateTable(tn, Arrays.<BaseTableElement>asList(
				newId(env),
				newInt(env, ColumnName.TABLE_CONSTRAINT_ID),
				newInt(env, ColumnName.ORDINAL),
				newInt(env, ColumnName.COLUMN_ID),
				newInt(env, ColumnName.REFERENCED_COLUMN_ID),				
				newIdPrimaryKey(env, tableName)));
		
		return ct;
	}
	
	protected ColumnDefinition newId(Environment env) {
		return newInt(env, ColumnName.ID);
	}
	
	protected ColumnDefinition newName(Environment env) {
		return newVarchar(env, ColumnName.NAME, NAME_LENGTH);
	}
	
	protected ColumnDefinition newSchemaRef(Environment env) {
		return newInt(env, ColumnName.SCHEMA_ID);
	}
	
	protected ColumnDefinition newTableRef(Environment env) {
		return newInt(env, ColumnName.TABLE_ID);
	}
	
	protected ColumnDefinition newDataTypeRef(Environment env) {
		return newInt(env, ColumnName.DATA_TYPE_ID);
	}
	
	protected ColumnDefinition newInt(Environment env, ColumnName col) {
		return newInt(env, col.toString());
	}
	protected ColumnDefinition newVarchar(Environment env, ColumnName col, int length) {
		return newVarchar(env, col.toString(), length);
	}


	private SchemaElementName newMetaSchemaElementName(Environment env, Identifier name) {
		Identifier schemaName = toIdentifier(env, MetaData.SCHEMA_NAME);
		return new SchemaElementName(schemaName, name);			
	}
	
	public Identifier toIdentifier(Environment env, TableName name) {
		return toIdentifier(env, name.toString().toLowerCase());
	}

	protected Identifier toIdentifier(Environment env, ColumnName name) {
		return toIdentifier(env, name.toString().toLowerCase());
	}

	
}