package fi.tnie.db.expr;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.QueryContext;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;
import fi.tnie.db.meta.impl.DefaultCatalogFactory;
import fi.tnie.db.meta.impl.MySQLCatalogFactory;

public class QueryTest {

	private static Logger logger = Logger.getLogger(QueryTest.class);
	
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 3) {
				System.err.println("usage:\n" +
						"java " + QueryTest.class.getName() + " <driver-name> <url> <config-file>");
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
			
//			DefaultCatalogFactory cf = new DefaultCatalogFactory();
			DefaultCatalogFactory cf = new MySQLCatalogFactory("millnew_20091023");
			
			logger().debug("loading config: " + new File(cfg).getAbsolutePath());					
			Properties info = cf.load(cfg);
			logger().debug("config loaded.");
											
			logger().debug("connecting to: " + url);
			
			Connection c = null;
			
			try {				
				c = selected.connect(url, info);
				
				logger().debug("connected.");
				
				if (c == null) {
					throw new IllegalArgumentException("can not create connection to " + url);
				}
				
				
				DatabaseMetaData meta = c.getMetaData();
				
				logger().debug("inspect meta-data...");
				logger().debug("driver-version: " + meta.getDriverVersion());
				logger().debug("JDBC-major version: " + meta.getJDBCMajorVersion());
				logger().debug("JDBC-minor version: " + meta.getJDBCMinorVersion());
			
				
				logger().debug("populating meta-data...");
				Catalog catalog = cf.create(meta, null);
				
				String sch = "millnew_20091023";				
				Schema schema = catalog.schemas().get(sch);
				BaseTable m = (BaseTable) schema.tables().get("mill");				
				BaseTable mv = (BaseTable) schema.tables().get("mill_version");
				BaseTable cv = (BaseTable) schema.tables().get("company_version");
				
				QueryContext qc = new QueryContext();
				
				Query qo = null;
				
				qo = new Query(qc);				
				
				qo.setTableRefList(new BaseTableReference(qc, m).asList());
				logger().info("mill: " + qo.generate(qc));				

				qo.setTableRefList(new BaseTableReference(qc, mv).asList());
				logger().info("mill-version: " + qo.generate(qc));				

				
//				System.out.println(m.foreignKeys().toString());				
//				System.out.println(mv.foreignKeys().toString());
				
				ForeignKey fkm = mv.foreignKeys().get("FK_MV_MILL");
				ForeignKey fkc = mv.foreignKeys().get("FK_MV_COMPANY");
				
//				BaseTableReference mvref = new BaseTableReference(qc, mv);				
//				ForeignKeyJoinedTable jt = mvref.innerJoin("FK_MV_MILL");
				
				BaseTableReference mvref = new BaseTableReference(qc, mv);
				BaseTableReference mref = new BaseTableReference(qc, m);
				BaseTableReference cvref = new BaseTableReference(qc, cv);
				
				ForeignKeyJoinCondition fc = new ForeignKeyJoinCondition(
						fkm, mvref, mref);
																															
				JoinedTable jt = new JoinedTable(mvref, mref, JoinType.INNER, fc);
				
				NestedTableReference nt = new NestedTableReference(qc, jt);
				
				ForeignKeyJoinCondition fc2 = new ForeignKeyJoinCondition(
						fkc, mvref, cvref);				
				
				jt = new JoinedTable(nt, cvref, JoinType.INNER, fc2);
				
//				BaseTableReference mvref = (BaseTableReference) jt.getLeft();											
//				JoinedTable jt2 = new JoinedTable(qc, );
																												
				qo = new Query(qc);				
				qo.setTableRefList(jt.asList());
				logger().info("joined: " + qo.generate(qc));
//				
//				final BaseTableReference mr = new BaseTableReference(m);
//				final BaseTableReference mvr = new BaseTableReference(mv);
				
//				mvr.getSelectList();
				
//				ForeignKey fk = m.foreignKeys().get(null);
//				fk.getReferencing().
						
//				JoinCondition(fk);
								
//				mr.innerJoin(mvr, jc)
				
//				m.columns().get();
												
//				JoinCondition jc = new JoinCondition() {					
//					
//				};
//				
//				JoinedTable jt = mr.innerJoin(mvr, jc);
//				
//				qo.setTableRefList(jt.asList());
				
//				logger().debug("view catalog...");					
//				new PrimaryKeyPrinter(null).setCatalog(catalog);					
			}
			finally {
				if (c != null) {
					c.close();
				}					
			}			
		}
		catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
	}
	
	public static Logger logger() {
		return QueryTest.logger;
	}
		
}
