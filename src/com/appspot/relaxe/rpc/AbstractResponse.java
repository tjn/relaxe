/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.ent.Request;
import com.appspot.relaxe.ent.Response;


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

	@Override
	public R getRequest() {
		return request;
	}

}
