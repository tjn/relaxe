/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;

public class DefaultNamingPolicy
	implements NamingPolicy {
	
	
	private String templateExtension;

	@Override
	public String getAttributeStyle(BaseTable t) {
		return null;
	}

	@Override
	public String getAttributeStyle(Column c) {
		return "attr";
	}

	@Override
	public String getAttributeTypeStyle(DataType t) {		
		String n = t.getTypeName();		
		n = toClass(n);
		return n;
	}
	
	@Override
	public String getAttributeSizeStyle(DataType t) {		
		int sz = t.getSize();
		return toClass("asz" + sz);		
	}

	@Override
	public String getLabelIdentifier(BaseTable t, Column c) {		
		String n = c.getColumnName().getName();		
		n = toClass("meta." + n);
		return n;
	}

	private String toClass(String n) {
		n = toIdentifier(n);
		return n;
	}


	@Override
	public String getTableStyle(BaseTable t) {
		String n = t.getUnqualifiedName().getName();
		
		return n;
	}

	@Override
	public String getTemplate(BaseTable table) {
		String n = table.getUnqualifiedName().getName();				
		n = toIdentifier(n + getTemplateExtension());		
		return n;
	}

	@Override
	public String getTemplateDir(Schema schema) {
		String n = schema.getUnqualifiedName().getName();				
		return toIdentifier(n);
	}

	@Override
	public String getValueIdentifier(BaseTable t, Column c) {
		String n = c.getColumnName().getName();		
		n = toIdentifier(n);				
		return n;
	}

	private String toIdentifier(String n) {
		n = n.toLowerCase();
		n = n.replace('_', '-');
		return n;
	}

	public String getTemplateExtension() {
		if (templateExtension == null) {
			templateExtension = ".html";			
		}

		return templateExtension;
	}

	public void setTemplateExtension(String templateExtension) {
		this.templateExtension = templateExtension;
	}
	
	
	@Override
	public String getLabelIdentifier(ForeignKey fk) {
		return toIdentifier("meta." + fk.getUnqualifiedName().getName());
	}
	
	@Override
	public String getLabelText(ForeignKey fk) {	
		// TODO:
		return fk.getUnqualifiedName().getName();
	}
	
	@Override
	public String getResetIdentifier(ForeignKey fk, String id) {	
		return toIdentifier(id + ".reset");
	}
	
	@Override
	public String getSelectorIdentifier(ForeignKey fk, String id) {
		return toIdentifier(id + ".selector");
	}
	
	@Override
	public String getValueIdentifier(ForeignKey t, Column c) {
		return toIdentifier(t.getUnqualifiedName().getName() + "." + c.getUnqualifiedName().getName());
	}

}
