/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Schema;

public class DefaultMutableCatalog extends DefaultMutableMetaObject 
	implements Catalog, NodeContainer<DefaultMutableSchema> {
	
	private String name; 
	
	
	public DefaultMutableCatalog() {
		super();
	}

	public DefaultMutableCatalog(String name) {
		this();
		this.name = name;
	}

	private NodeManager<DefaultMutableSchema, DefaultMutableCatalog> nodeManager = 
		new NodeManager<DefaultMutableSchema, DefaultMutableCatalog>();	 
	
	@Override
	public DefaultMutableMetaObject getParent() {
		return null;
	}

	@Override
	public Map<String, Schema> schemas() {
		if (nodeManager.nodes().isEmpty()) {
			return Collections.emptyMap();
		}
		
		return new LinkedHashMap<String, Schema>(nodeManager.nodes());
	}
		
	public boolean add(DefaultMutableSchema t) {
		return nodeManager.add(this, t);				
	}
	
	public boolean remove(DefaultMutableSchema schema) {
		return nodeManager.remove(schema);
	}

	public NodeManager<DefaultMutableSchema, DefaultMutableCatalog> getNodeManager() {
		return nodeManager;
	}

	@Override
	public String getName() {
		return name;
	}
	
}
