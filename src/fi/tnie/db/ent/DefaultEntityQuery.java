/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.SelectListElement;
import fi.tnie.db.expr.ValueElement;
import fi.tnie.db.expr.TableColumnExpr;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class DefaultEntityQuery<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
	> implements EntityQuery<A, R, Q, T, E>
{		
	private EntityMetaData<A, R, Q, T, ? extends E> meta;
	private DefaultTableExpression query;
	
	private TableReference tableRef;
			
//	private static Logger logger = Logger.getLogger(DefaultEntityQuery.class);
									
	public DefaultEntityQuery(EntityMetaData<A, R, Q, T, ? extends E> meta) {
		super();				
		this.meta = meta;
	}	
	
	public DefaultTableExpression getQuery() {		
		if (query == null) {
			DefaultTableExpression q = new DefaultTableExpression();		
			TableReference tref = getTableRef();
			q.setFrom(new From(tref));
					
			Select s = new Select();
			
			ElementList<SelectListElement> p = s.getSelectList();

			for (A a : meta.attributes()) {
				Column c = meta.getColumn(a);
				TableColumnExpr tce = new TableColumnExpr(tref, c);
				p.add(new ValueElement(tce, tce.getColumnName()));
			}
			
			// get columns from foreign keys, 
			// but remove duplicates
			
			Set<Column> kc = new HashSet<Column>();
			
			for (R r : meta.relationships()) {								
				ForeignKey fk = meta.getForeignKey(r);				
				kc.addAll(fk.columns().keySet());				
			}
			
			for (Column c : kc) {
				p.add(new ValueElement(new TableColumnExpr(tref, c)));	
			}
			
//			logger().debug("projection: " + p);			
						
			q.setSelect(s);
			this.query = q;
		}
		
		return this.query;		
	}	
	
    public TableReference getTableRef() {
        if (this.tableRef == null) {
            this.tableRef = new TableReference(meta.getBaseTable());
        }
        
        return this.tableRef;
    }

	@Override
	public Long getLimit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public EntityMetaData<A, R, Q, T, ? extends E> getMetaData() {
		return this.meta;
	}    
}
