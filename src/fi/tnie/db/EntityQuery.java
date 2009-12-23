package fi.tnie.db;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import sun.security.action.GetBooleanAction;

import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Table;

public class EntityQuery<K extends Enum<K>, E extends DBEntity<K, E>>
	implements QueryExpression
{	
	private BaseTable table;
	private List<K> projection;	
	private Class<K> columnNameType;
	
	private EntityPredicate<K, E> predicate;	 
		
	public EntityQuery(BaseTable table, Class<K> columnNameType, List<K> projection) {
		this(table, columnNameType);
		
		if (projection != null) {
			if (projection.isEmpty()) {
				throw new IllegalArgumentException("projection must not be empty");
			}
			
			this.projection = new ArrayList<K>(projection);
		}
	}
	
	public EntityQuery(BaseTable table, Class<K> columnNameType) {
		super();
		this.table = table;
		this.columnNameType = columnNameType;
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
//		String tref = qc.start(getTable(), "r");
//		
//		try {					
//			dest.append(" SELECT ");
//			select(qc, tref, dest);
//			dest.append(" FROM ");
//			dest.append(getTable().getName());
//			
//			EntityPredicate<K, E> ep = getPredicate();
//			
//			if (ep != null) {
//				dest.append(" WHERE ");
//				ep.generate(qc, dest);
//			}
//			
//			String g = groupBy(qc);
//			
//			if (g != null) {
//				dest.append(" GROUP BY ");
//				dest.append(g);
//			}
//			
//			String h = having(qc);
//			
//			if (h != null) {
//				dest.append(" HAVING ");
//				dest.append(h);
//			}			
//			
//			String o = orderBy(qc);
//			
//			if (o != null) {
//				dest.append(" ORDER BY ");
//				dest.append(o);
//			}				
//			
//		}
//		finally {
//			qc.end(tref);
//		}
	}

	private String orderBy(QueryContext qc) {
		// TODO Auto-generated method stub
		return null;
	}

	private String having(QueryContext qc) {
		// TODO Auto-generated method stub
		return null;
	}

	private String groupBy(QueryContext qc) {
		return null;
	}

	private String where(QueryContext qc) {	
		return null;
	}

	private void select(QueryContext qc, String tref, StringBuffer dest) {		
		List<K> cl = this.getProjection();
		String prefix = (tref == null) ? null : tref + ".";		
		dest.append(" ");
		
		if (cl == null) {			
			if (prefix != null) {
				dest.append(prefix);
			}
			
			dest.append("*");
		}
		else {
			join(cl, ",", prefix, null, dest);
		}
		
		dest.append(" ");
	}
	
	private String join(Iterable<?> elems, String delim, String prefix, String suffix, StringBuffer dest) {		
		for (Iterator<?> iter = elems.iterator(); iter.hasNext();) {
			String e = iter.next().toString();
	
			if (prefix != null) {			
				dest.append(prefix);
			}
			
			dest.append(e);
			
			if (prefix != null) {			
				dest.append(prefix);
			}			
			
			if (delim != null) {
				if (iter.hasNext()) {
					dest.append(delim);
				}
			}
		}
		
		return null;
		
	}
	
	
	private BaseTable getTable() {
		return table;
	}

	private void setTable(BaseTable table) {
		this.table = table;
	}

	private List<K> getProjection() {
		return projection;
	}

	private void setProjection(List<K> projection) {
		this.projection = projection;
	}

	public EntityPredicate<K, E> getPredicate() {
		return predicate;
	}

	public void setPredicate(EntityPredicate<K, E> predicate) {
		this.predicate = predicate;
	}	
}
