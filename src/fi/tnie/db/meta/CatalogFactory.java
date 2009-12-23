package fi.tnie.db.meta;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public interface CatalogFactory {

	Catalog create(DatabaseMetaData meta, String catalog)
		throws SQLException;
	
}
