package fi.tnie.db.meta;

public interface Column 
	extends MetaObject {

	DataType getDataType();	
	boolean isPrimaryKeyColumn();
	Boolean isAutoIncrement();
}
