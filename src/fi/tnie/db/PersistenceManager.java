/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.CyclicTemplateException;
import fi.tnie.db.ent.DefaultEntityTemplateQuery;
import fi.tnie.db.ent.DefaultQueryTemplate;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.Reference;
//import fi.tnie.db.ent.DefaultEntityQuery;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.expr.Assignment;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Default;
import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.Predicate;
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
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public class PersistenceManager<
    A extends Attribute,
    R extends Reference,
    T extends ReferenceType<A, R, T, E, H, F, M>,
    E extends Entity<A, R, T, E, H, F, M>,
    H extends ReferenceHolder<A, R, T, E, H, M>,
    F extends EntityFactory<E, H, M, F>,
    M extends EntityMetaData<A, R, T, E, H, F, M>
>
{	
//    private class PMQuery
//        extends DefaultEntityQuery<A, R, T, E, F, M>
//    {
//        /**
//		 * 
//		 */
//		private static final long serialVersionUID = -1285004174865437785L;
//
//		public PMQuery(M meta) throws EntityException {
//            super(meta);
//        }
//    }
    
    private class PMTemplate
	    extends DefaultQueryTemplate<A, R, T, E, H, F, M, PMTemplate>
    	implements EntityQueryTemplate<A, R, T, E, H, F, M, PMTemplate>
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = -1285004174865437785L;		
		private M meta;
		
		public PMTemplate(M meta) {
			super();
			this.meta = meta;
			addAllAttributes();
		}

		@Override
		public M getMetaData() {
			return this.meta;
		}

		@Override
		public PMTemplate self() {
			return this;
		}
		
		@Override
		public EntityQuery<A, R, T, E, H, F, M, PMTemplate> newQuery() 
			throws EntityRuntimeException
		{						
			return new DefaultEntityTemplateQuery<A, R, T, E, H, F, M, PMTemplate>(this);
		}	
	}

    private E target;    
    private Implementation implementation = null;

    private static Logger logger = Logger.getLogger(PersistenceManager.class);
    
