/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
/**
 * 
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.ValueExtractor;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;


public class TriggerGeneratedKeyHandler 
    extends DefaultGeneratedKeyHandler {
		
	private String seriesTableSchema;
	private String seriesTableName;
	
	private String schemaColumn;
	private String tableColumn;
	private String columnColumn;
	private String seriesColumn;	
		
	private static Logger logger = LoggerFactory.getLogger(TriggerGeneratedKeyHandler.class);
	    
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
		T extends com.appspot.relaxe.types.ReferenceType<A,R,T,E,?,?,M>, 
		E extends com.appspot.relaxe.ent.Entity<A,R,T,E,?,?,M>, 
		M extends com.appspot.relaxe.ent.EntityMetaData<A,R,T,E,?,?,M>
	> 
	void processGeneratedKeys(com.appspot.relaxe.expr.InsertStatement ins, final E target, final Statement statement) 
		throws java.sql.SQLException {
		
		logger().debug("processGeneratedKeys - enter");
		
		SchemaElementName n = ins.getTarget().getName();
		String schema = n.getQualifier().getSchemaName().getContent();
		String table = n.getUnqualifiedName().getContent();
		
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
				Column column = t.getColumnMap().get(col);
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