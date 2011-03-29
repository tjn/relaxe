/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.EntityCompositor;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.expr.AbstractTableReference;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.ForeignKeyJoinCondition;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.SelectListElement;
import fi.tnie.db.expr.ValueElement;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class DefaultEntityQuery<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
	> implements EntityQuery<A, R, T, E>
{
	private EntityMetaData<A, R, T, E> meta;
	private DefaultTableExpression query;
	private TableReference tableRef;
	private EntityCompositor compositor;
	
//	private static Logger logger = Logger.getLogger(DefaultEntityQuery.class);

	public DefaultEntityQuery(EntityMetaData<A, R, T, E> meta) {
		super();
		this.meta = meta;		
	}

	public DefaultEntityQuery(E root)
		throws CyclicTemplateException {
		super();

		if (root == null) {
			throw new NullPointerException();
		}

		EntityMetaData<A, R, T, E> meta = root.getMetaData();
		BaseTable table = meta.getBaseTable();

		if (table == null) {
			throw new NullPointerException("EntityMetaData.getBaseTable()");
		}

		this.meta = meta;

		DefaultTableExpression q = new DefaultTableExpression();
		HashSet<Entity<?,?,?,?>> visited = new HashSet<Entity<?,?,?,?>>();

		AbstractTableReference tref = fromTemplate(root, null, null, q, visited);
		q.setFrom(new From(tref));

		this.query = q;
	}


	private
	<TA extends Attribute, TR>
	AbstractTableReference fromTemplate(
			Entity<TA,TR,?,?> template, 
			AbstractTableReference qref, ForeignKey fk, 
			DefaultTableExpression q, Set<Entity<?,?,?,?>> visited)
		throws CyclicTemplateException {
		if (visited.contains(template)) {
			throw new CyclicTemplateException(template);
		}
		else {
			visited.add(template);
		}

		Select s = q.getSelect();

		if (s == null) {
			q.setSelect(s = new Select());
		}

		EntityMetaData<TA,TR,?,?> meta = template.getMetaData();
		TableReference tref = null;

		if (qref == null) {
			tref = getTableRef();
			qref = tref;
		}
		else {
			tref = new TableReference(meta.getBaseTable());
			ForeignKeyJoinCondition jc = new ForeignKeyJoinCondition(fk, qref, tref);
			qref = qref.leftJoin(tref, jc);
		}

		Set<TA> as = meta.attributes();

		for (TA a : as) {
			PrimitiveHolder<?, ?> h = template.value(a);

			if (h != null) {
				Column c = meta.getColumn(a);

				if (c != null) {
					s.add(new ColumnReference(tref, c));
				}
			}
		}

		// TODO check:
//		Set<TR> rs = meta.relationships();
//
//		for (TR r : rs) {
//			Entity<?, ?, ?, ?, ?> e = template.get(r);
//
//			if (e != null) {
//				fk = meta.getForeignKey(r);
//
//				if (fk == null) {
//					throw new NullPointerException();
//				}
//
//				qref = fromTemplate(e, qref, fk, q, visited);
//			}
//		}

		return qref;
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
				ColumnReference cr = new ColumnReference(tref, c);
//				p.add(new ValueElement(cr, cr.getColumnName()));
				p.add(new ValueElement(cr));
			}


			// get columns from foreign keys,
			// but remove duplicates

			Set<Column> kc = new HashSet<Column>();

			for (R r : meta.relationships()) {
				ForeignKey fk = meta.getForeignKey(r);
				kc.addAll(fk.columns().keySet());
			}		
			

			for (Column c : kc) {				
				ColumnReference cr = new ColumnReference(tref, c);
				p.add(new ValueElement(cr));
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
	public EntityMetaData<A, R, T, E> getMetaData() {
		return this.meta;
	}
}
