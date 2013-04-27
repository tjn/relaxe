/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.util;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Schema;


public class CatalogPrinter {

	private PrintWriter writer;
	private static Logger logger = Logger.getLogger(CatalogPrinter.class);
	
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
				
				logger().info(indent(indent) + fk.getUnqualifiedName().getName());			
				
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
			logger().info(s.getUnqualifiedName().getName());
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
