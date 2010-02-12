/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public interface Row {
	
  public abstract Object get(int ordinal);  
  abstract RowMetaData getMetaData();
  
}
 