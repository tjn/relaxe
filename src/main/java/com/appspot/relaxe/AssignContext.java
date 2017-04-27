package com.appspot.relaxe;

public interface AssignContext
	extends InputStreamSource {
		
	public void setLength(InputStreamKey key, long length);
	
	public long getLength(InputStreamKey key);

}
