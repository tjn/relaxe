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
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;


public class SchemaElementNameComparator
	implements Comparator<SchemaElementName> {
	
	private Comparator<Identifier> identifierComparator;
	
	public SchemaElementNameComparator(Comparator<Identifier> identifierComparator) {
		if (identifierComparator == null) {
			throw new NullPointerException("identifierComparator");
		}		
		
		this.identifierComparator = identifierComparator;
	}

	@Override
	public int compare(SchemaElementName n1, SchemaElementName n2) {
		if (n1 == n2) {
			return 0;
		}
				
		SchemaName q1 = n1.getQualifier();
		SchemaName q2 = n2.getQualifier();
		
		int result;		
		
		if (q1 != q2) {
			result = compare(q1.getCatalogName(), q2.getCatalogName());
			
			if (result != 0) {
				return result;
			}
			
			result = compare(q1.getSchemaName(), q2.getSchemaName());
			
			if (result != 0) {
				return result;
			}			
		}
				
		result = this.identifierComparator.compare(n1.getUnqualifiedName(), n2.getUnqualifiedName());		
				
		return result;
	}

	private int compare(Identifier a, Identifier b) {
		return (a == b) ? 0 : this.identifierComparator.compare(a, b);
	}

	
}
