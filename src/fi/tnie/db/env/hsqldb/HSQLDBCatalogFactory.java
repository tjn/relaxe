/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.hsqldb;

import fi.tnie.db.env.DefaultCatalogFactory2;
import fi.tnie.db.meta.impl.hsqldb.HSQLDBEnvironment;

public class HSQLDBCatalogFactory
	extends DefaultCatalogFactory2 {

	public HSQLDBCatalogFactory(HSQLDBEnvironment env) {
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
//	
//	@Override
//	public String getCatalogNameFromPrimaryKeys(DatabaseMetaData meta, ResultSet pkcols) throws SQLException {
//	    return meta.getConnection().getCatalog();
//	};
//	
//	
//	@Override
//	public String getCatalogNameFromImportedKeys(DatabaseMetaData meta,
//	        ResultSet fkcols) throws SQLException {
//	    return getCatalog(meta);
//	}
//	
//	@Override
//    public String getCatalogNameFromSchemas(DatabaseMetaData meta,
//			ResultSet schemas) throws SQLException {
//	    return getCatalog(meta);
//	}	
//	
//	@Override
//    public String getCatalogNameFromTables(DatabaseMetaData meta, ResultSet tables)
//			throws SQLException {		
//		String cn = super.getCatalogNameFromTables(meta, tables);		
//		return (cn == null) ? getCatalog(meta) : cn;
//	}
//	
//	@Override
//    public String getReferencedTableCatalogName(DatabaseMetaData meta, ResultSet fkcols) throws SQLException {		
//		String cn = super.getReferencedTableCatalogName(meta, fkcols);		
//		return (cn == null) ? getCatalog(meta) : cn;		
//	}
//	
//	@Override
//	protected void finish() {		
//		this.catalog = null;		
//	}
//	
//	private String getCatalog(DatabaseMetaData meta) 
//	    throws SQLException {
//	    return meta.getConnection().getCatalog();
//	}
}
