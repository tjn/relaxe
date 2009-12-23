package fi.tnie.db.meta.impl;

public interface Node<P extends NodeContainer<?>> {	
	P getParentNode();
	void detach();
	void attach(P newParent);
	String getName();
	
	public NodeManager<?, ?> getParentNodeManager();
}
