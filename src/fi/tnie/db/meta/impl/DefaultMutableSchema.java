/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;


import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;

public class DefaultMutableSchema 
	extends DefaultMutableMetaObject
	implements Schema, Node<DefaultMutableCatalog>, NodeContainer<DefaultMutableTable> {
		
//	private TreeMap<String, Table> tableMap;
	private DefaultMutableCatalog catalog;
	
	private NodeManager<DefaultMutableTable, DefaultMutableSchema> nodeManager = 
		new NodeManager<DefaultMutableTable, DefaultMutableSchema>();
			
	private NodeManager<DefaultMutableSchema, DefaultMutableCatalog> parentNodeManager = 
		new NodeManager<DefaultMutableSchema, DefaultMutableCatalog>();
	
	private static Logger logger = Logger.getLogger(DefaultMutableSchema.class);
			
	public DefaultMutableSchema() {
		super();
	}
	
	public DefaultMutableSchema(String n) {
		super(n);		
	}

//	@Override
//	public AbstractMetaObject getParent() {
//		return null;
//	}
		
	public boolean add(DefaultMutableTable t) {
//		logger().debug("adding: " + t.getName());
		boolean added = nodeManager.add(this, t);
//		logger().debug("added ? " + added);
//		logger().debug("nodes: " + nodeManager.nodes().size());
		return added;
		
//		if (t == null) {
//			throw new NullPointerException();
//		}
//		
//		SchemaImpl s = t.getSchema();
//		
//		if (s == this) {
//			return false;
//		}
//		
//		if (s != null) {
//			s.remove(t);
//		}
//		
//		getTableMap().put(t.getName(), t);
//		t.attach(this);
//		return true;
	}
		
	public boolean remove(DefaultMutableTable t) {
		return nodeManager.remove(t);
//		if (t == null) {
//			throw new NullPointerException();
//		}
//		
//		t.detach();
//		getTableMap().remove(t.getName());		
	}


//	private TreeMap<String, Table> getTableMap() {
//		if (tableMap == null) {
//			tableMap = new TreeMap<String, Table>();			
//		}
//
//		return tableMap;
//	}
	
	

	@Override
	public Catalog getCatalog() {
		return getCatalogImpl();
	}

	@Override
	public Map<String, Table> tables() {
		return new TreeMap<String, Table>(nodeManager.nodes());
	}

	DefaultMutableCatalog getCatalogImpl() {
		return catalog;
	}

	public void attach(DefaultMutableCatalog catalogImpl) {
		this.catalog = catalogImpl;		
	}

	@Override
	public void detach() {
		this.catalog = null;
	}

	@Override
	public DefaultMutableMetaObject getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefaultMutableCatalog getParentNode() {
		return getCatalogImpl();
	}

	NodeManager<DefaultMutableTable, DefaultMutableSchema> getNodeManager() {
		return nodeManager;
	}
	
	public NodeManager<DefaultMutableSchema, DefaultMutableCatalog> getParentNodeManager() {
		DefaultMutableCatalog c = getCatalogImpl();
		return (c == null) ? this.parentNodeManager : c.getNodeManager();
	}
	
	public Map<String, BaseTable> baseTables() {
		Map<String, DefaultMutableTable> n = nodeManager.nodes();
		
//		logger().debug("table-nodes: " + n.size());
		
		if (n.isEmpty()) {
			return Collections.emptyMap();
		}
		
		TreeMap<String, BaseTable> tables = new TreeMap<String, BaseTable>();
		
		for (Table t : n.values()) {			
//			logger().debug("table " + t.getName() + " base table ? " + t.isBaseTable());
			
			if (t.isBaseTable()) {
				tables.put(t.getName(), (BaseTable) t);
			}
		}
		
		return tables;
	}
		
	public static Logger logger() {
		return DefaultMutableSchema.logger;
	}
	
	@Override
		public String toString() {		
			return super.toString() + ": " + this.getName();
		}
}
