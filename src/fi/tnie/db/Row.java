/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public interface Row<C extends Enum<C>> {
	
  public abstract Object get(C column);
  abstract RowMetaData getMetaData();
  
}
 