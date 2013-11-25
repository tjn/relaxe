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
