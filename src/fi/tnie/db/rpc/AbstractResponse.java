/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

import fi.tnie.db.ent.Request;
import fi.tnie.db.ent.Response;

public abstract class AbstractResponse<R extends Request>
	implements Response<R>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4299109127097016759L;
	private R request;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected AbstractResponse() {
	}
	
	public AbstractResponse(R request) {
		super();
		
		if (request == null) {
			throw new NullPointerException("request");
		}		
		
		this.request = request;
	}

	public R getRequest() {
		return request;
	}

}
