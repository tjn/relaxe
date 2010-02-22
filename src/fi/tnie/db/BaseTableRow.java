/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.Assignment;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.Parameter;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.UpdateStatement;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.ValueParameter;
import fi.tnie.db.expr.ValueRow;
import fi.tnie.db.expr.op.AndPredicate;
import fi.tnie.db.expr.op.Comparison;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.impl.ColumnMap;

public class BaseTableRow<C extends Enum<C> & Identifiable> 
	implements Row {
	
	private BaseTableRowMetaData<C> meta;	
	private EnumMap<C, Object> values;	
	private EnumMap<C, Parameter> parameterMap;
			
	private static Logger logger = Logger.getLogger(BaseTableRow.class);
	 
	public BaseTableRow(BaseTableRowFactory<C, BaseTableRow<C>, ?> source, BaseTableRowMetaData<C> meta) {
		super();
		
		if (meta == null) {
			throw new NullPointerException("'meta' must not be null");
		}
						
		int cc = meta.getColumnCount();
		
		if (cc < 1) {			
			throw new IllegalArgumentException("column count must be >= 1, was " + cc); 
		}
			
		this.meta = meta;
//		this.values = new Object[cc];		
		this.values = new EnumMap<C, Object>(meta.getColumnNameType());
	}	
	
	public void insert(Connection c) 
		throws SQLException {		
	 
		InsertStatement q = createInsertQuery();		
		String qs = q.generate();		
		final PreparedStatement ps = c.prepareStatement(qs, Statement.RETURN_GENERATED_KEYS);					
		q.traverse(null, new ParameterAssignment(ps));
			
		int ins = ps.executeUpdate();
		
		logger().debug("inserted: " + ins);
		
		ResultSet rs = ps.getGeneratedKeys();
		
		try {
									
			// TODO: read generated keys:									
//			RowFactory<K, E> ef = getFactory();
//			EnumMap<K, Integer> keys = ef.columns(rs.getMetaData());					
//			
//			if (rs.next()) {
//				ef.copy(keys, rs, this);				
//			}
		}
		finally {
			rs.close();
		}
	}

	
	
	public InsertStatement createInsertQuery() {

		ValueRow newRow = new ValueRow();
		
		BaseTable t = getTable();
		ColumnMap cm = t.columnMap();		
		ElementList<ColumnName> names = new ElementList<ColumnName>();
				
		for (Map.Entry<C, ?> e : values.entrySet()) {			
			C column = e.getKey();			
			Column col = cm.get(column.identifier());
			names.add(col.getColumnName());
			ValueParameter p = new ValueParameter(col, e.getValue());
			newRow.add(p);
		}
				
		return new InsertStatement(t, names, newRow);						
		
	}

	private void update(Connection c, BaseTableRowFactory<C, BaseTableRow<C>, ?> rf) 
		throws SQLException {
	
//		// reload:
//				
//		Predicate pkp = createPKPredicate();
//									
//		BaseTableRow<C> snapshot = reload(c, rf, pkp);
//		
//		if (snapshot == null) {
//			// TODO: return diff: 
//			throw new IllegalStateException("object was deleted");
//		}		
//						
//		UpdateStatement q = createUpdateQuery();				
//		String qs = q.generate();
//		
//		final PreparedStatement ps = c.prepareStatement(qs, Statement.RETURN_GENERATED_KEYS);						
//		q.traverse(null, new ParameterAssignment(ps));				
//		int ins = ps.executeUpdate();
//		
//		logger().debug("updated: " + ins);
//		
//		ResultSet rs = ps.getGeneratedKeys();
//		
//		try {			
////			EnumMap<K, Integer> keys = ef.columns(rs.getMetaData());					
////			
//			if (rs.next()) {
//				
//				
//				copyFrom(rs);
////				ef.copy(keys, rs, this);				
//			}
//		}
//		finally {
//			rs.close();
//		}
	}

	private BaseTableRow<C> reload(Connection c, Predicate pkp) 
		throws SQLException {
		
//		BaseTableRowFactory<C, BaseTableRow<C>> rf = getBaseTableRowMetaData().getFactory(); 
//		TableRowQuery<C, BaseTableRow<C>> q = new RowQuery<C, BaseTableRow<C>>(rf);
//		q.getQuery().getWhere().setSearchCondition(pkp);
//		
//		BaseTableRow<C> reloaded = q.exec(c).first();
//		
//		return reloaded;
		return null;
	}

	private Predicate createPKPredicate() {		
		Map<C, ?> pk = getPrimaryKey();
		
		if (pk == null) {
			return null;
		}
		
		Predicate p = null;
		
		for (C c : pk.keySet()) {			
			p = AndPredicate.newAnd(p, eq(c, getParameter(c)));
		}
				
		return p;
	}


	protected Map<C, ?> getPrimaryKey() {
		
		Map<C, ?> vm = values;
		Class<C> kt = meta.getColumnNameType();
		EnumMap<C, Object> pk = new EnumMap<C, Object>(kt);				
		Set<C> keys = meta.getPKDefinition();
		
		for (C c : keys) {
			Object value = vm.get(c);
			
			if (value == null) {
				return null;			
			}
			
			pk.put(c, value);
		}
			
		return pk;
	}		

///**
// * Returns true, if and only if every component of the primary key
// * is either set or known to be in auto-increment column.
// * 
// * @return
// */
//
//protected boolean isInsertable() {
//	PrimaryKey pk = getPKDefinition();		
//	Map<K, Object> vm = values();
//	
//	for (Column c : pk.columns()) {
//		Boolean ai = c.isAutoIncrement();		
//					
//		if (ai != null && ai.booleanValue() == false) {				
//			K key = getKey(c.getName());				
//			Object value = vm.get(key);
//			
//			if (value == null) {
//				return false;
//			}
//		}
//	}		
//	
//	return true;
//}

//public K getKey(String n)  {
//	Class<K> kt = getFactory().getColumnNameType();				
//	return Enum.valueOf(kt, n);
//}


protected void saved() {
	// TODO Auto-generated method stub		
}

protected void saving() {		
}		

//private PrimaryKey getPKDefinition() {
//	BaseTable t = getTable();
//	
//	if (t == null) {
//		throw new IllegalStateException("No base table for the row: " + this);			
//	}
//	
//	PrimaryKey pk = t.getPrimaryKey();
//	
//	if (pk == null) {
//		throw new IllegalStateException("No primary key in table: " + t.getQualifiedName());
//	}		
//	
//	return pk;
//}

//public void markLoaded() {		
////	this.loadedAs = new EnumMap<K, Object>(values());		 		
//}

	public Predicate eq(C c, ValueExpression expr) {				
		return Comparison.eq(getParameter(c), expr);
	}
	
//public Predicate loadedAs(RowQuery<K, E> q) {
//	Predicate p = null;		
//	
//	RowFactory<K, E> ef = getFactory();
//	TableReference tref = q.getTableReference();
//			
//	for (K k : ef.getPKDefinition()) {
//		Column c = ef.getColumn(k);
//		Object v = loadedAs.get(k);			
//					
//		p = AndPredicate.newAnd(p, Comparison.eq(
//				new TableColumnExpr(tref, c), new ValueParameter(c, v)));
//	}
//	
//	return p;
//}




/**
 * Returns a parameter view to the <code>key</code> value of this Row.
 * Further changes to this value are visible via the returned parameter object.
 * 
 * @param key
 * @return
 */

public Parameter getParameter(final C column) {
	if (column == null) {
		throw new NullPointerException("'column' must not be null");
	}
	
	EnumMap<C, Parameter> pm = getParameterMap();
	Parameter p = pm.get(column);
	
	if (p == null) {		
		Column c = meta.getTable().columnMap().get(column.toString());
		
		p = new Parameter(c) {
			@Override
			public Object getValue() {					
				return values.get(column);
			}
		};
	}
	
	return p;
}



	private EnumMap<C, Parameter> getParameterMap() {
		if (parameterMap == null) {
			parameterMap = new EnumMap<C, Parameter>(meta.getColumnNameType());			
		}
	
		return parameterMap;
	}
	

	@Override
	public RowMetaData getMetaData() {
		return getBaseTableRowMetaData();
	}
	
	public BaseTableRowMetaData<C> getBaseTableRowMetaData() {
		return this.meta;
	}
		
	public Object get(C column) {		
		return values.get(column);
	}
	
	
	private BaseTable getTable() {
		return getBaseTableRowMetaData().getTable();		
	}
	
	public static Logger logger() {
		return BaseTableRow.logger;
	}

	public class RowExtractor 
		extends AbstractExtractor {

		private BaseTableRow<C> dest;
		private C column;
		
		public RowExtractor(BaseTableRow<C> dest, int ordinal, C col) {
			super(ordinal);
			this.dest = dest;
			this.column = col;
		}
	
		@Override
		public void extract(ResultSet rs) throws SQLException {
			Object value = rs.getObject(getOrdinal());		
			dest.values.put(this.column, value);
		}
	}

	
	public UpdateStatement createUpdateQuery() {
		BaseTable table = getTable();
		ColumnMap cm = table.columnMap();
		
		ElementList<Assignment> assignments = new ElementList<Assignment>(); 
				
		for (Map.Entry<C, ?> e : values.entrySet()) {			
			C column = e.getKey();			
			Column col = cm.get(column.toString());
			ValueParameter p = new ValueParameter(col, e.getValue());
			assignments.add(new Assignment(col.getColumnName(), p));
		}
									
		UpdateStatement q = new UpdateStatement(table, assignments);				
		return q;		
	}

	@Override
	public Object get(int ordinal) {
		// getMetaData().get
		return null;
	}
	
}
