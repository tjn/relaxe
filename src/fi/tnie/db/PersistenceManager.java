/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.CyclicTemplateException;
import fi.tnie.db.ent.DefaultEntityTemplateQuery;
import fi.tnie.db.ent.DefaultQueryTemplate;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.ent.Reference;
//import fi.tnie.db.ent.DefaultEntityQuery;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.exec.QueryProcessorAdapter;
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
import fi.tnie.db.expr.op.Comparison;
import fi.tnie.db.log.JavaLogger;
import fi.tnie.db.log.Logger;
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
    T extends ReferenceType<A, R, T, E, H, F, M, C>,
    E extends Entity<A, R, T, E, H, F, M, C>,
    H extends ReferenceHolder<A, R, T, E, H, M, C>,
    F extends EntityFactory<E, H, M, F, C>,
    M extends EntityMetaData<A, R, T, E, H, F, M, C>,
    C extends Content
>
{
	private static final QueryProcessor NO_OPERATION = new QueryProcessorAdapter();
	    
    private class PMTemplate
	    extends DefaultQueryTemplate<A, R, T, E, H, F, M, C, PMTemplate>
    	implements EntityQueryTemplate<A, R, T, E, H, F, M, C, PMTemplate>
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
		public EntityQuery<A, R, T, E, H, F, M, C, PMTemplate> newQuery() 
			throws EntityRuntimeException
		{						
			return new DefaultEntityTemplateQuery<A, R, T, E, H, F, M, C, PMTemplate>(this);
		}	
	}

    private E target;    
    private PersistenceContext<?> persistenceContext = null;
    private UnificationContext unificationContext;

    private static Logger logger = JavaLogger.getLogger(PersistenceManager.class);
    
    private MergeMode mergeMode;        
    
    public DeleteStatement createDeleteStatement() throws EntityException {
        E pe = getTarget();
        final EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> meta = pe.getMetaData();

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

    	final M meta = pe.getMetaData();
    	BaseTable t = meta.getBaseTable();
    	
    	Set<Column> pks = meta.getPKDefinition();
    	    	
    	ElementList<ColumnName> names = new ElementList<ColumnName>();
    	

    	for (A a : meta.attributes()) {
    		logger().debug("createInsertStatement: a=" + a);
    		
    		Column col = meta.getColumn(a);
    		PrimitiveHolder<?, ?, ?> holder = pe.value(a);
    		
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
    		
    		ValueParameter<?, ?> p = createParameter(col, holder.self());
            newRow.add(p);
    		names.add(col.getColumnName());
    	}

//    	M m = target.getMetaData();

    	for (R r : meta.relationships()) {
            ForeignKey fk = meta.getForeignKey(r);
            
            ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = pe.ref(r);
                                    
            if (rh == null) {
            	continue;
            }
            
            Entity<?, ?, ?, ?, ?, ?, ?, ?> ref = rh.value();

            if (ref == null) {
                for (Column c : fk.columns().keySet()) {                	
                	PrimitiveHolder<?, ?, ?> nh = PrimitiveType.nullHolder(c.getDataType().getDataType());                	                	
                	ValueParameter<?, ?> vp = createParameter(c, nh.self());
                    newRow.add(vp);
                    names.add(c.getColumnName());
                }
            }
            else {
                for (Map.Entry<Column, Column> ce : fk.columns().entrySet()) {
                    Column fc = ce.getValue();
                    PrimitiveHolder<?, ?, ?> o = ref.get(fc);
                    ValueParameter<?, ?> p = createParameter(ce.getKey(), o.self());
                    newRow.add(p);
                    names.add(ce.getKey().getColumnName());
                }
            }
        }
    	
    	logger().debug("createInsertStatement: has-names=" + (!names.isEmpty()));

    	return new InsertStatement(t, names, newRow);
    }


	private <
		V extends Serializable,
		P extends PrimitiveType<P>, 
		PH extends PrimitiveHolder<V, P, PH>
	>
	ValueParameter<P, PH> createParameter(Column col, PH holder) {
		return new ValueParameter<P, PH>(col, holder);
	}

    public UpdateStatement createUpdateStatement() throws EntityException {
        E pe = getTarget();

    	final M meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
   	

    	Predicate p = getPKPredicate(tref, pe);

    	if (p == null) {
    		return null;
    	}

    	ElementList<Assignment> assignments = new ElementList<Assignment>();

    	for (A a : meta.attributes()) {
    		Column col = meta.getColumn(a);
    		PrimitiveHolder<?, ?, ?> ph = pe.value(a);
    		
    		if (ph != null) {    		    		    		
	    		ValueParameter<?, ?> vp = createParameter(col, ph.self());    		
	    		assignments.add(new Assignment(col.getColumnName(), vp));
    		}
    	}
    	
    	Map<Column, Assignment> am = new LinkedHashMap<Column, Assignment>();
    	
    	for (R r : meta.relationships()) {
   			EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
   			processKey(ek, am);
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

    private 
    <	
    	RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
		RA extends Attribute,
		RR extends Reference,	
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
		RF extends EntityFactory<RE, RH, RM, RF, RC>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
		RC extends Content,
		RK extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, RK>
    >    
    void processKey(EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, RK> key, Map<Column, Assignment> am) {
    	
    	logger().debug("processKey - enter: " + key.name());
    	
    	final E e = getTarget();
    	final M m = e.getMetaData();
    	    	
    	RH rh = e.getRef(key.self());
    	
    	logger().debug("rh: " + rh);
    	
    	if (rh == null) {
    		return;
    	}
    	    	    	
		ForeignKey fk = m.getForeignKey(key.name());			

		if (rh.isNull()) {
			logger().debug("null ref: " + rh);
			
		      for (Column c : fk.columns().keySet()) {
		      	if (!am.containsKey(c)) {
		      		am.put(c, null);
		      	}
		      }
		  }
		  else {
			  RE re = rh.value();
			  
			  logger().debug("ref: " + re);
			  logger().debug("fk-cols: " + fk.columns().size());
			  
		      for (Map.Entry<Column, Column> ce : fk.columns().entrySet()) {
		          Column fc = ce.getValue();		          		          
		          PrimitiveHolder<?, ?, ?> ph = re.get(fc);
		          
		          logger().debug("rc: " + fc + " => " + ph);
		          
		          Column column = ce.getKey();
		          
		          if (ph != null) {    		    		    		
			  		ValueParameter<?, ?> vp = createParameter(column, ph.self());    		
				    am.put(column, new Assignment(column.getColumnName(), vp));
			  	}                    
		     }
		  }
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
    
    private E sync(EntityQuery<A, R, T, E, H, F, M, C, PMTemplate> query, Predicate pkp, Connection c) 
		throws EntityException, SQLException, QueryException  {
    	E stored = null;

    	if (pkp != null) {
    		query.getTableExpression().getWhere().setSearchCondition(pkp);
    		EntityQueryExecutor<A, R, T, E, H, F, M, C, PMTemplate> ee = new EntityQueryExecutor<A, R, T, E, H, F, M, C, PMTemplate>(getPersistenceContext(), getUnificationContext());
    		EntityQueryResult<A, R, T, E, H, F, M, C, PMTemplate> er = ee.execute(query, null, c);
    		QueryResult<EntityDataObject<E>> qr = er.getContent();    		
    		List<? extends EntityDataObject<E>> cl = qr.getContent();
    		logger().debug("merge: cl.size()=" + cl.size());
    		
    		stored = cl.isEmpty() ? null : cl.get(0).getRoot(); 
    	}
    	
    	return stored;
    }
    
    
    public E sync(Connection c)
    	throws EntityException, SQLException, QueryException  {

    	M meta = getTarget().getMetaData();    	
        PMTemplate qt = new PMTemplate(meta);
        
        EntityQuery<A, R, T, E, H, F, M, C, PMTemplate> eq = qt.newQuery();
        qt.addAllAttributes();
        TableReference tref = eq.getTableRef();        
    	Predicate pkp = getPKPredicate(tref, getTarget());

    	E stored = null;

    	if (pkp != null) {
    		stored = sync(eq, pkp, c);    		
    	}
    	
    	return stored;
    }

    	

    public void merge(Connection c) 
    	throws CyclicTemplateException, EntityException, SQLException, QueryException  {
    	
    	M meta = getTarget().getMetaData();    	
        PMTemplate qt = new PMTemplate(meta);        
        EntityQuery<A, R, T, E, H, F, M, C, PMTemplate> eq = qt.newQuery();        
        TableReference tref = eq.getTableRef();        
    	Predicate pkp = getPKPredicate(tref, getTarget());

    	E stored = null;

    	if (pkp != null) {
    		stored = sync(eq, pkp, c);    		
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
    	DT extends ReferenceType<DA, DR, DT, DE, DH, DF, DM, DC>,	
		DE extends Entity<DA, DR, DT, DE, DH, DF, DM, DC>,
		DH extends ReferenceHolder<DA, DR, DT, DE, DH, DM, DC>,
		DF extends EntityFactory<DE, DH, DM, DF, DC>,
		DM extends EntityMetaData<DA, DR, DT, DE, DH, DF, DM, DC>,
		DC extends Content, 
		DQ extends EntityQueryTemplate<DA, DR, DT, DE, DH, DF, DM, DC, DQ>
    >    
    void mergeDependencies(DE target, DQ qt, Connection c) throws EntityException, SQLException, QueryException {
    	
    	logger().debug("mergeDependencies: target=" + target);
    	
    	DM m = target.getMetaData();    	
    	Set<DR> rs = m.relationships();
    	
    	final MergeMode ms = getMergeMode();
    	
    	for (DR dr : rs) {
			EntityKey<DA, DR, DT, DE, DH, DF, DM, DC, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = m.getEntityKey(dr);
			
			String id = ek.name().identifier();
			logger().debug("mergeDependencies: id=" + id);			
			
			ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = ek.get(target);
			
			if (rh == null || rh.isNull()) {
				logger().debug("no ref");
				continue;
			}
			
			Entity<?, ?, ?, ?, ?, ?, ?, ?> rv = rh.value();
								
			if (ms == MergeMode.ALL || (!rv.isIdentified())) {
				logger().debug("merging dependency: " + id);
				PersistenceManager<?, ?, ?, ?, ?, ?, ?, ?> pm = create(rv.self(), getPersistenceContext());
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
    		try {
	    		StatementExecutor se = new StatementExecutor(getPersistenceContext());
	    		se.execute(q, c, NO_OPERATION);
    		}
    		catch (SQLException e) {
    			logger().error(e.getMessage(), e);
    			throw new EntityException(e.getMessage(), e);
    		}
    		catch (QueryException e) {
    			logger().error(e.getMessage(), e);
    			throw new EntityException(e.getMessage(), e);
    		}
    	}
    }


	private AssignmentVisitor createAssignmentVisitor(PreparedStatement ps) {
		return new AssignmentVisitor(getPersistenceContext().getValueAssignerFactory(), ps);
	}


    private Predicate getPKPredicate() throws EntityException {
        E pe = getTarget();
    	EntityMetaData<A, R, T, E, ?, ?, ?, ?> meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);
        return pkp;
    }

    public static Logger logger() {
        return PersistenceManager.logger;
    }
    
    public PersistenceManager(E target, PersistenceContext<?> persistenceContext, UnificationContext unificationContext) {
    	this(target, persistenceContext, MergeMode.UNIDENTIFIED, unificationContext);    	
    }

    public PersistenceManager(E target, PersistenceContext<?> persistenceContext, MergeMode mergeStrategy, UnificationContext unificationContext) {
        super();

        if (target == null) {
            throw new NullPointerException("'target' must not be null");
        }
        
        if (persistenceContext == null) {
			throw new NullPointerException("persistenceContext");
		}
        
        if (mergeStrategy == null) {
			throw new NullPointerException("mergeStrategy");
		}
        
        setPersistenceContext(persistenceContext);
        setTarget(target);        
        setMergeMode(mergeStrategy);
        
        this.unificationContext = unificationContext;
    }

    public E getTarget() {
        return target;
    }

    public void setTarget(E target) {
        this.target = target;
    }

    public Predicate getPKPredicate(TableReference tref, E pe)
        throws EntityException {

        if (tref == null) {
            throw new NullPointerException();
        }

        EntityMetaData<A, R, T, E, ?, ?, ?, ?> meta = pe.getMetaData();
        Set<Column> pkcols = meta.getPKDefinition();

        if (pkcols.isEmpty()) {
            throw new EntityException("no pk-columns available for entity type " + pe.type());
        }

        Predicate p = null;

        for (Column col : pkcols) {
            PrimitiveHolder<?, ?, ?> o = pe.get(col);

            // to successfully create a pk predicate
            // every component must be set:
            if (o == null) {
                return null;
            }

            ColumnReference cr = new ColumnReference(tref, col);
            ValueParameter<?, ?> param = createParameter(col, o.self());
            p = AndPredicate.newAnd(p, eq(cr, param));
        }

        return p;
    }

    private Predicate eq(ValueExpression a, ValueExpression b) {
    	return Comparison.eq(a, b);        
    }

	public GeneratedKeyHandler getKeyHandler() {
		return getPersistenceContext().generatedKeyHandler();
	}

	public SQLSyntax getSyntax() {
		return getPersistenceContext().getImplementation().getSyntax();
	}

	public PersistenceContext<?> getPersistenceContext() {
		return persistenceContext;
	}

	private void setPersistenceContext(PersistenceContext<?> persistenceContext) {
		this.persistenceContext = persistenceContext;
	}
	
	private <
		DA extends Attribute, 
		DR extends fi.tnie.db.ent.Reference, 
		DT extends ReferenceType<DA, DR, DT, DE, DH, DF, DM, DC>, 
		DE extends Entity<DA, DR, DT, DE, DH, DF, DM, DC>,
		DH extends ReferenceHolder<DA, DR, DT, DE, DH, DM, DC>,
		DF extends EntityFactory<DE, DH, DM, DF, DC>,
		DM extends EntityMetaData<DA, DR, DT, DE, DH, DF, DM, DC>,
		DC extends Content
	>
	PersistenceManager<DA, DR, DT, DE, DH, DF, DM, DC> create(DE e, PersistenceContext<?> pc) {
		PersistenceManager<DA, DR, DT, DE, DH, DF, DM, DC> pm = new PersistenceManager<DA, DR, DT, DE, DH, DF, DM, DC>(e, pc, getMergeMode(), getUnificationContext());
		return pm;
	}

	private MergeMode getMergeMode() {
		return mergeMode;
	}


	private void setMergeMode(MergeMode mm) {
		this.mergeMode = mm;
	}
		
	private UnificationContext getUnificationContext() {
		return unificationContext;
	}
}
