/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.DefaultEntityQuery;
import com.appspot.relaxe.ent.DefaultEntityQueryElement;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.expr.AbstractRowValueConstructor;
import com.appspot.relaxe.expr.Assignment;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.DeleteStatement;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.RowValueConstructor;
import com.appspot.relaxe.expr.RowValueConstructorElement;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.UpdateStatement;
import com.appspot.relaxe.expr.ValueExpression;
import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.expr.op.AndPredicate;
import com.appspot.relaxe.expr.op.Comparison;
import com.appspot.relaxe.log.Logger;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryResult;
import com.appspot.relaxe.rdbms.GeneratedKeyHandler;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.types.AbstractValueType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;
import com.appspot.relaxe.value.ValueHolder;

//import org.slf4j.Logger;

//import com.appspot.relaxe.ent.DefaultEntityQuery;

public class PersistenceManager<
    A extends AttributeName,
    R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M>,
	B extends MutableEntity<A, R, T, E, B, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>
>
{
    private class PMElement
    	extends DefaultEntityQueryElement<A, R, T, E, B, H, F, M, PMElement> {
    	
    	private M meta;
    	
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		protected PMElement() {	
		}
		
		public PMElement(M meta) {
			super();
			this.meta = meta;
		}
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public PMElement self() {
			return this;
		}

		@Override
		public M getMetaData() {
			return this.meta;
		}

		@Override
		public EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> getQueryElement(EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> k) {
			return null;
		}

		@Override
		public int getElementCount() {
			return 0;
		}

		@Override
		public Set<A> attributes() {
			return meta.attributes();
		}		
    }


    private E target;    
    private PersistenceContext<?> persistenceContext = null;
    private UnificationContext unificationContext;
    
    private static Logger logger = SLF4JLogger.getLogger(PersistenceManager.class);
    
    private MergeMode mergeMode;        
    
    public DeleteStatement createDeleteStatement() throws EntityException {
        E pe = getTarget();
        
        if (!pe.isIdentified()) {
        	return null;
        }        
        
        final EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
    	Predicate pkp = getPKPredicate(tref, pe);
    	return getSyntax().newDeleteStatement(tref, pkp);
    }


    public InsertStatement createInsertStatement() throws EntityException {
        E pe = getTarget();
        
//        logger().debug("createInsertStatement: pe=" + pe);        
    	List<RowValueConstructorElement> values = new ArrayList<RowValueConstructorElement>();

    	final M meta = pe.getMetaData();
    	BaseTable t = meta.getBaseTable();
    	
    	Set<Identifier> pks = meta.getBaseTable().getPrimaryKey().getColumnMap().keySet();
    	    	
    	List<Identifier> names = new ArrayList<Identifier>();

    	for (A a : meta.attributes()) {
//    		logger().debug("createInsertStatement: a=" + a);
    		
    		Column col = meta.getColumn(a);
    		ValueHolder<?, ?, ?> holder = pe.value(a);
    		
    		if (holder == null) {
    			values.add(RowValueConstructorElement.DEFAULT);
    			names.add(col.getColumnName());
    			continue;
    		}

//   		TODO: 
//    		boolean nn = col.isDefinitelyNotNullable();
//    		logger().debug("createInsertStatement: not-nullable ? =" + nn);
//    		
//    		We should probably use condition (holder.isNull() && nn), but isDefinitelyNotNullable() 
//    		does not currently work so well for LiteralColumns:
    		    		
    		if (holder.isNull() && pks.contains(col.getColumnName())) {
    			values.add(RowValueConstructorElement.DEFAULT);
    			names.add(col.getColumnName());
    			continue;    			
    		}
    		    		
    		RowValueConstructorElement elem = createRowValueConstructorElement(col, holder);    		
    		values.add(elem);
    		names.add(col.getColumnName());
    	}

    	for (R r : meta.relationships()) {
            ForeignKey fk = meta.getForeignKey(r);
            
            ReferenceHolder<?, ?, ?, ?, ?, ?> rh = pe.ref(r);
                                    
            if (rh == null) {
            	continue;
            }
            
            Entity<?, ?, ?, ?, ?, ?, ?, ?> ref = rh.value();

            if (ref == null) {
            	for (Column c : fk.getColumnMap().values()) {                	
                	ValueHolder<?, ?, ?> nh = AbstractValueType.nullHolder(c.getDataType().getDataType());
                	
                	RowValueConstructorElement p = newRowValueConstructorElement(c, nh.self());                	
                	values.add(p);
                    names.add(c.getColumnName());
                }
            }
            else {
            	for (Column c : fk.getColumnMap().values()) {
            		Column fc = fk.getReferenced(c);
                    ValueHolder<?, ?, ?> o = ref.get(fc);
                    RowValueConstructorElement p = newRowValueConstructorElement(c, o.self());
                    values.add(p);
                    names.add(c.getColumnName());            		
            	}
            }
        }
    	
    	RowValueConstructor rvc = AbstractRowValueConstructor.of(ElementList.newElementList(values));
    	
    	return new InsertStatement(t, ElementList.newElementList(names), rvc);    	
    }
	
	private	RowValueConstructorElement createRowValueConstructorElement(Column col, ValueHolder<?, ?, ?> holder) {
		return newRowValueConstructorElement(col, holder.self());
	}
	
	private <		
		PV extends Serializable,
		PT extends ValueType<PT>,
		PH extends ValueHolder<PV, PT, PH>
	>
	ImmutableValueParameter<PV, PT, PH> newRowValueConstructorElement(Column col, ValueHolder<PV, PT, PH> holder) {		
		return new ImmutableValueParameter<PV, PT, PH>(col, holder.self());
	}
	
	private	ValueExpression createValueExpression(Column col, ValueHolder<?, ?, ?> holder) {				
		return newValueExpression(col, holder.self());
	}
	
	private <
		V extends Serializable,
		P extends ValueType<P>, 
		PH extends ValueHolder<V, P, PH>
	>
	ValueExpression newValueExpression(Column col, ValueHolder<V, P, PH> holder) {				
		return new ImmutableValueParameter<V, P, PH>(col, holder.self());
	}	

    public UpdateStatement createUpdateStatement() throws EntityException {
        E pe = getTarget();
        
        if (!pe.isIdentified()) {
        	return null;
        }
        

    	final M meta = pe.getMetaData();
    	TableReference tref = new TableReference(meta.getBaseTable());
   	
    	Predicate pkp = getPKPredicate(tref, pe);

    	List<Assignment> al = new ArrayList<Assignment>();

    	for (A a : meta.attributes()) {
    		Column col = meta.getColumn(a);
    		ValueHolder<?, ?, ?> ph = pe.value(a);
    		
    		if (ph != null) {    		    		    		
	    		ValueExpression vp = createValueExpression(col, ph.self());    		
	    		al.add(new Assignment(col.getColumnName(), vp));
    		}
    	}
    	
    	Map<Column, Assignment> am = new LinkedHashMap<Column, Assignment>();
    	
    	for (R r : meta.relationships()) {
   			EntityKey<A, R, T, E, B, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(r);
   			processKey(ek, am);
	    }
    	
    	for (Map.Entry<Column, Assignment> e : am.entrySet()) {
    		Assignment a = e.getValue();
    		    		
			if (a == null) {
				Column column = e.getKey();				
				a = new Assignment(column.getColumnName(), null);
			}
			
			al.add(a);    	
    	}
    	
    	ElementList<Assignment> ael = ElementList.newElementList(al);
    	    	    	
    	return new UpdateStatement(tref, ael, pkp);
    }

    private 
    <	
    	RA extends AttributeName,
		RR extends Reference,	
		RT extends ReferenceType<RA, RR, RT, RE, RB, RH, RF, RM>,		
		RE extends Entity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RB extends MutableEntity<RA, RR, RT, RE, RB, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RB, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RB, RH, RF, RM>,
		RK extends EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK>
    >    
    void processKey(EntityKey<A, R, T, E, B, H, F, M, RA, RR, RT, RE, RB, RH, RF, RM, RK> key, Map<Column, Assignment> am) {
    	
//    	logger().debug("processKey - enter: " + key.name());
    	
    	final E e = getTarget();
    	final M m = e.getMetaData();
    	    	
    	RH rh = e.getRef(key.self());
    	
//    	logger().debug("rh: " + rh);
    	
    	if (rh == null) {
    		return;
    	}
    	    	    	
		ForeignKey fk = m.getForeignKey(key.name());			

		if (rh.isNull()) {
//			logger().debug("null ref: " + rh);
			
			for (Column c : fk.getColumnMap().values()) {
		      	if (!am.containsKey(c)) {
		      		am.put(c, null);
		      	}
		      }
		  }
		  else {
			  RE re = rh.value();
			  
//			  logger().debug("ref: " + re);
//			  logger().debug("fk-cols: " + fk.columns().size());
			  
		      for (Column ce : fk.getColumnMap().values()) {
		          Column fc = fk.getReferenced(ce);		          		          
		          ValueHolder<?, ?, ?> ph = re.get(fc);
		          
		          logger().debug("rc: " + fc + " => " + ph);
		          		          		          
		          if (ph != null) {    		    		    		
			  		ValueExpression vp = createValueExpression(ce, ph.self());    		
				    am.put(ce, new Assignment(ce.getColumnName(), vp));
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
//    			logger().debug("qs: " + qs);
    			ps = c.prepareStatement(qs);
//    			logger().debug("ps sh: " + System.identityHashCode(ps));
    			ds.traverse(null, createAssignmentVisitor(ps));
    			int deleted = ps.executeUpdate();
    		    			
//    			logger().debug("deleted: " + deleted);
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
        B dest = pe.asMutable();
        
        if (dest == null) {
        	dest = pe.toMutable();
        }
        

		InsertStatement q = createInsertStatement();
		String qs = q.generate();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
//			logger().debug("insert: qs=" + qs);			
//			logger().debug("JDBC-driver :" + c.getMetaData().getDriverName());
//			logger().debug("JDBC-driver-maj:" + c.getMetaData().getDriverMajorVersion());
//			logger().debug("JDBC-driver-min:" + c.getMetaData().getDriverMinorVersion());
//			logger().debug("JDBC-support-get-gen:" + c.getMetaData().supportsGetGeneratedKeys());

			ps = c.prepareStatement(qs, Statement.RETURN_GENERATED_KEYS);
//			logger().debug("ps sh: " + System.identityHashCode(ps));
			q.traverse(null, createAssignmentVisitor(ps));

//			logger().debug("q: " + qs);
//			logger().debug("ps: " + ps.toString());
			
			int ins = ps.executeUpdate();
//			logger().debug("inserted: " + ins);
			
			GeneratedKeyHandler kh = getKeyHandler();
			kh.processGeneratedKeys(q, dest, ps);
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
    
    private E load(EntityQueryElement<A, R, T, E, B, H, F, M, PMElement> qe, Connection c) 
		throws EntityException, SQLException, QueryException  {
    	E stored = null;

		EntityQueryExecutor<A, R, T, E, B, H, F, M, PMElement> ee = 
				new EntityQueryExecutor<A, R, T, E, B, H, F, M, PMElement>(getPersistenceContext(), getUnificationContext());
		
		
		PMElement pe = qe.self();    		
		
		EntityQuery.Builder<A, R, T, E, B, H, F, M, PMElement> builder = new DefaultEntityQuery.Builder<A, R, T, E, B, H, F, M, PMElement>(pe);
				
		E e = getTarget();
				
		E pk = e.toPrimaryKey();
		ColumnMap cm = e.getMetaData().getBaseTable().getPrimaryKey().getColumnMap();
		
		int size = cm.size(); 
		
		for (int i = 0; i < size; i++) {
			Column col = cm.get(i);
			ValueHolder<?, ?, ?> val = pk.get(col);			
			ValueExpression ve = createValueExpression(col, val);
			builder.addPredicate(col.getColumnName(), Comparison.Op.EQ, ve);
		}
		
		EntityQuery<A, R, T, E, B, H, F, M, PMElement> query = builder.newQuery();
		
		EntityQueryResult<A, R, T, E, B, H, F, M, PMElement> er = ee.execute(query, null, c);
		QueryResult<EntityDataObject<E>> qr = er.getContent();    		
		List<? extends EntityDataObject<E>> cl = qr.getContent();
		logger().debug("merge: cl.size()=" + cl.size());
		
		stored = cl.isEmpty() ? null : cl.get(0).getRoot(); 
    	
    	return stored;
    }
    
    
    /**
     * 
     * @param c
     * @return
     * @throws EntityException
     * @throws SQLException
     * @throws QueryException
     */    
    public E sync(Connection c)
    	throws EntityException, SQLException, QueryException  {

    	E stored = null;

    	if (getTarget().isIdentified()) {
    		PMElement eq = new PMElement(getTarget().getMetaData());
    		stored = load(eq, c);    		
    	}
    	
    	return stored;
    }

    	

    public void merge(Connection c) 
    	throws EntityException, SQLException, QueryException  {
    	
    	M meta = getTarget().getMetaData();
    	    	
    	PMElement qe = new PMElement(meta);   	    	

    	E stored = null;

    	if (getTarget().isIdentified()) {
    		stored = load(qe, c);    		
    	}
    	
    	logger().debug("merge: stored=" + stored);    	
    	    	
    	mergeDependencies(getTarget(), qe, c);    	

    	if (stored == null) {
    		insert(c);
    	}
    	else {
    	    update(c);
    	}
    }

    private 
    <
    	DA extends AttributeName,
    	DR extends Reference,
    	DT extends ReferenceType<DA, DR, DT, DE, DB, DH, DF, DM>,	
		DE extends Entity<DA, DR, DT, DE, DB, DH, DF, DM>,
		DB extends MutableEntity<DA, DR, DT, DE, DB, DH, DF, DM>,
		DH extends ReferenceHolder<DA, DR, DT, DE, DH, DM>,
		DF extends EntityFactory<DE, DB, DH, DM, DF>,
		DM extends EntityMetaData<DA, DR, DT, DE, DB, DH, DF, DM>, 
		DX extends EntityQueryElement<DA, DR, DT, DE, DB, DH, DF, DM, DX>
    >    
    void mergeDependencies(DE target, DX qe, Connection c) throws EntityException, SQLException, QueryException {
    	
    	logger().debug("mergeDependencies: target=" + target);
    	
    	DM m = target.getMetaData();    	
    	Set<DR> rs = m.relationships();
    	
    	final MergeMode ms = getMergeMode();
    	
    	for (DR dr : rs) {
			EntityKey<DA, DR, DT, DE, DB, DH, DF, DM, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = m.getEntityKey(dr);
			
			String id = ek.name().identifier();
			logger().debug("mergeDependencies: id=" + id);			
			
			ReferenceHolder<?, ?, ?, ?, ?, ?> rh = ek.get(target);
			
			if (rh == null || rh.isNull()) {
				logger().debug("no ref");
				continue;
			}
			
			Entity<?, ?, ?, ?, ?, ?, ?, ?> rv = rh.value();
								
			if (ms == MergeMode.ALL || (!rv.isIdentified())) {
				logger().debug("merging dependency: " + id);
				PersistenceManager<?, ?, ?, ?, ?, ?, ?, ?> pm = create(rv, getPersistenceContext());
				pm.merge(c);
			}
			else {
				logger().debug("no merge for " + id);
			}
		}
	}
    

	public void update(Connection c) throws EntityException {
    	if (!getTarget().isIdentified()) {
    		throw new EntityException("no primary key available");
    	}

    	UpdateStatement q = createUpdateStatement();

    	if (q != null) {
    		try {
	    		StatementExecutor se = new StatementExecutor(getPersistenceContext());
	    		se.executeUpdate(q, c);
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


//    private Predicate getPKPredicate() throws EntityException {
//        E pe = getTarget();
//    	EntityMetaData<A, R, T, E, ?, ?, ?, ?> meta = pe.getMetaData();
//    	TableReference tref = new TableReference(meta.getBaseTable());
//    	Predicate pkp = getPKPredicate(tref, pe);
//        return pkp;
//    }

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
    	if (target == null) {
			throw new NullPointerException("target");
		}
    	
        this.target = target;
    }
   
    private Predicate getPKPredicate(TableReference tref, E pe)
            throws EntityException {

        EntityMetaData<A, R, T, E, B, ?, ?, ?> meta = pe.getMetaData();
        Collection<Column> pkcols = meta.getBaseTable().getPrimaryKey().getColumnMap().values();

        if (pkcols.isEmpty()) {
            throw new EntityException("no pk-columns available for entity type " + pe.type());
        }

        Predicate p = null;

        for (Column col : pkcols) {
            ValueHolder<?, ?, ?> o = pe.get(col);

            // to successfully create a pk predicate
            // every component must be set:
            if (o == null) {
                return null;
            }

            ColumnReference cr = new ColumnReference(tref, col);
            ValueExpression param = createValueExpression(col, o.self());
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
		logger.debug("get persistenceContext: " + persistenceContext);		
		return persistenceContext;
	}

	private void setPersistenceContext(PersistenceContext<?> persistenceContext) {
		logger.debug("set persistenceContext: " + persistenceContext);		
		this.persistenceContext = persistenceContext;
	}
	
	private <
		DA extends AttributeName, 
		DR extends com.appspot.relaxe.ent.Reference, 
		DT extends ReferenceType<DA, DR, DT, DE, DB, DH, DF, DM>, 
		DE extends Entity<DA, DR, DT, DE, DB, DH, DF, DM>,
		DB extends MutableEntity<DA, DR, DT, DE, DB, DH, DF, DM>,
		DH extends ReferenceHolder<DA, DR, DT, DE, DH, DM>,
		DF extends EntityFactory<DE, DB, DH, DM, DF>,
		DM extends EntityMetaData<DA, DR, DT, DE, DB, DH, DF, DM>
	>
	PersistenceManager<DA, DR, DT, DE, DB, DH, DF, DM> create(Entity<DA, DR, DT, DE, DB, DH, DF, DM> e, PersistenceContext<?> pc) {
		PersistenceManager<DA, DR, DT, DE, DB, DH, DF, DM> pm = new PersistenceManager<DA, DR, DT, DE, DB, DH, DF, DM>(e.self(), pc, getMergeMode(), getUnificationContext());
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
