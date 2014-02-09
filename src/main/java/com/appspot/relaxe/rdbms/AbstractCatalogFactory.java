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
package com.appspot.relaxe.rdbms;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaName;


public abstract class AbstractCatalogFactory
	implements CatalogFactory {
	
	private Environment environment;
	
	private static Logger logger = LoggerFactory.getLogger(AbstractCatalogFactory.class);
	
	public AbstractCatalogFactory(Environment environment) {
		super();
		this.environment = environment;
	}

	public String getCatalogNameFromSchemas(DatabaseMetaData meta, ResultSet schemas)
		throws SQLException {		
		return schemas.getString(2);
	}

	public String getSchemaNameFromSchemas(DatabaseMetaData meta, ResultSet schemas)
		throws SQLException {						
		return schemas.getString(1);
	}

	public String getSchemaNameFromTables(DatabaseMetaData meta, ResultSet tables)
			throws SQLException {
		return tables.getString(2);
	}

	public String getCatalogNameFromTables(DatabaseMetaData meta, ResultSet tables)
			throws SQLException {
		return tables.getString(1);		
	}

	public String getReferencingTableCatalogName(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {		
		return fkcols.getString(5);
	}

	public String getReferencingTableSchemaName(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {		
		return fkcols.getString(6);
	}

	public String getSchemaNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols)
			throws SQLException {
		return pkcols.getString(2);		
	}

	public String getCatalogNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols)
			throws SQLException {
	    return pkcols.getString(1);     
	}

	public String getCatalogNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {
	    return fkcols.getString(5);     
	}

	protected String getCatalogPattern(SchemaName s) {		
		Identifier cn = (s == null) ? null : s.getCatalogName();
		return (cn == null) ? null : cn.getContent();
	}

	protected String getSchemaPattern(SchemaName s) {
		Identifier sn = (s == null) ? null : s.getSchemaName();		
		return (sn == null) ? null : sn.getContent();
	}
	
	protected String getPrimaryKeyCatalogNameParameter(SchemaName s) {		
		return getCatalogNameParameter(s);
	}
	
	protected String getPrimaryKeySchemaNameParameter(SchemaName s) {		
		return getSchemaNameParameter(s);
	}
	
	protected String getForeignKeyCatalogNameParameter(SchemaName s) {
		return getCatalogNameParameter(s);
	}
	
	protected String getForeignKeySchemaNameParameter(SchemaName s) {		
		return getSchemaNameParameter(s);
	}

	
	protected String getCatalogNameParameter(SchemaName s) {		
		Identifier cn = (s == null) ? null : s.getCatalogName();
		return (cn == null) ? null : cn.getContent();
	}
	
	protected String getSchemaNameParameter(SchemaName s) {		
		Identifier n = (s == null) ? null : s.getSchemaName();
		return (n == null) ? null : n.getContent();
	}


	public Environment getEnvironment() {
		return environment;
	}

	@Override
	public String getCatalogName(Connection c) throws SQLException {
	    return c.getCatalog();
	}

	protected ResultSet close(ResultSet rs) {		
		try {
			if (rs != null) {
				rs.close();
			}
		} 
		catch (SQLException e) {
			logger().warn(e.getMessage(), e);
		}
		
		return null;
	}
	
	public ResultSet getSchemas(DatabaseMetaData meta) throws SQLException {                      
        return meta.getSchemas();
    }

	
	protected String getSchemaNameFromImportedKeys(DatabaseMetaData meta, ResultSet fkcols)
			throws SQLException {
				return fkcols.getString(2);		
			}

	private static Logger logger() {
		return AbstractCatalogFactory.logger;
	}
}
