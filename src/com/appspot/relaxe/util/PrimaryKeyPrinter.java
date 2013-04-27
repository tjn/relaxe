/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.util;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.PrimaryKey;
import com.appspot.relaxe.meta.Schema;


public class PrimaryKeyPrinter {

	private PrintWriter writer;
	private static Logger logger = Logger.getLogger(PrimaryKeyPrinter.class);
	
	public PrimaryKeyPrinter(PrintWriter writer) {
		super();
		
		if (writer == null) {
			this.writer = new PrintWriter(System.out);
		}
	}

	
	public void setCatalog(Catalog c) {
		print(c, this.writer, 0);
	}

	private void print(BaseTable b, PrintWriter w, int indent, Set<String> visited) {		
		print(b.getQualifiedName(), w, indent);		
		indent++;		
		PrimaryKey pk = b.getPrimaryKey();
		
		if (pk == null) {
			print("<no primary key>", w, indent);
		}
		else {
			for (Column c : pk.getColumnMap().values()) {
				DataType t = c.getDataType();				
				print(c.getColumnName() + " [" + t.getTypeName() + "(" + t.getSize() + ")]", w, indent);						
			}
		}
	}

	private void print(Catalog c, PrintWriter w, int indent) {		
		for (Schema s : c.schemas().values()) {			
			logger().info(s.getUnqualifiedName());
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
	
	private void print(String msg, PrintWriter w, int level) {
		w.println(indent(level) + msg);
	}

	private String indent(int level) {
		char[] pad = new char[level * 2];
		Arrays.fill(pad, ' ');
		return String.copyValueOf(pad);
	}


	public static Logger logger() {
		return PrimaryKeyPrinter.logger;
	}
}
