package com.appspot.relaxe;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.ent.query.EntityQueryExpressionBuilder;
import com.appspot.relaxe.ent.value.HasLongVarBinary;
import com.appspot.relaxe.ent.value.LongVarBinaryAttribute;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryTime;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;

public class BinaryAttributeQueryExecutor<
	A extends AttributeName,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, B, H, F, M>,
	E extends Entity<A, R, T, E, B, H, F, M> & HasLongVarBinary.Read<A, E, B>,
	B extends MutableEntity<A, R, T, E, B, H, F, M> & HasLongVarBinary.Write<A, E, B>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, B, H, M, F>,
	M extends EntityMetaData<A, R, T, E, B, H, F, M>,
	RE extends EntityQueryElement<A, R, T, E, B, H, F, M, RE>
> 	
	extends EntityQueryExecutor<A, R, T, E, B, H, F, M, RE> {
	
	private static Logger logger = LoggerFactory.getLogger(EntityQueryExecutor.class);

	private static final class ExtractProcessor extends QueryProcessorAdapter {
		private final ByteArrayExtractor be;
		private final OutputStream out;
		
		private long count;

		private ExtractProcessor(ByteArrayExtractor be, OutputStream out) {
			this.be = be;
			this.out = out;
			this.count = 0;
		}
		
		@Override
		public void startResultSet(ResultSetMetaData m) throws QueryException, SQLException, IOException {
			logger.info("startResultSet");			
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws QueryException, SQLException, IOException {
			logger.info("extracting at: {}", ordinal); 
			
			long n = be.extract(rs, out);
			logger.info("bytes extracted: {}", n);
			this.count += n;
		}
		
		@Override
		public void endResultSet() throws QueryException {
			logger.info("endResultSet");
		}
		
		public long getCount() {
			return count;
		}
	}

	public BinaryAttributeQueryExecutor(PersistenceContext<?> persistenceContext, UnificationContext unificationContext) {
		super(persistenceContext, unificationContext);	
	}

	public long readAll(EntityQuery<A, R, T, E, B, H, F, M, RE> query, final LongVarBinaryAttribute<A, E, B> attr, final OutputStream out, Connection c) 
			throws SQLException, QueryException, EntityException, IOException {
		
		RE re = query.getRootElement();
					
		QueryExecutor se = getExecutor();		
		PersistenceContext<?> pc = se.getPersistenceContext();
							
		
		EntityQueryExpressionBuilder<A, R, T, E, B, H, F, M, RE> eqb = newBuilder(query);
				
		
		ValueExtractorFactory vef = pc.getValueExtractorFactory();
		
		int no = eqb.findColumn(re, attr.name());
		
		if (no < 1) {
			throw new IllegalArgumentException("Root query element does not contain attribute '" + attr.name() + "'");
		}	
		
		final ByteArrayExtractor be = vef.createByteArrayExtractor(no);
		
		QueryExpression qe = newQueryExpression(eqb);		
						
		StatementExecutor sx = new StatementExecutor(pc, 1);
				
		
		SelectStatement ss = new SelectStatement(qe);
		
		ExtractProcessor ep = new ExtractProcessor(be, out);
		
		
		QueryTime qt = sx.executeSelect(ss, c, ep);
		
		logger.info("readAll: et: {}, pt: {}", qt.getExecutionTime(), qt.getPopulationTime());
				
		
		return ep.getCount();		
	}
	
}
