/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.Assignment;
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.TableColumnExpr;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.UpdateStatement;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.ValueParameter;
import fi.tnie.db.expr.ValueRow;
import fi.tnie.db.expr.op.AndPredicate;
import fi.tnie.db.expr.op.Eq;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.impl.ColumnMap;

public class PersistenceManager<
    A extends Enum<A> & Identifiable, 
    R extends Enum<R> & Identifiable,
    Q extends Enum<Q> & Identifiable,
    E extends Entity<A, R, Q, ? extends E>>
{
    
    private class Query
        extends DefaultEntityQuery<A, R, Q, E>
    {
        public Query(EntityMetaData<A, R, Q, ? extends E> meta) {
            super(meta);
        }       
    }
    
    private E target;
    private Query query = null;
        
    private static Logger logger = Logger.getLogger(PersistenceManager.class);

    public DeleteStatement createDeleteStatement() throws EntityException {
        E pe = getTarget();
        final EntityMetaData<?, ?, ?, ?> meta = pe.getMetaData();
        
        Environment env = meta.getCatalog().getEnvironment();
        SQLSyntax stx = env.getSyntax();
                
    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);    	   	
    	
    	if (pkp == null) {
    		return null;
    	}		
    				
    	return stx.newDeleteStatement(tref, pkp);
    }

    
    public InsertStatement createInsertStatement() {	
        E pe = getTarget();        
    	ValueRow newRow = new ValueRow();
    	 
    	final EntityMetaData<A, R, Q, ? extends E> meta = pe.getMetaData();				
    	BaseTable t = meta.getBaseTable();
    			
    	ElementList<ColumnName> names = new ElementList<ColumnName>();
    	   	    	
    	for (A a : meta.attributes()) {    	    
    		Column col = meta.getColumn(a);
    		
    		Object value = pe.get(a);
    		
    		if (value == null && col.isPrimaryKeyColumn()) {
    		    // this might be auto-increment pk-column
    		    // newRow.add(new ValueExpression());
    		}    		
    		else {
                ValueParameter p = new ValueParameter(col, value);
                newRow.add(p);
                names.add(col.getColumnName());    		    
    		}
    	}		
    	
    	for (R r : meta.relationships()) {
            ForeignKey fk = meta.getForeignKey(r);
            Entity<?, ?, ?, ?> ref = pe.get(r);
                        
            if (ref == null) {
                for (Column c : fk.columns().keySet()) {
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
        E pe = getTarget();
    
    	final EntityMetaData<A, R, Q, ? extends E> meta = pe.getMetaData();
    	// TableReference tref = meta.createTableRef();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	    	
    	Predicate p = getPKPredicate(tref, pe);
    	
    	if (p == null) {
    		return null;
    	}		
    	
    	ElementList<Assignment> assignments = new ElementList<Assignment>();
    	
    	    	 		
    	for (A a : meta.attributes()) {			
    		Column col = meta.getColumn(a);
    		Object value = pe.get(a);
    		ValueParameter vp = new ValueParameter(col, value);						
    		assignments.add(new Assignment(col.getColumnName(), vp));
    	}
    	    	
    	
        for (R r : meta.relationships()) {          
            ForeignKey fk = meta.getForeignKey(r);         
            Entity<?,?,?,?> ref = pe.get(r);
            
            if (ref == null) {
                for (Column c : fk.columns().keySet()) {
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

    public void insert(Connection c) throws EntityException {
        E pe = getTarget();
    
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
				
				logger().debug("getGeneratedKeys: ");
				
				ResultSetMetaData meta = rs.getMetaData();
				
				for (int i = 1; i <= cc; i++) {
				    String label = meta.getColumnLabel(i);
				    logger().debug("[" + i + "]: " + label);
                }
				
				
				
	//			StringBuffer buf = new StringBuffer();			
				
				EntityMetaData<A, R, Q, ? extends E> em = pe.getMetaData();
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
						pe.set(attr, value);
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
        Query pq = getQuery();
        TableReference tref = pq.getTableRef();
               
        
    	Predicate pkp = getPKPredicate(tref, getTarget());
    	E stored = null; 
    	    
    	if (pkp != null) {
            pq.getQuery().getWhere().setSearchCondition(pkp);
            stored = pq.exec(c).first();    		
    	}    	
    	
    	if (stored == null) {    		
    		insert(c);
    	}
    	else {
    	    update(c);
    	}		
    }

    
    public void update(Connection c) throws EntityException {
    	Predicate pkp = getPKPredicate();
    	
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


    private Predicate getPKPredicate() throws EntityException {
        E pe = getTarget();
    	EntityMetaData<A, R, Q, ? extends E> meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);
        return pkp;
    }
    
    public static Logger logger() {
        return PersistenceManager.logger;
    }
        
    public PersistenceManager(E target) {
        super();
        
        if (target == null) {
            throw new NullPointerException("'target' must not be null");
        }
        
        setTarget(target);    
    }

//    public static <
//      A extends Enum<A> & Identifiable, 
//      R extends Enum<R> & Identifiable, 
//      Q extends Enum<Q> & Identifiable, 
//      E extends Entity<A, R, Q, ? extends E>
//    > 
//    PersistenceManager<A, R, Q, ? extends E> newInstance(E pe) {
//        return new PersistenceManager<A, R, Q, E>(pe);
//    }


    public E getTarget() {
        return target;
    }


    public void setTarget(E target) {
        this.target = target;
        this.query = null;
    }


    public Predicate getPKPredicate(TableReference tref, E pe) 
        throws EntityException {
    
        if (tref == null) {
            throw new NullPointerException();
        }
        
        EntityMetaData<A, R, Q, ? extends E> meta = pe.getMetaData();
        Set<Column> pkcols = meta.getPKDefinition();
            
        if (pkcols.isEmpty()) {
            throw new EntityException("no pk-attributes available");            
        }                   
        
        Predicate p = null;
        
        for (Column col : pkcols) {
            Object o = pe.get(col);
            
            // to successfully create a pk predicate 
            // every component must be set:             
            if (o == null) {                
                return null;
            }
            
            ColumnExpr ce = new TableColumnExpr(tref, col);
            ValueParameter param = new ValueParameter(col, o);
            p = AndPredicate.newAnd(p, eq(ce, param));
        }
        
        return p;
    }
    
    
    public Object get(Entity<A, R, Q, E> pe, Column column)
        throws NullPointerException {
        if (column == null) {
            throw new NullPointerException("'c' must not be null");
        }
        
        EntityMetaData<A, R, Q, E> m = pe.getMetaData();
        
        A a = m.getAttribute(column);
        
        if (a != null) {
            return pe.get(a);
        }
        
        // column may be part of multiple
        // overlapping foreign-keys:                
        Set<R> rs = m.getReferences(column);
        
        if (rs == null) {
            return null;
        }
    
        Entity<?, ?, ?, ?> ref = null;
        R r = null;
        
        for (R ri : rs) {
            ref = pe.get(r);
            
            if (ref != null) {              
                r = ri;
                break;
            }
        }
                
        if (ref == null) {          
            return null;
        }
        
        ForeignKey fk = m.getForeignKey(r);
        Column fkcol = fk.columns().get(column);        
        return ref.get(fkcol);
    };
    
    private Eq eq(ValueExpression a, ValueExpression b) {
        return new Eq(a, b);
    }


    private Query getQuery() {
        if (this.query == null) {
            this.query = new Query(getTarget().getMetaData());            
        }

        return this.query;
    }

}
