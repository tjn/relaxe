package fi.tnie.db.meta.impl;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;

public class CatalogPrinter implements CatalogUI {

	private PrintWriter writer;
	private static Logger logger = Logger.getLogger(CatalogPrinter.class);
	
	public CatalogPrinter(PrintWriter writer) {
		super();
		
		if (writer == null) {
			this.writer = new PrintWriter(System.out);
		}
	}
	

	@Override
	public void setCatalog(Catalog c) {
		print(c, this.writer, 0);
	}

	private void print(BaseTable b, PrintWriter w, int indent, Set<String> visited) {
		
		for (Map.Entry<String, ForeignKey> e : b.foreignKeys().entrySet()) {
			ForeignKey fk = e.getValue();
			String n = fk.getReferenced().getQualifiedName();
			
			if (!visited.contains(n)) {
				visited.add(n);
				
				logger().info(indent(indent) + e.getKey());			
				
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
		for (Map.Entry<String, Schema> e : c.schemas().entrySet()) {			
			logger().info(e.getKey());
			print((DefaultMutableSchema) e.getValue(), w, indent + 1);			
		}
	}

	private void print(DefaultMutableSchema s, PrintWriter w, int indent) {
		Set<String> visited = new HashSet<String>();
		
		for (Map.Entry<String, BaseTable> e : s.baseTables().entrySet()) {			
			logger().info(indent(indent) + e.getValue().getQualifiedName());
			visited.clear();
			print((BaseTable) e.getValue(), w, indent + 1, visited);
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
