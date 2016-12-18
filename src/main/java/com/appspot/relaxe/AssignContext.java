package com.appspot.relaxe;

import java.io.InputStream;

public interface AssignContext
	extends InputStreamSource {
		
	public void setLength(InputStreamKey key, long length);
	
	public long getLength(InputStreamKey key);

}
