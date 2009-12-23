package fi.tnie.db.meta;

import fi.tnie.db.meta.impl.DefaultMutableSchema;

public interface MetaElement
	extends MetaObject {
	Schema getSchema();
	public void detach();
	public void attach(DefaultMutableSchema s);
	
	String getQualifiedName();
	public String getQualifiedName(boolean quote);
}
