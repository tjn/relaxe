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
package com.appspot.relaxe.meta.impl.pg;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.env.DefaultCatalogFactory;
import com.appspot.relaxe.env.pg.PGEnvironment;


public class PGCatalogFactory
	extends DefaultCatalogFactory {

	public PGCatalogFactory(PGEnvironment env) {
		super(env);
	}
	
//	private String catalog;
//	
//	@Override
//	public void prepare(DatabaseMetaData m) 
//		throws SQLException {
//		
//		if (catalog != null) {
//			throw new IllegalStateException("catalog creation is in progress");
//		}
//		
//		this.catalog = m.getConnection().getCatalog();
//	}
	
	@Override
	public String getCatalogNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols) throws SQLException {
	    return meta.getConnection().getCatalog();
	};
	
	
	@Override
	public String getCatalogNameFromImportedKeys(DatabaseMetaData meta,
	        ResultSet fkcols) throws SQLException {
	    return getCatalog(meta);
	}
	
	@Override
    public String getCatalogNameFromSchemas(DatabaseMetaData meta,
			ResultSet schemas) throws SQLException {
	    return getCatalog(meta);
	}	
	
	@Override
    public String getCatalogNameFromTables(DatabaseMetaData meta, ResultSet tables)
			throws SQLException {		
		String cn = super.getCatalogNameFromTables(meta, tables);		
		return (cn == null) ? getCatalog(meta) : cn;
	}
	
	@Override
    public String getReferencingTableCatalogName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {		
		String cn = super.getReferencingTableCatalogName(meta, fkcols);		
		return (cn == null) ? getCatalog(meta) : cn;		
	}
	
//	@Override
//	protected void finish() {		
//		this.catalog = null;		
//	}
	
	private String getCatalog(DatabaseMetaData meta) 
	    throws SQLException {
	    return meta.getConnection().getCatalog();
	}
}
