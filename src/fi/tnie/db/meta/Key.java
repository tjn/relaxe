package fi.tnie.db.meta;

import fi.tnie.db.meta.impl.DefaultMutableMetaObject;
import fi.tnie.db.meta.impl.DefaultMutableTable;

public class Key
	extends DefaultMutableMetaObject {
	
	private DefaultMutableTable table;
	

	@Override
	public DefaultMutableMetaObject getParent() {
		return null;
	}
	
}
