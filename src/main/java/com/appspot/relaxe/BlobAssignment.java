package com.appspot.relaxe;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.appspot.relaxe.types.BlobType;

public class BlobAssignment
	extends AbstractParameterAssignment<InputStreamKey, BlobType, InputStreamKeyHolder> {
		
	private AssignContext context;
			
	public BlobAssignment(InputStreamKeyHolder holder, AssignContext context) {
		super(holder);		
		this.context = context;		
	}

	@Override
	public void assign(PreparedStatement ps, int ordinal, InputStreamKey key) throws SQLException, IOException {
				
		
		if (key == null) {			
			ps.setNull(ordinal, BlobType.TYPE.getSqlType());			
		}
		else {			
			InputStream in = this.context.getInputStream(key);			
			ps.setBlob(ordinal, in);
/*			
			OutputStream out =  blob.setBinaryStream(1);
			
			byte[] buffer = new byte[1024];
			
			int n;
			long len = 0;
			
			try {			
				while ((n = in.read(buffer)) >= 1) {
					out.write(buffer, 0, n);
					len += n;					
				}
				
				ps.setBlob(ordinal, blob);
				
				this.context.setLength(key, len);
			} 
			finally {
				 out.close();
			}
			*/			
		}
		
	}

}
