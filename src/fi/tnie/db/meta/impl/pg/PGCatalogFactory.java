/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.meta.impl.DefaultCatalogFactory;

public class PGCatalogFactory
	extends DefaultCatalogFactory {

	public PGCatalogFactory(PGEnvironment environment) {
		super(environment);
	}
	
	private String catalog;
	
	@Override
	protected void prepare(DatabaseMetaData m) 
		throws SQLException {
		
		if (catalog != null) {
			throw new IllegalStateException("catalog creation is in progress");
		}
		
		this.catalog = m.getConnection().getCatalog();
	}	

	@Override
	protected String getCatalogNameFromSchemas(DatabaseMetaData meta,
			ResultSet schemas) throws SQLException {
		return this.catalog;
	}
	
	@Override
	protected String getCatalogNameFromTables(DatabaseMetaData meta, ResultSet tables)
			throws SQLException {		
		String cn = super.getCatalogNameFromTables(meta, tables);		
		return (cn == null) ? this.catalog : cn;
	}
	
	@Override
	protected String getReferencedTableCatalogName(DatabaseMetaData meta,
			ResultSet fkcols) throws SQLException {		
		String cn = super.getReferencedTableCatalogName(meta, fkcols);		
		return (cn == null) ? this.catalog : cn;		
	}
	
	@Override
	protected void finish() {		
		this.catalog = null;		
	}
}
