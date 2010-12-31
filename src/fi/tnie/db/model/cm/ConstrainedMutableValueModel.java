/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;

import fi.tnie.db.model.MutableValueModel;

public interface ConstrainedMutableValueModel<V>
	extends ConstrainedValueModel<V>, MutableValueModel<V> {

}
