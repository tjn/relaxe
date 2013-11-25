/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.source;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Schema;

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
	public String getLabelText(BaseTable t, Column c) {
		return c.getColumnName().getName();
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
