package com.appspot.relaxe;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteArrayExtractor
{
	
	private int column;
	
		
	public ByteArrayExtractor(int column) {
		super();
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public long extract(ResultSet rs, OutputStream out)
		throws SQLException, IOException {
		
		byte[] buf = rs.getBytes(column);
		out.write(buf);
		return buf.length;
	}


}
