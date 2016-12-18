package com.appspot.relaxe;

import java.io.InputStream;

public interface InputStreamSource {

	public InputStream getInputStream(InputStreamKey key);
	
}
