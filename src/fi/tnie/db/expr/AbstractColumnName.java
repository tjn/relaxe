/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class AbstractColumnName implements ColumnName {

	@Override
	public abstract String getColumnName();

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append(getColumnName());
//	}

}
