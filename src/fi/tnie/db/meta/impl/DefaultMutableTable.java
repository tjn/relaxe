package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public abstract class DefaultMutableTable
	extends DefaultMutableMetaElement 
	implements Table,	Node<DefaultMutableSchema>, NodeContainer<DefaultMutableColumn> {
	
	private NodeManager<DefaultMutableColumn, DefaultMutableTable> nodeManager = 
		new NodeManager<DefaultMutableColumn, DefaultMutableTable>();
	
	private NodeManager<DefaultMutableTable, DefaultMutableSchema> parentNodeManager = 
		new NodeManager<DefaultMutableTable, DefaultMutableSchema>();
				
	public DefaultMutableTable() {
		this(null, null);
	}
		
	public DefaultMutableTable(DefaultMutableSchema s, String name) {
		super(null, name);
		setSchema(s);				
	}
		
	void setSchema(DefaultMutableSchema ns) {		
		getParentNodeManager().setParent(this, ns);
		setSchemaImpl(ns);
	}
	
	public boolean add(DefaultMutableColumn c) {
		return nodeManager.add(this, c);		
	}
	
	public boolean remove(DefaultMutableColumn c) {
		return nodeManager.remove(c);
	}	
	
	@Override
	public DefaultMutableSchema getParentNode() {
		return super.getSchemaImpl();
	}

	NodeManager<DefaultMutableColumn, DefaultMutableTable> getNodeManager() {
		return nodeManager;
	}

	@Override
	public NodeManager<DefaultMutableTable, DefaultMutableSchema> getParentNodeManager() {
		DefaultMutableSchema s = getSchemaImpl();
		
		return (s == null) ? parentNodeManager : s.getNodeManager();
	}
	
	@Override
	public Map<String, Column> columns() {
		return Collections.unmodifiableMap(new LinkedHashMap<String, Column>(nodeManager.nodes()));		
	}	
	
	public DefaultMutableBaseTable asBaseTable() {
		return (DefaultMutableBaseTable) (isBaseTable() ? this : null);
	}
}
