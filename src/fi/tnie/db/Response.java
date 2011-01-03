/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

public abstract class Response<R extends Request>
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4299109127097016759L;
	private R request;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected Response() {
	}
	
	public Response(R request) {
		super();
		
		if (request == null) {
			throw new NullPointerException("request");
		}		
	}

	public R getRequest() {
		return request;
	}

}
