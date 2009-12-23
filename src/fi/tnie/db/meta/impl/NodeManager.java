package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class NodeManager<N extends Node<P>, P extends NodeContainer<N>> {
	
	private TreeMap<String, N> nodeMap;
	
	private static Logger logger = Logger.getLogger(NodeManager.class);
	
//	private NodeManager<DefaultMutableTable, DefaultMutableSchema> parentNodeManager = 
//		new NodeManager<DefaultMutableTable, DefaultMutableSchema>();	
	
	boolean add(P parent, N t) {
		if (t == null) {
			throw new NullPointerException();
		}
		
		P p = t.getParentNode();
		
		if (p == parent) {
			return false;
		}
		
		if (p != null) {
			p.remove(t);
		}
		
		getNodeMap().put(t.getName(), t);
		t.attach(parent);
		
//		logger().debug("put: " + t.getName() + " in " + parent);
		
		return true;		
	}
	
	boolean remove(N n) {
		n.detach();
		return (getNodeMap().remove(n.getName()) != null);		
	}
	
	void setParent(N n, P newParent) {
		P p = n.getParentNode();
		
		if (newParent == p) {
			logger().debug("parent match => no add: " + p);
			return;
		}		
				
		if (p != null) {
			p.remove(n);
		}
		
		if (newParent == null) {
			n.detach();
		}
		else {
			newParent.add(n);
		}
	}
	
	public Map<String, N> nodes() {
		if (this.nodeMap == null) {
			return Collections.emptyMap();
		}
		
		return Collections.unmodifiableMap(getNodeMap());
	}

	private Map<String, N> getNodeMap() {
		if (nodeMap == null) {
			nodeMap = new TreeMap<String, N>();			
		}

		return nodeMap;
	}
	
	public static Logger logger() {
		return NodeManager.logger;
	}

}
