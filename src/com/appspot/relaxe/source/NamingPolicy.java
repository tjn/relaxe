/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.source;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Schema;

public interface NamingPolicy {

	String getTemplateDir(Schema schema);
	String getTemplate(BaseTable table);
	String getTableStyle(BaseTable t);
	String getAttributeStyle(BaseTable t);
	String getAttributeStyle(Column c);	
	String getAttributeTypeStyle(DataType t);
	String getAttributeSizeStyle(DataType t);
	String getLabelIdentifier(BaseTable t, Column c);
	String getLabelText(BaseTable t, Column c);
	String getValueIdentifier(BaseTable t, Column c);
	
	String getLabelIdentifier(ForeignKey fk);
	String getLabelText(ForeignKey fk);
	
	String getValueIdentifier(ForeignKey t, Column c);
	String getSelectorIdentifier(ForeignKey fk, String id);
	String getResetIdentifier(ForeignKey fk, String id);
	
}
