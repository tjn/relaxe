package com.appspot.relaxe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultBlobExtractor
	implements BlobExtractor {

	
	private final int column;
	private final byte[] buffer;	
	
	public DefaultBlobExtractor(int col) {
		this(col, 256 * 1024);
	}
	
	public DefaultBlobExtractor(int col, int size) {
		if (size <= 0)  {
			throw new IllegalArgumentException(String.valueOf(size));
		}
		this.column = col;
		this.buffer = new byte[size];
	}

	@Override
	public long extract(ResultSet rs, OutputStream out) throws SQLException, IOException {
		Blob blob = rs.getBlob(this.column);
		
		if (blob == null) {
			return -1;
		}
		
		InputStream in = blob.getBinaryStream();
		
		int n;
		long total = 0;
		
		while ((n = in.read(this.buffer)) > 0) {			
			out.write(buffer, 0, n);
			total += n;			
		}		
		
		return total;
	}

}
