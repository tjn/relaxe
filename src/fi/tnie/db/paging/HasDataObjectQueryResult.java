/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;

public interface HasDataObjectQueryResult<O extends DataObject> {
	DataObjectQueryResult<O> getResult();
}
