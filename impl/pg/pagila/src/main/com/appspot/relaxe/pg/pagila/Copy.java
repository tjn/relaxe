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
package com.appspot.relaxe.pg.pagila;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.ColumnReference;
import com.appspot.relaxe.expr.DefaultTableExpression;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.From;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.expr.Parameter;
import com.appspot.relaxe.expr.Select;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ValueRow;
import com.appspot.relaxe.expr.ValuesListElement;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataTypeMap;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.QuerySession;
import com.appspot.relaxe.service.StatementSession;
import com.appspot.relaxe.service.UpdateReceiver;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.ValueHolder;

public class Copy {	
	
	private Schema sourceSchema;
	private Schema destinationSchema;
	private DataAccessSession source;
	private DataAccessSession destination;
		
	private static Logger logger = LoggerFactory.getLogger(Copy.class);		
	
	
	public Copy(Schema sourceSchema, Schema destinationSchema,
			DataAccessSession source, DataAccessSession destination) {
		super();
		this.sourceSchema = sourceSchema;
		this.destinationSchema = destinationSchema;
		this.source = source;
		this.destination = destination;				
	}

	public void run() throws QueryException, DataAccessException {
		
		
		SchemaElementMap<BaseTable> tm = sourceSchema.baseTables();
		
		QuerySession qs = source.asQuerySession();
		StatementSession ss = destination.asStatementSession();
				
		Environment tenv = destinationSchema.getEnvironment();
		SchemaElementMap<BaseTable> dtm = destinationSchema.baseTables();
		
		UpdateReceiver ur = new UpdateReceiver() {				
			@Override
			public void updated(Statement statement, int updateCount) {
				logger.debug("updated: {}", updateCount);
			}
		};
		
//		DataTypeMap sm = sourceSchema.getEnvironment().getDataTypeMap();
//		DataTypeMap dm = destinationSchema.getEnvironment().getDataTypeMap();
		DataTypeMap sm = source.getDataTypeMap();
		DataTypeMap dm = destination.getDataTypeMap();
		
		logger.info("source data type map: {}", sm);
		logger.info("destination data type map: {}", dm);
		
		
		for (BaseTable t : tm.values()) {
			logger.info("copying: {}", t.getQualifiedName());
			
//			if (!t.getQualifiedName().toLowerCase().contains("staff")) {
//				logger.info("skipped");
//				continue;
//			}
			
			Select.Builder b = new Select.Builder();
			TableReference tref = new TableReference(t);
			BaseTable tt = dtm.get(t.getUnqualifiedName().getContent());
			Collection<Column> cc = t.getColumnMap().values();
			List<Identifier> tcl = new ArrayList<>();
			
			for (Column scol : cc) {
				b.add(new ColumnReference(tref, scol));
				
				Identifier name = tenv.getIdentifierRules().toIdentifier(scol.getColumnName().getContent());
				tcl.add(name);
				
				
				ValueType<?> stype = sm.getType(scol.getDataType());
				
				Column tcol = tt.getColumnMap().get(name);
				ValueType<?> ttype = dm.getType(tcol.getDataType());			
				
				logger.warn("column: {}.{}: {} => {}", new Object[] { 
						t.getUnqualifiedName(), scol.getUnqualifiedName(), stype, ttype});
				
				
				if (stype == null) {
					logger.warn("unmapped src data type: {}", scol.getDataType());				
				}
								
				if (ttype == null) {
					logger.warn("unmapped dst data type: {}", tcol.getDataType());				
				}
				
				
				if (!stype.equals(ttype)) {
					logger.warn("column type mismatch: {}.{}: {} => {}", new Object[] { 
							t.getUnqualifiedName(), scol.getUnqualifiedName(), stype, ttype});
				}			
				
				// logger.debug("col: {} {}", scol.getUnqualifiedName(), stype);
			}
			
						
			DefaultTableExpression e = new DefaultTableExpression(b.newSelect(), new From(tref));
			List<ValueRow> rows = new ArrayList<ValueRow>();
			
			DataObjectQueryResult<DataObject> results = qs.execute(e, null);
															
			for (DataObject o : results.getContent()) {				
				int i = 0;
				
				List<ValuesListElement> elems = new ArrayList<ValuesListElement>();
				
				for (Column col : cc) {
					ValueHolder<?, ?, ?> h = o.get(i);
					Parameter<?, ?, ?> p = newParameter(col, h);
					elems.add(p);
					i++;
				}
				
				rows.add(new ValueRow(elems));
			}
			
			
			if (rows.isEmpty()) {
				logger.debug("no data to copy");
			}
			else {
				logger.debug("inserting all..");
				InsertStatement ins = new InsertStatement(tt, new ElementList<Identifier>(tcl), rows);
				ss.executeUpdate(ins, ur);		
				logger.debug("inserted");
			}			
			
		}
		
		
		
	}
	
	public 
	<
		V extends Serializable,
		T extends ValueType<T>, 
		H extends ValueHolder<V, T, H>
	>	
	Parameter<V, T, H> newParameter(Column c, ValueHolder<V, T, H> holder) {
		return new ImmutableValueParameter<V, T, H>(c, holder.self());
	}

}
