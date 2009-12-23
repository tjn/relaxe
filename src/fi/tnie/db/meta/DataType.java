package fi.tnie.db.meta;


public interface DataType {
	int getDataType();
//	short getSourceDataType();
	String getTypeName();
	int getCharOctetLength();
	int getDecimalDigits();
	int getNumPrefixRadix();
	int getSize();	

}
