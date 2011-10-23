/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;

public interface NamingPolicy {

	String getTemplateDir(Schema schema);
	String getTemplate(BaseTable table);
	String getTableStyle(BaseTable t);
	String getAttributeStyle(BaseTable t);
	String getAttributeStyle(Column c);	
	String getAttributeTypeStyle(DataType t);
	String getAttributeSizeStyle(DataType t);
	String getLabelIdentifier(BaseTable t, Column c);
	String getValueIdentifier(BaseTable t, Column c);
	
	String getLabelIdentifier(ForeignKey fk);
	String getLabelText(ForeignKey fk);
	
	String getValueIdentifier(ForeignKey t, Column c);
	String getSelectorIdentifier(ForeignKey fk, String id);
	String getResetIdentifier(ForeignKey fk, String id);
	
}
