/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
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
package com.appspot.relaxe.env.util;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Schema;


public class CatalogPrinter {

	private PrintWriter writer;
	private static Logger logger = LoggerFactory.getLogger(CatalogPrinter.class);
	
	public CatalogPrinter(PrintWriter writer) {
		super();
		
		if (writer == null) {
			this.writer = new PrintWriter(System.out);
		}
	}
	
	
	public void setCatalog(Catalog c) {
		print(c, this.writer, 0);
	}

	private void print(BaseTable b, PrintWriter w, int indent, Set<String> visited) {
			
		
		for (ForeignKey fk : b.foreignKeys().values()) {
			String n = fk.getReferenced().getQualifiedName();
			
			if (!visited.contains(n)) {
				visited.add(n);
				
				logger().info(indent(indent) + fk.getUnqualifiedName().getContent());			
				
				if (fk.getReferenced() == null) {
					throw new NullPointerException(fk.getName() + ": 'fk.getReferenced()' must not be null");
				}				
				
				if (fk.getReferenced() == b) {
					logger().info(indent(indent + 1) + "<self>");
				}
				else {					
					logger().info(indent(indent + 1) + n);
					print(fk.getReferenced(), w, indent + 1, visited);
				}
			}
		}		
	}

	private void print(Catalog c, PrintWriter w, int indent) {		
		for (Schema s : c.schemas().values()) {			
			logger().info(s.getUnqualifiedName().getContent());
			print(s, w, indent + 1);			
		}
	}

	private void print(Schema s, PrintWriter w, int indent) {
		Set<String> visited = new HashSet<String>();
		
		for (BaseTable t : s.baseTables().values()) {			
			logger().info(indent(indent) + t.getQualifiedName());
			visited.clear();
			print(t, w, indent + 1, visited);
		}
	}

	private String indent(int level) {
		char[] pad = new char[level * 2];
		Arrays.fill(pad, ' ');
		return String.copyValueOf(pad);
	}


	public static Logger logger() {
		return CatalogPrinter.logger;
	}
}
