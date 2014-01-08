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
package com.appspot.relaxe.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.appspot.relaxe.env.CatalogFactory;
import com.appspot.relaxe.env.DefaultResolver;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.ImplementationResolver;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Schema;

public class ForeignKeyListSample {

	public static void main(String[] args) {
		
		if (args.length < 3) {
			System.err.println("usage: java " + ForeignKeyListSample.class.getCanonicalName() + " <username> <password> <jdbc-url>");
			return;
		}
		
		String user = args[0];
		String password = args[1];
		String jdbcURL = args[2];
		
		ImplementationResolver r = new DefaultResolver();
		Implementation<?> imp = r.resolve(jdbcURL);
		
		if (imp == null) {
			System.err.println("Can not find implementation for JDBC URL: " + jdbcURL);
			return;
		}
		
		Connection c = null;
		
		try {
			try {			
				Properties cfg = imp.getProperties();
				cfg.setProperty("user", user);
				cfg.setProperty("password", password);
										
				c = DriverManager.getConnection(jdbcURL, cfg);			
				
				CatalogFactory cf = imp.catalogFactory();
				
				Catalog catalog = cf.create(c);
												
				Identifier cn = catalog.getName();
				
				System.out.println((cn == null) ? "<unnamed catalog>" : cn);
				
				int indent = 0;
				boolean relative = true;
								
				for (Schema schema : catalog.schemas().values()) {
					println(indent, schema.getName());
					
					for (ForeignKey fk : schema.foreignKeys().values()) {						
						BaseTable referencing = fk.getReferencing();
						BaseTable referenced = fk.getReferenced();
																		
						println(indent = 1, 
								fk.getQualifiedName(), ": ", 
								referencing.getName(relative), " => ", referenced.getName(relative));
						
						ColumnMap cm = fk.getColumnMap();						
						
						for (Column col : cm.values()) {
							Identifier rcol = fk.getReferencedColumnName(col.getUnqualifiedName());							
							println(indent = 2, col.getUnqualifiedName(), " => ", rcol);
						}
					}
				}				
			}
			finally {
				if (c != null) {
					c.close();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void println(int indent, Object ... values) {
		for (int i = 0; i < indent; i++) {
			System.out.print("  ");
		} 
		
		for (int i = 0; i < values.length; i++) {
			System.out.print(values[i]);
		}
		
		System.out.println();
	}
}
