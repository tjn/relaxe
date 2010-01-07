/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.List;

public interface SelectListElement
	extends Element {

	List<? extends ColumnName> getColumnNames();
	public int getColumnCount();
}
