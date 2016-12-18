package com.appspot.relaxe;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface BlobExtractor {

	public long extract(ResultSet rs, OutputStream out)
		throws SQLException, IOException;
	
}
