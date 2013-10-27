/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

import com.appspot.relaxe.exec.QueryProcessor;

import fi.tnie.util.cli.CommandLine;
import fi.tnie.util.cli.Option;
import fi.tnie.util.cli.Parameter;
import fi.tnie.util.cli.Parser;
import fi.tnie.util.io.IOHelper;


public class QueryTool {
	
	private static Logger logger = Logger.getLogger(QueryTool.class);
	

	public static void main(String[] args) {
		
		try {
		    Parser p = new Parser();
		    Option drvOpt = p.addOption("jdbc-driver", "d");
		    Option urlOpt = p.addOption("jdbc-url", "u");
		    Option cfgOpt = p.addOption("jdbc-config", "c", true);
		    Option help = p.addOption("help", "h", true);
		    Parameter inputFile = p.addParameter("query-input", false);
		    
		    CommandLine cl = p.parse(args);
		    
		    if (cl.needsHelp(help)) {
		        System.err.println(p.usage(QueryTool.class));
                System.exit(-1);
		    }

//			if (args.length < 3) {
//				System.err.println("usage:\n" +
//						"java " + QueryTool.class.getName() + " <driver-name> <url> <config-file> <query-file> [auto-commit=<true|false>]");
//				System.exit(-1);
//			}
		    
            String driverName = cl.value(drvOpt);
            String url = cl.value(urlOpt);
            String cfg = cl.value(cfgOpt);
            List<String> input = cl.values(inputFile);
            
            logger().debug("input value: " + input);
		    			
//			String driverName = arg(args, 0);
//			String url = arg(args, 1);
//			String cfg = arg(args, 2);
//			String input = arg(args, 3);			
//			String autoCommitOpt = arg(args, 4);
//			
//			boolean autoCommit = false;
//			
//			if(autoCommitOpt != null) {
//				
//			}										
			
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
			
			logger().debug("loading config...");					
			Properties info = IOHelper.doLoad(cfg);
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
				
				logger().debug("reading queries...");
				
				StringWriter sw = new StringWriter();
				
				for (String qf : input) {
				    copy(qf, sw);
				    sw.write("\n");
                }
				
				QueryTool qt = new QueryTool();				
				List<String> statements = qt.statements(sw.toString());
				
				if (statements.isEmpty()) {
					throw new IllegalArgumentException("no statements in input file(s)");
				}
				
				logger().debug("queries read: " + statements.size());
								
				boolean autoCommit = (statements.size() == 1);								
				c.setAutoCommit(autoCommit);
				
				logger().debug("auto-commit disabled ? " + (!c.getAutoCommit()));
				
				QueryProcessor qp = new SimpleQueryProcessor(out);
				
				try {
					logger().debug("executing transaction...");
					qt.executeAll(statements, c, qp);
					logger().debug("committing...");
					
					if (!c.getAutoCommit()) {						
						c.commit();
						logger().debug("committed.");
					}					
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
	
	public void executeAll(List<String> statements, Connection c, QueryProcessor qp) 
		throws Throwable {
		
		if (qp == null) {
			throw new NullPointerException();
		}

		for (String s : statements) {
			logger().debug("statement: (" + s + ")");
			
			PreparedStatement ps = c.prepareStatement(s);						
			processAdHoc(ps, qp);			
		}
	}
		
	public static void processAdHoc(PreparedStatement ps, QueryProcessor qp) 
		throws Exception {

	    qp.prepare();
				
		try {
			boolean moreResults = ps.execute();
			boolean countProcessed = false;
																	
			do {
				countProcessed = false;
				
				if (moreResults) {
					ResultSet rs = ps.getResultSet();
					
					long ordinal = 1;
					
					qp.startQuery(rs.getMetaData());
					
					while (rs.next()) {
						qp.process(rs, ordinal++);
					}
					
					qp.endQuery();					
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
		catch (Exception e) {
			logger().error(e.getMessage(), e);
			qp.abort(e);
		}
		finally {
			qp.finish();
		}
	}

	public static long process(ResultSet rs, QueryProcessor qp) 
	   throws Exception {

       qp.prepare();
            
       long ordinal = 0;
       
       try {        
           qp.startQuery(rs.getMetaData());
                   
           while (rs.next()) {
               ordinal++;
               qp.process(rs, ordinal);
           }
                   
           qp.endQuery();                  
       }
       catch (Exception e) {
           logger().error(e.getMessage(), e);
           qp.abort(e);
       }
       finally {
           qp.finish();
       }
       
       return ordinal;
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

	
	public List<String> statements(String sql) {
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
		return QueryTool.logger;
	}
}
