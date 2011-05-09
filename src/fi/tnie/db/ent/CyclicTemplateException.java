/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

public class CyclicTemplateException extends Exception {

	private Entity<?, ?, ?, ?, ?, ?, ?> entity; 
	
	public CyclicTemplateException(Entity<?, ?, ?, ?, ?, ?, ?> template) {
		setEntity(template);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8336918826639436436L;

	public Entity<?, ?, ?, ?, ?, ?, ?> getEntity() {
		return entity;
	}

	private void setEntity(Entity<?, ?, ?, ?, ?, ?, ?> entity) {
		this.entity = entity;
	}

}
