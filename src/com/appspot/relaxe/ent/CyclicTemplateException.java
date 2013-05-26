/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;

public class CyclicTemplateException extends EntityRuntimeException
	implements Serializable {

	private EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> template;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected CyclicTemplateException() {
	}
	
	public CyclicTemplateException(EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> template) {
		setTemplate(template);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8336918826639436436L;

	public EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> getEntity() {
		return template;
	}

	private void setTemplate(EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> newTemplate) {
		this.template = newTemplate;
	}

}