//    private QT queryTemplate;
    
    /**
     * Specifies a behavior for merging the dependencies of the entity currently being merged.  
     * 
     * @see PersistenceManager#merge(Connection)
     * @author tnie
     */    
    public enum MergeMode {
    	/** 
    	 * Only the unidentified (non-null) entities the target refers to are merged.
    	 */    	
    	UNIDENTIFIED,
    	/**
    	 * All the non-null entities the target refers to are also merged.
    	 */
    	ALL
    }
    
    private MergeMode mergeMode;        
    
    public DeleteStatement createDeleteStatement() throws EntityException {
        E pe = getTarget();
        final EntityMetaData<?, ?, ?, ?, ?, ?, ?> meta = pe.getMetaData();

    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);

    	if (pkp == null) {
    		return null;
    	}

    	return getSyntax().newDeleteStatement(tref, pkp);
    }


    public InsertStatement createInsertStatement() throws EntityException {
        E pe = getTarget();
        
        logger().debug("createInsertStatement: pe=" + pe);        
    	ValueRow newRow = new ValueRow();

//    	M meta = pe.getMetaData();
    	final M meta = pe.getMetaData();
    	BaseTable t = meta.getBaseTable();
    	
    	Set<Column> pks = meta.getPKDefinition();
    	    	
    	ElementList<ColumnName> names = new ElementList<ColumnName>();

    	for (A a : meta.attributes()) {
    		logger().debug("createInsertStatement: a=" + a);
    		
    		Column col = meta.getColumn(a);
    		PrimitiveHolder<?, ?> holder = pe.value(a);
    		
    		if (holder == null) {
    			newRow.add(new Default(col));
    			names.add(col.getColumnName());
    			continue;
    		}

//   		TODO: 
//    		boolean nn = col.isDefinitelyNotNullable();
//    		logger().debug("createInsertStatement: not-nullable ? =" + nn);
//    		
//    		We should probably use condition (holder.isNull() && nn), but isDefinitelyNotNullable() 
//    		does not currently work so well for LiteralColumns:
    		    		
    		if (holder.isNull() && pks.contains(col)) {
    			newRow.add(new Default(col));
    			names.add(col.getColumnName());
    			continue;    			
    		}
    		
    		ValueParameter<?, ?> p = createParameter(col, holder);
            newRow.add(p);
    		names.add(col.getColumnName());
    	}

    	M m = target.getMetaData();

    	for (R r : meta.relationships()) {
            ForeignKey fk = meta.getForeignKey(r);
            
            EntityKey<R, T, E, M, ?, ?, ?, ?, ?, ?, ?, ?> ek = m.getEntityKey(r);
            
//            ReferenceHolder<?, ?, ?, ?, ?, ?> rh = pe.ref(r);
            ReferenceHolder<?, ?, ?, ?, ?, ?> rh = ek.get(target);
                        
            if (rh == null || rh.isNull()) {
            	continue;
            }
            
            Entity<?, ?, ?, ?, ?, ?, ?> ref = rh.value();

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
    	
    	logger().debug("createInsertStatement: has-names=" + (!names.isEmpty()));

    	return new InsertStatement(t, names, newRow);
    }


	private <
		P extends PrimitiveType<P>, 
		PH extends PrimitiveHolder<?, P>
	>
	ValueParameter<P, PH> createParameter(Column col, PH holder) {
		return new ValueParameter<P, PH>(col, holder);
	}

    public UpdateStatement createUpdateStatement() throws EntityException {
        E pe = getTarget();

    	final EntityMetaData<A, R, T, E, ?, ?, ?> meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	
    	Set<Column> pkcols = meta.getPKDefinition();

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
    	
    	Map<Column, Assignment> am = new LinkedHashMap<Column, Assignment>();
    	
    	for (R r : meta.relationships()) {
    		
    		if (!pe.has(r)) {
    			logger().debug("createUpdateStatement: no ref=" + r);
    		}
    		else {
		        ForeignKey fk = meta.getForeignKey(r);
		        Entity<?,?,?,?,?,?,?> ref = pe.getRef(r);
		
		        if (ref == null) {
		            for (Column c : fk.columns().keySet()) {
		            	if (!am.containsKey(c)) {
		            		am.put(c, null);
		            	}
		            }
		        }
		        else {
		            for (Map.Entry<Column, Column> ce : fk.columns().entrySet()) {
		                Column fc = ce.getValue();
		                PrimitiveHolder<?, ?> ph = ref.get(fc);
		                
		                Column column = ce.getKey();
		                
		        		if (ph != null) {    		    		    		
		    	    		ValueParameter<?, ?> vp = createParameter(column, ph);    		
		    	    		am.put(column, new Assignment(column.getColumnName(), vp));
		        		}                    
		            }
		        }
    		}
	    }    	
    	
    	for (Map.Entry<Column, Assignment> e : am.entrySet()) {
    		Assignment a = e.getValue();
    		    		
			if (a == null) {
				Column column = e.getKey();				
				a = new Assignment(column.getColumnName(), null);
			}
			
			assignments.add(a);    	
    	}
    	

    	return new UpdateStatement(tref, assignments, p);
    }

    public void delete(Connection c) throws EntityException {
    	DeleteStatement ds = createDeleteStatement();

    	if (ds != null) {
    		PreparedStatement ps = null;
    		
    		try {
    			String qs = ds.generate();
    			logger().debug("qs: " + qs);
    			ps = c.prepareStatement(qs);
    			logger().debug("ps sh: " + System.identityHashCode(ps));
    			ds.traverse(null, createAssignmentVisitor(ps));
    			int deleted = ps.executeUpdate();
    		    			
    			logger().debug("deleted: " + deleted);
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

    public void insert(Connection c) throws EntityException {
        E pe = getTarget();

		InsertStatement q = createInsertStatement();
		String qs = q.generate();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			logger().debug("insert: qs=" + qs);
			
			logger().debug("JDBC-driver :" + c.getMetaData().getDriverName());
			logger().debug("JDBC-driver-maj:" + c.getMetaData().getDriverMajorVersion());
			logger().debug("JDBC-driver-min:" + c.getMetaData().getDriverMinorVersion());
			logger().debug("JDBC-support-get-gen:" + c.getMetaData().supportsGetGeneratedKeys());

			ps = c.prepareStatement(qs, Statement.RETURN_GENERATED_KEYS);
//			logger().debug("ps: " + ps);
			logger().debug("ps sh: " + System.identityHashCode(ps));
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

    public void merge(Connection c) 
    	throws CyclicTemplateException, EntityException, SQLException, QueryException  {
    	
    	Implementation imp = getImplementation();
    	M meta = getTarget().getMetaData();    	
        PMTemplate qt = new PMTemplate(meta);
        
        EntityQuery<A, R, T, E, H, F, M, PMTemplate> eq = qt.newQuery();        
        TableReference tref = eq.getTableRef();        
    	Predicate pkp = getPKPredicate(tref, getTarget());
    	logger().debug("merge: pkp=" + pkp);
    	
    	E stored = null;

    	if (pkp != null) {    	
    		eq.getTableExpression().getWhere().setSearchCondition(pkp);
    		EntityQueryExecutor<A, R, T, E, H, F, M, PMTemplate> ee = new EntityQueryExecutor<A, R, T, E, H, F, M, PMTemplate>(imp);
//    		QueryResult<EntityDataObject<E>> qr = ee.execute(eq, false, c);
    		EntityQueryResult<A, R, T, E, H, F, M, PMTemplate> er = ee.execute(eq, null, c);
    		QueryResult<EntityDataObject<E>> qr = er.getContent();    		
    		List<? extends EntityDataObject<E>> cl = qr.getContent();
    		logger().debug("merge: cl.size()=" + cl.size());
    		
    		stored = cl.isEmpty() ? null : cl.get(0).getRoot(); 
    	}
    	
    	logger().debug("merge: stored=" + stored);
    	
    	    	
    	mergeDependencies(getTarget(), qt, c);    	

    	if (stored == null) {
    		insert(c);
    	}
    	else {
    	    update(c);
    	}
    }

    private 
    <
    	DA extends Attribute,
    	DR extends Reference,
    	DT extends ReferenceType<DA, DR, DT, DE, DH, DF, DM>,	
		DE extends Entity<DA, DR, DT, DE, DH, DF, DM>,
		DH extends ReferenceHolder<DA, DR, DT, DE, DH, DM>,
		DF extends EntityFactory<DE, DH, DM, DF>,
		DM extends EntityMetaData<DA, DR, DT, DE, DH, DF, DM>,
		DQ extends EntityQueryTemplate<DA, DR, DT, DE, DH, DF, DM, DQ>
    >    
    void mergeDependencies(DE target, DQ qt, Connection c) throws EntityException, SQLException, QueryException {
    	
    	logger().debug("mergeDependencies: target=" + target);
    	
    	DM m = target.getMetaData();    	
    	Set<DR> rs = m.relationships();
    	
    	final MergeMode ms = getMergeMode();
    	
    	for (DR dr : rs) {
			EntityKey<DR, DT, DE, DM, ?, ?, ?, ?, ?, ?, ?, ?> ek = m.getEntityKey(dr);
			
			String id = ek.name().identifier();
			logger().debug("mergeDependencies: id=" + id);			
			
			ReferenceHolder<?, ?, ?, ?, ?, ?> rh = ek.get(target);
			
			if (rh == null || rh.isNull()) {
				logger().debug("no ref");
				continue;
			}
			
			Entity<?, ?, ?, ?, ?, ?, ?> rv = rh.value();
								
			if (ms == MergeMode.ALL || (!rv.isIdentified())) {
				logger().debug("merging dependency: " + id);
				PersistenceManager<?, ?, ?, ?, ?, ?, ?> pm = create(rv.self(), getImplementation());
				pm.merge(c);
			}
			else {
				logger().debug("no merge for " + id);
			}
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
    			logger().debug("ps sh: " + System.identityHashCode(ps));
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
    	EntityMetaData<A, R, T, E, ?, ?, ?> meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);
        return pkp;
    }

    public static Logger logger() {
        return PersistenceManager.logger;
    }
    
    public PersistenceManager(E target, Implementation implementation) {
    	this(target, implementation, MergeMode.UNIDENTIFIED);    	
    }

    public PersistenceManager(E target, Implementation implementation, MergeMode mergeStrategy) {
        super();

        if (target == null) {
            throw new NullPointerException("'target' must not be null");
        }
        
        if (implementation == null) {
			throw new NullPointerException("implementation");
		}
        
        if (mergeStrategy == null) {
			throw new NullPointerException("mergeStrategy");
		}
        
        setImplementation(implementation);
        setTarget(target);        
        setMergeMode(mergeStrategy);
    }

    public E getTarget() {
        return target;
    }

    public void setTarget(E target) {
        this.target = target;
//        this.query = null;
    }

    public Predicate getPKPredicate(TableReference tref, E pe)
        throws EntityException {

        if (tref == null) {
            throw new NullPointerException();
        }

        EntityMetaData<A, R, T, E, ?, ?, ?> meta = pe.getMetaData();
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

    private Eq eq(ValueExpression a, ValueExpression b) {
        return new Eq(a, b);
    }

//
//    private PMQuery getQuery() throws EntityException {
//        if (this.query == null) {
//            this.query = new PMQuery(getTarget().getMetaData());
//        }
//
//        return this.query;
//    }

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
	
	private <
		DA extends Attribute, 
		DR extends fi.tnie.db.ent.Reference, 
		DT extends ReferenceType<DA, DR, DT, DE, DH, DF, DM>, 
		DE extends Entity<DA, DR, DT, DE, DH, DF, DM>,
		DH extends ReferenceHolder<DA, DR, DT, DE, DH, DM>,
		DF extends EntityFactory<DE, DH, DM, DF>,
		DM extends EntityMetaData<DA, DR, DT, DE, DH, DF, DM>		
	>
	PersistenceManager<DA, DR, DT, DE, DH, DF, DM> create(DE e, Implementation impl) {
		PersistenceManager<DA, DR, DT, DE, DH, DF, DM> pm = new PersistenceManager<DA, DR, DT, DE, DH, DF, DM>(e, impl, getMergeMode());
		return pm;
	}


	private MergeMode getMergeMode() {
		return mergeMode;
	}


	private void setMergeMode(MergeMode mm) {
		this.mergeMode = mm;
	}	
}
