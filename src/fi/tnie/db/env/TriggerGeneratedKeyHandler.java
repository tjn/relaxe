/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.env;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

import fi.tnie.db.QueryHelper;
import fi.tnie.db.ValueExtractor;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;

public class TriggerGeneratedKeyHandler 
    extends DefaultGeneratedKeyHandler {
		
	private String seriesTableSchema;
	private String seriesTableName;
	
	private String schemaColumn;
	private String tableColumn;
	private String columnColumn;
	private String seriesColumn;	
		
	private static Logger logger = Logger.getLogger(TriggerGeneratedKeyHandler.class);
	    
	public TriggerGeneratedKeyHandler(ValueExtractorFactory valueExtractorFactory, 
			String seriesTableSchema, String seriesTableName, 
			String schemaColumn, String tableColumn, String columnColumn, String seriesColumn) {
		super(valueExtractorFactory);		
		this.seriesTableSchema = seriesTableSchema;
		this.seriesTableName = seriesTableName;
		
		this.schemaColumn = schemaColumn;
		this.tableColumn = tableColumn;
		this.columnColumn = columnColumn;
		this.seriesColumn = seriesColumn;
	}
	
	@Override
	public <
		A extends Attribute, 
		R extends Reference, 
		T extends fi.tnie.db.types.ReferenceType<A,R,T,E,?,?,M,?>, 
		E extends fi.tnie.db.ent.Entity<A,R,T,E,?,?,M,?>, 
		M extends fi.tnie.db.ent.EntityMetaData<A,R,T,E,?,?,M,?>
	> 
	void processGeneratedKeys(fi.tnie.db.expr.InsertStatement ins, final E target, final Statement statement) 
		throws java.sql.SQLException {
		
		logger().debug("processGeneratedKeys - enter");
		
		SchemaElementName n = ins.getTarget().getName();
		String schema = n.getQualifier().getSchemaName().getName();
		String table = n.getUnqualifiedName().getName();
		
		M meta = target.getMetaData();
		
		Connection c = statement.getConnection();
				
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT ");
		buf.append(columnColumn);
		buf.append(", ");
		buf.append(seriesColumn);
		buf.append(" FROM ");
		buf.append(seriesTableSchema).append(".").append(seriesTableName);
		buf.append(" WHERE ");
		buf.append(schemaColumn).append(" = ? AND ");
		buf.append(tableColumn).append(" = ?");	
		
		
		PreparedStatement ps = null;
		ResultSet keys = null;
		
		try {
			ps = c.prepareStatement(buf.toString());
			ps.setString(1, schema);
			ps.setString(2, table);
			keys = ps.executeQuery();
					
			ValueExtractorFactory vef = getValueExtractorFactory();
			
			final BaseTable t = meta.getBaseTable();
			
			final int nc = 1;
			final int vc = 2; 
					
			while (keys.next()) {
				String col = keys.getString(nc);
				Column column = t.columnMap().get(col);
				A a = meta.getAttribute(column);			
				PrimitiveKey<A, E, ?, ?, ?, ?> key = meta.getKey(a);
				ValueExtractor<?, ?, ?> ve = vef.createExtractor(column.getDataType(), vc);									
				write(key.self(), ve, keys, target);			
			}
		}
		finally {
			QueryHelper.doClose(keys);
			QueryHelper.doClose(ps);
		}
	}
	
	private static Logger logger() {
		return TriggerGeneratedKeyHandler.logger;
	}
}