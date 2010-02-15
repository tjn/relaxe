/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

public class SourceGenerator {
	
	private static Logger logger = Logger.getLogger(SourceGenerator.class);


	public static void main(String[] args) {
		
		try {
			if (args.length < 3) {
				System.err.println("usage:\n" +
						"java " + SourceGenerator.class.getName() + " <driver-symbol> <url> <config-file>");
				System.exit(-1);
			}			
			
			String driverName = args[0];
			String url = args[1];
			String cfg = args[2];
						
			PrintWriter out = new PrintWriter(System.out);
						
			logger().debug("loading " + driverName);
			Class<?> driverClass = Class.forName(driverName);
			logger().debug("driver loaded.");
			
			Driver selected = null;
			
			List<Driver> loaded = Collections.list(DriverManager.getDrivers());
			
			for(Driver d : loaded) {
				if (d.getClass().equals(driverClass)) {
					selected = d;
					break;
				}				
			}
			
			if (!selected.acceptsURL(url)) {
				throw new IllegalArgumentException(
						"driver " + selected.getClass() + " does not accept URL: " + url);
			}
			
			logger().debug("loading config: " + new File(cfg).getAbsolutePath());					
			Properties info = load(cfg);
			logger().debug("config loaded.");
											
			logger().debug("connecting to: " + url);
			
			Connection c = selected.connect(url, info);
			
			logger().debug("connected.");
			
			if (c == null) {
				throw new IllegalArgumentException("can not create connection to " + url);
			}
			
			try {
//				if (c != null) {
//					// test connection only
//					return; 
//				}
				
				try {				
					ResultSet rs = null;
					DatabaseMetaData meta = c.getMetaData();								
					QueryProcessor qp = new SimpleQueryProcessor(out, "\t");
					
//					meta.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)
//					QueryProcessor qp = new SimpleQueryProcessor(out, "\t");
					
					out.println("\nTABLES\n");
					rs = meta.getTables(null, null, "%", null);
					process(rs, qp, true);
					
					out.println("\nIMPORTED KEYS\n");
					rs = meta.getImportedKeys("millnew_20091023", null, "mill");
					process(rs, qp, true);

//					out.println("TABLE TYPES");
//					rs = meta.getTableTypes();
//					process(rs, qp, true);
					
										
					
//					TableNameProcessor np = new TableNameProcessor(out);
					
//					out.println("BASE TABLE");
//					String[] types = { "TABLE" };
//					rs = meta.getTables(null, null, "%", types);
//					process(rs, np, true);
//					
//					List<String> names = np.getNameList();
//					
//					for (String n : names) {
//						process(n, meta, out);
//					}

//					meta.getTables(null, null, null, null);					
//					meta.getTables(null, null, null, null);
					
//					logger().debug("executing transaction...");
//					executeAll(statements, c, qp);
//					logger().debug("committing...");
//					c.commit();
//					logger().debug("committed.");
				}
				catch (SQLException e) {
					logger().error(e.getMessage(), e);
					out.println("SQL-state: " + e.getSQLState());
					out.println("error-code: " + e.getErrorCode());
					out.println("message: " + e.getMessage());
					out.println(e.getMessage());
					c.rollback();
				}
				catch (Throwable e) {
					logger().error(e.getMessage(), e);
					out.println(e.getMessage());
					out.println("rolling back...");
					c.rollback();
				}
				
				out.flush();
			}			
			finally { 
				logger().debug("closing connection...");
				c.close();				
				logger().debug("connection closed");				
			}		
		}
		catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
	}
	
	private static void process(String n, DatabaseMetaData meta, PrintWriter out) 
		throws SQLException {
		out.println("TABLE: " + n);		
		
		String[] tokens = n.split("_");
		StringBuffer nb = new StringBuffer();
		
		for (String t : tokens) {
			t = t.toLowerCase();
						
			if (knownAbbr(t) || (t.length() <= 2 && (!knownProposition(t)))) {
				nb.append(t.toUpperCase());
			}
			else {	
				t = t.toLowerCase();
				nb.append(t.substring(0, 1).toUpperCase());
				
				if (t.length() > 1) {
					nb.append(t.substring(1));
				}				
			}
		}
		
		out.println(nb.toString());
		
//		QueryProcessor sp = new SimpleQueryProcessor(out, "\t");
//		
//		{
//			ResultSet pk = meta.getPrimaryKeys(null, null, n);
//			out.print("PK");
//			process(pk, sp, true);			
//		}
//
//		{
//			ResultSet ik = meta.getImportedKeys(null, null, n);			
//			out.print("IMPORTED");
//			process(ik, sp, true);			
//		}
//
//		{
//			ResultSet fk = meta.getExportedKeys(null, null, n);			
//			out.print("EXPORTED");
//			process(fk, sp, true);			
//		}
	}

	private static boolean knownAbbr(String t) {
		return t.equals("url") || t.equals("http") || t.equals("xml");
	}

	private static boolean knownProposition(String t) {
		return 
			t.equals("of") || t.equals("in") || t.equals("with") || 
			t.equals("at") || t.equals("for");
	}

