/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// import fi.tnie.db.DefaultEntity.Query;
import fi.tnie.db.expr.Assignment;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.UpdateStatement;
import fi.tnie.db.expr.ValueParameter;
import fi.tnie.db.expr.ValueRow;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.impl.ColumnMap;

public abstract class DefaultPersistentEntity<
    A extends Enum<A> & Identifiable,
    R extends Enum<R> & Identifiable,
    Q extends Enum<Q> & Identifiable,
    P extends PersistentEntity<A, R, Q, ? extends P>
> 
    extends DefaultEntity<A, R, Q, P>
    implements PersistentEntity<A, R, Q, P>
{

    public void insert(Connection c) throws EntityException {
    		InsertStatement q = createInsertStatement();
    		String qs = q.generate();
    		
    		PreparedStatement ps = null;
    		ResultSet rs = null;		 
    		
    		try {		
    			logger().debug("JDBC-driver :" + c.getMetaData().getDriverName());
    			logger().debug("JDBC-driver-maj:" + c.getMetaData().getDriverMajorVersion());
    			logger().debug("JDBC-driver-min:" + c.getMetaData().getDriverMinorVersion());
    			logger().debug("JDBC-support-get-gen:" + c.getMetaData().supportsGetGeneratedKeys());
    			
    			ps = c.prepareStatement(qs, Statement.RETURN_GENERATED_KEYS);
    			q.traverse(null, new ParameterAssignment(ps));
    			
    			logger().debug("q: " + qs);
    			int ins = ps.executeUpdate();	
    			logger().debug("inserted: " + ins);
    			
    			rs = ps.getGeneratedKeys();
    			
    //			logger().debug(buf.toString());
    			
    			if (rs.next()) {
    //				buf.setLength(0);
    				
    					int cc = rs.getMetaData().getColumnCount();
    	//			StringBuffer buf = new StringBuffer();			
    				
    				EntityMetaData<A, R, ?, P> em = getMetaData();
    				BaseTable table = em.getBaseTable();
    				ColumnMap cm = table.columnMap();
    				List<A> keys = new ArrayList<A>();
    							
    				for (int i = 1; i <= cc; i++) {
    					String name = rs.getMetaData().getColumnLabel(i);
    					Column col = cm.get(name);
    					
    					if (col == null) {
    						throw new NullPointerException("Can not find column " + name + " in table: " + table.getQualifiedName());
    					} 
    					
    					A attr = em.getAttribute(col);				
    					keys.add(attr);				
    					
    	//				buf.append(name);
    	//				buf.append("\t");
    				}			
    								
    				for (int i = 1; i <= cc; i++) {					
    					A attr = keys.get(i - 1);
    					
    					if (attr != null) {
    						Object value = rs.getObject(i);
    						set(attr, value);
    					}
    															
    //					String col = rs.getString(i);
    //					buf.append(col);
    //					buf.append("\t");
    				}
    			}
    				
    //				logger().debug(buf.toString());				
    		}
    		catch (SQLException e) {
    			logger().error(e.getMessage(), e);
    			throw new EntityException(e.getMessage(), e);
    		}
    		finally {
    			rs = QueryHelper.doClose(rs);			
    			ps = QueryHelper.doClose(ps);
    		}
    	}

    public void merge(Connection c) throws EntityException {		
    	TableReference tref = createTableRef();		
    	Predicate pkp = getPKPredicate(tref);
    	
    	if (pkp == null) {
    		throw new EntityException("no primary key available");
    	}
    	
    	Query pq = new Query(getMetaData());		
    	P stored = pq.exec(c).first();
    	
    	if (stored == null) {
    		//
    		insert(c);
    	}
    	else {			
    	}		
    }

    @Override
    public void update(Connection c) throws EntityException {
    	
    	TableReference tref = createTableRef();		
    	Predicate pkp = getPKPredicate(tref);
    	
    	if (pkp == null) {
    		throw new EntityException("no primary key available");
    	}
    	
    	UpdateStatement q = createUpdateStatement();
    	
    	if (q != null) {
    		PreparedStatement ps = null;
    		
    		try {			
    			String qs = q.generate();
    			logger().debug("qs: " + qs);		
    			ps = c.prepareStatement(qs);				
    			q.traverse(null, new ParameterAssignment(ps));		
    			int ins = ps.executeUpdate();
    			logger().debug("updated: " + ins);
    		}
    		catch (SQLException e) {
    			logger().error(e.getMessage(), e);
    			throw new EntityException(e.getMessage(), e);
    		}
    		finally {
    			ps = QueryHelper.doClose(ps);
    		}
    	}
    }

    @Override
    public void delete(Connection c) throws EntityException {
    	DeleteStatement ds = createDeleteStatement();
    	
    	if (ds != null) {
    		try {
    			String qs = ds.generate();				
    			logger().debug("qs: " + qs);		
    			final PreparedStatement ps = c.prepareStatement(qs);				
    			ds.traverse(null, new ParameterAssignment(ps));		
    			int deleted = ps.executeUpdate();
    			logger().debug("deleted: " + deleted);
    		}
    		catch (SQLException e) {
    			logger().error(e.getMessage(), e);
    			throw new EntityException(e.getMessage(), e);
    		}
    	}
    }


    public InsertStatement createInsertStatement() {	
    	ValueRow newRow = new ValueRow();
    	 
    	final EntityMetaData<A, R, ?, P> meta = getMetaData();				
    	BaseTable t = meta.getBaseTable();
    			
    	ElementList<ColumnName> names = new ElementList<ColumnName>();
    	
    	for (Map.Entry<A, Object> e : attrs().entrySet()) {			
    		Column col = meta.getColumn(e.getKey());
    		ValueParameter p = new ValueParameter(col, e.getValue());
    		newRow.add(p);
    		names.add(col.getColumnName());
    	}		
    	
    	for (Map.Entry<R, Entity<?,?,?,?>> e : refs().entrySet()) {			
    		ForeignKey fk = meta.getForeignKey(e.getKey());			
    		Entity<?,?,?,?> ref = e.getValue();
    		
    		if (ref == null) {
    			for (Column	c : fk.columns().keySet()) {
    				ValueParameter p = new ValueParameter(c, null);
    				newRow.add(p);
    				names.add(c.getColumnName());
    			}
    		}
    		else {
    			for (Map.Entry<Column, Column> ce : fk.columns().entrySet()) {
    				Column fc = ce.getValue();
    				Object o = ref.get(fc);
    				ValueParameter p = new ValueParameter(ce.getKey(), o);										
    				newRow.add(p);
    				names.add(ce.getKey().getColumnName());
    			}
    		}
    	}	
    			
    	return new InsertStatement(t, names, newRow);
    }

    public UpdateStatement createUpdateStatement() throws EntityException {
    
    	final EntityMetaData<A, R, ?, P> meta = getMetaData();
    	TableReference tref = createTableRef();
    	
    	Predicate p = getPKPredicate(tref);
    	
    	if (p == null) {
    		return null;
    	}		
    	
    	ElementList<Assignment> assignments = new ElementList<Assignment>();
    	 		
    	for (Map.Entry<A, Object> e : attrs().entrySet()) {			
    		Column col = meta.getColumn(e.getKey());
    		ValueParameter vp = new ValueParameter(col, e.getValue());						
    		assignments.add(new Assignment(col.getColumnName(), vp));
    	}
    	
    	
    	for (Map.Entry<R, Entity<?,?,?,?>> e : refs().entrySet()) {			
    		ForeignKey fk = meta.getForeignKey(e.getKey());			
    		Entity<?,?,?,?> ref = e.getValue();
    		
    		if (ref == null) {
    			for (Column	c : fk.columns().keySet()) {
    				assignments.add(new Assignment(c.getColumnName(), null));
    			}
    		}
    		else {
    			for (Map.Entry<Column, Column> ce : fk.columns().entrySet()) {
    				Column fc = ce.getValue();
    				Object o = ref.get(fc);
    				Column column = ce.getKey();										
    				ValueParameter vp = new ValueParameter(column, o);
    				assignments.add(new Assignment(column.getColumnName(), vp));
    			}
    		}
    	}
    				
    	return new UpdateStatement(tref, assignments, p);
    }

    public DeleteStatement createDeleteStatement() throws EntityException {
    					
    	final EntityMetaData<A, R, ?, P> meta = getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	
    	Predicate p = getPKPredicate(tref);
    	
    	if (p == null) {
    		return null;
    	}		
    				
    	return new DeleteStatement(tref, p);
    }


    private class Query
        extends DefaultEntityQuery<A, R, Q, P>
    {
        public Query(EntityMetaData<A, R, Q, P> meta) {
            super(meta);
        }       
    }


}
