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
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DefaultEntityQuery;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityContext;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.expr.Assignment;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.UpdateStatement;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.ValueParameter;
import fi.tnie.db.expr.ValueRow;
import fi.tnie.db.expr.op.AndPredicate;
import fi.tnie.db.expr.op.Eq;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public class PersistenceManager<
    A extends Attribute,
    R,
    T extends ReferenceType<T>,
    E extends Entity<A, R, T, E>>
{	
    private class Query
        extends DefaultEntityQuery<A, R, T, E>
    {
        public Query(EntityMetaData<A, R, T, E> meta) {
            super(meta);
        }
    }

    private E target;
    private Query query = null;
    private Implementation implementation = null;

    private static Logger logger = Logger.getLogger(PersistenceManager.class);

    public DeleteStatement createDeleteStatement() throws EntityException {
        E pe = getTarget();
        final EntityMetaData<?, ?, ?, ?> meta = pe.getMetaData();

    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);

    	if (pkp == null) {
    		return null;
    	}

    	return getSyntax().newDeleteStatement(tref, pkp);
    }


    public InsertStatement createInsertStatement() {
        E pe = getTarget();
    	ValueRow newRow = new ValueRow();

    	final EntityMetaData<A, R, T, E> meta = pe.getMetaData();
    	BaseTable t = meta.getBaseTable();

    	ElementList<ColumnName> names = new ElementList<ColumnName>();

    	for (A a : meta.attributes()) {
    		logger().debug("createInsertStatement: a=" + a);
    		
    		Column col = meta.getColumn(a);    		
    		PrimitiveHolder<?, ?> holder = pe.value(a);
    		
    		if (holder == null) {
    			continue;
    		}

    		if (holder.isNull() && col.isPrimaryKeyColumn()) {
    		    // this might be auto-increment pk-column
    		    // newRow.add(new ValueExpression());
    		}
    		else {
                ValueParameter<?, ?> p = createParameter(col, holder);
                newRow.add(p);
                names.add(col.getColumnName());
    		}
    	}


    	for (R r : meta.relationships()) {
            ForeignKey fk = meta.getForeignKey(r);                        
            ReferenceHolder<?, ?, ?, ?> rh = pe.ref(r);
            
            if (rh == null) {
            	continue;
            }
            
            Entity<?, ?, ?, ?> ref = pe.ref(r).value();

            if (ref == null) {
                for (Column c : fk.columns().keySet()) {                	
                	PrimitiveHolder<?, ?> nh = PrimitiveType.nullHolder(c.getDataType().getDataType());                	                	
                	ValueParameter<?, ?> vp = createParameter(c, nh);
                    newRow.add(vp);
                    names.add(c.getColumnName());
                }
            }
            else {
                for (Map.Entry<Column, Column> ce : fk.columns().entrySet()) {
                    Column fc = ce.getValue();
//                    Object o = ref.get(fc);
                    PrimitiveHolder<?, ?> o = ref.get(fc);

//                    ValueParameter p = new ValueParameter(ce.getKey(), o);
                    ValueParameter<?, ?> p = createParameter(ce.getKey(), o);
                    newRow.add(p);
                    names.add(ce.getKey().getColumnName());
                }
            }
        }

    	return new InsertStatement(t, names, newRow);
    }


	private
	<P extends PrimitiveType<P>, H extends PrimitiveHolder<?, P>>
	ValueParameter<P, H> createParameter(Column col, H holder) {
		return new ValueParameter<P, H>(col, holder);
	}

    public UpdateStatement createUpdateStatement() throws EntityException {
        E pe = getTarget();

    	final EntityMetaData<A, R, T, ?> meta = pe.getMetaData();
    	// TableReference tref = meta.createTableRef();
    	TableReference tref = new TableReference(meta.getBaseTable());

    	Predicate p = getPKPredicate(tref, pe);

    	if (p == null) {
    		return null;
    	}

    	ElementList<Assignment> assignments = new ElementList<Assignment>();

    	for (A a : meta.attributes()) {
    		Column col = meta.getColumn(a);
    		PrimitiveHolder<?, ?> ph = pe.value(a);
    		
    		if (ph != null) {    		    		    		
	    		ValueParameter<?, ?> vp = createParameter(col, ph);    		
	    		assignments.add(new Assignment(col.getColumnName(), vp));
    		}
    	}

        for (R r : meta.relationships()) {
        	// TODO: Should order matter here? 
        	// Foreign keys consisting of the columns which are 
        	// subset of some other foreign key should be treated specially, no?        	
        	
            ForeignKey fk = meta.getForeignKey(r);
            Entity<?,?,?,?> ref = pe.getRef(r);

            if (ref == null) {
                for (Column c : fk.columns().keySet()) {
                    assignments.add(new Assignment(c.getColumnName(), null));
                }
            }
            else {
                for (Map.Entry<Column, Column> ce : fk.columns().entrySet()) {
                    Column fc = ce.getValue();
                    PrimitiveHolder<?, ?> ph = ref.get(fc);
                    
                    Column column = ce.getKey();
                    
            		if (ph != null) {    		    		    		
        	    		ValueParameter<?, ?> vp = createParameter(column, ph);    		
        	    		assignments.add(new Assignment(column.getColumnName(), vp));
            		}                    
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
    			ds.traverse(null, createAssignmentVisitor(ps));
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
			q.traverse(null, createAssignmentVisitor(ps));

			logger().debug("q: " + qs);
			int ins = ps.executeUpdate();
			logger().debug("inserted: " + ins);

			rs = ps.getGeneratedKeys();

//			logger().debug(buf.toString());

			if (rs.next()) {
				GeneratedKeyHandler kh = getKeyHandler();
				kh.processGeneratedKeys(q, pe, rs);
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

        DefaultEntityQueryTask<A, R, T, E> qt =
        	new DefaultEntityQueryTask<A, R, T, E>(pq, getImplementation());

    	Predicate pkp = getPKPredicate(tref, getTarget());
    	E stored = null;

    	if (pkp != null) {
            pq.getQuery().getWhere().setSearchCondition(pkp);
            stored = qt.exec(c).first();
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
    			q.traverse(null, createAssignmentVisitor(ps));
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


	private AssignmentVisitor createAssignmentVisitor(PreparedStatement ps) {
		return new AssignmentVisitor(getImplementation().getValueAssignerFactory(), ps);
	}


    private Predicate getPKPredicate() throws EntityException {
        E pe = getTarget();
    	EntityMetaData<A, R, T, ?> meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);
        return pkp;
    }

    public static Logger logger() {
        return PersistenceManager.logger;
    }

    public PersistenceManager(E target, Implementation implementation) {
        super();

        if (target == null) {
            throw new NullPointerException("'target' must not be null");
        }
        
        if (implementation == null) {
			throw new NullPointerException("implementation");
		}
        
        setImplementation(implementation);
        setTarget(target);        
    }

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

        EntityMetaData<A, R, T, ?> meta = pe.getMetaData();
        Set<Column> pkcols = meta.getPKDefinition();

        if (pkcols.isEmpty()) {
            throw new EntityException("no pk-columns available for entity type " + pe.getType());
        }

        Predicate p = null;

        for (Column col : pkcols) {
            PrimitiveHolder<?, ?> o = pe.get(col);

            // to successfully create a pk predicate
            // every component must be set:
            if (o == null) {
                return null;
            }

            ColumnReference cr = new ColumnReference(tref, col);
            ValueParameter<?, ?> param = createParameter(col, o);
            p = AndPredicate.newAnd(p, eq(cr, param));
        }

        return p;
    }


    public Object get(Entity<A, R, T, ?> pe, Column column)
        throws NullPointerException {
        if (column == null) {
            throw new NullPointerException("'c' must not be null");
        }

        EntityMetaData<A, R, T, ?> m = pe.getMetaData();

        A a = m.getAttribute(column);

        if (a != null) {
            return pe.value(a);
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
            ref = pe.ref(r).value();

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

	public GeneratedKeyHandler getKeyHandler() {
		return getImplementation().generatedKeyHandler();
	}

	public SQLSyntax getSyntax() {
		return getImplementation().getSyntax();
	}


	public Implementation getImplementation() {
		return implementation;
	}


	public void setImplementation(Implementation implementation) {
		this.implementation = implementation;
	}
	
	
	public QueryResult<E> execute(EntityQuery<A, R, T, E> query, EntityContext ctx, Connection c) 
		throws SQLException {
		StatementExecutor st = new StatementExecutor();
		
		DefaultTableExpression qo = query.getQuery();
		QueryExpression e = qo;
		ValueExtractorFactory vef = getImplementation().getValueExtractorFactory();
		ArrayList<DataObject> content = new ArrayList<DataObject>();		
		
		// DataObjectReader r = new DataObjectReader(vef, qo, content);
//		EntityCompositor cmp = new EntityCompositor(ctx, vef, e);
		
				
//		st.execute(qo, c, cmp);		
				
		
		
		
		
		
		return null;
		
	}
}