	private static void executeAll(List<String> statements, Connection c, QueryProcessor qp) 
		throws Throwable {
		
		if (qp == null) {
			throw new NullPointerException();
		}

		for (String s : statements) {
			logger().debug("statement: (" + s + ")");
			
			PreparedStatement ps = c.prepareStatement(s);
						
			processAdHoc(ps, qp);
			
//			ResultSetMetaData m = ps.getMetaData();			
//			int cc = (m == null) ? 0 : m.getColumnCount();
//			
//			
//			try {			
//				qp.prepare(s, cc);
//								
//				if (cc == 0) {
//					int count = ps.executeUpdate();
//					qp.updated(count);
//				}
//				else {
//					ResultSet rs = ps.executeQuery();
//					long ordinal = 1;
//					
//					qp.startQuery(m);
//					
//					while (rs.next()) {
//						qp.process(rs, ordinal++);
//					}
//					
//					qp.endQuery();
//				}
//			}
//			catch (Throwable e) {				
//				qp.abort(e);
//				throw e;
//			}
//			finally {			
//				qp.finish();
//			}
		}
	}
	
	
	//
	
	private static void processAdHoc(PreparedStatement ps, QueryProcessor qp) 
		throws SQLException {

//		ResultSetMetaData m = ps.getMetaData();			
//		int cc = (m == null) ? 0 : m.getColumnCount();

		qp.prepare();
		
		try {
			boolean moreResults = ps.execute();
			boolean countProcessed = false;
																	
			do {
				countProcessed = false;
				
				if (moreResults) {
					ResultSet rs = null;
					try {
						rs = ps.getResultSet();					
						process(rs, qp);
					}
					finally {
						close(rs);
					}
				}
				else {
					int count = ps.getUpdateCount();
					
					if (count != -1) {
						qp.updated(count);
						countProcessed = true;
					}
				}
				
				moreResults = ps.getMoreResults();				
			} while (moreResults || countProcessed);			
		}
		catch (Throwable e) {
			logger().error(e.getMessage(), e);
			qp.abort(e);
		}
		finally {
			qp.finish();
		}
	}
	
	
	private static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		}
		catch (SQLException e) {
			logger().warn(e.getMessage(), e);
		}
	}
	
	private static void process(ResultSet rs, QueryProcessor qp, boolean close) 
		throws SQLException {
		try {
			process(rs, qp);
		}
		finally {
			if (close) {
				close(rs);
			}
		}		
	}
	

	private static void process(ResultSet rs, QueryProcessor qp)
		throws SQLException {

		long ordinal = 1;
		
		qp.startQuery(rs.getMetaData());
		
		while (rs.next()) {
			qp.process(rs, ordinal++);
		}
		
		qp.endQuery();
	}


	private static Properties load(String path) 
		throws IOException {
		FileInputStream in = null;
		
		try {
			in = new FileInputStream(path);			
			Properties p = new Properties();
			p.load(in);
			return p;
		}
		finally {
			if (in != null) {				
				in.close();
			}
		}
	}

	private static void copy(String path, Writer w) 
		throws IOException {
		Reader r = null;
		
		try {
			r = new BufferedReader(r = new FileReader(path));
			char[] buf = new char[8192];
			int read = 0;
			
			while((read = r.read(buf)) != -1) {
				w.write(buf, 0, read);
			}			
		}
		finally {
			if (r != null) {
				r.close();
			}
		}
	}

	
	private static List<String> statements(String sql) {
//		String sep = System.getProperty("line.separator");		
//		String qp = Pattern.quote(sep);		
//		logger().debug("quoted pattern: " + qp);
		
//		String p = "(\\n+|\\c+|\\r+)";
		String p = "(\\n+|\\r+)";
		String[] lines = sql.split(p);
		List<String> statements = new LinkedList<String>();
		
		StringBuffer qb = null;
		
//		logger().debug("lines: " + lines.length); 
		for (String line : lines) {			
			String n = line.trim();
			
//			logger().debug ("line {" + n + "}");
			
			if (n.equals("") || n.startsWith("--")) {
//				ignore
				continue;
			}
			

			
			if (n.equals(";") || n.equals(";;")) {
				if (qb != null) {
					String q = qb.toString();
					
					if (!q.trim().equals("")) {
						statements.add(q);
					}
					
					qb = null;
				}
				if (n.equals(";;")) {
					break;
				}
				else {
					continue;
				}
			}		
						
			qb = (qb == null) ? new StringBuffer() : qb;			
			qb.append(n);
			qb.append(" ");			
		}
		
		if (qb != null) {
			String q = qb.toString();
			
			if (!q.trim().equals("")) {
				statements.add(q);
			}
		}
				
		return statements;
	}
	
	public static Logger logger() {
		return SourceGenerator.logger;
	}
	
	private static class TableNameProcessor
		extends SimpleQueryProcessor {

		public TableNameProcessor(PrintWriter out) {
			super(out, "\t");
		}

		private List<String> names = new ArrayList<String>(); 
		
		@Override
		public void process(ResultSet rs, long ordinal) throws SQLException {
//			String schema = rs.getString("TABLE_SCHEM");
			String table = rs.getString("TABLE_NAME");
			names.add(table);			
		}

		protected List<String> getNameList() {
			return names;
		}			
	}

	
	private static class TableProcessor
		extends SimpleQueryProcessor {
		
		private DatabaseMetaData meta;
		
		public TableProcessor(PrintWriter rs, String cd, DatabaseMetaData meta) {
			super(rs, cd);
			this.meta = meta;
		}


		@Override
		public void process(ResultSet rs, long ordinal) throws SQLException {
			String schema = rs.getString("TABLE_SCHEM");
			String table = rs.getString("TABLE_NAME");
		}
	}
	
	

	
}
