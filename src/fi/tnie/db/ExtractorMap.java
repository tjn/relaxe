/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ColumnMap;
import fi.tnie.db.types.ReferenceType;

public class ExtractorMap<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
> {

	private List<AttributeExtractor<?, ?, ?, A, E, ?>> attributeExctractorList;

	private static Logger logger = Logger.getLogger(ExtractorMap.class);


	/**
	 * TODO: when initialized like this, this does not read references properly
	 * @param rsmd
	 * @param em
	 * @throws SQLException
	 */
	public ExtractorMap(ResultSetMetaData rsmd,
			EntityMetaData<A, R, T, E> em,
			ValueExtractorFactory vef) throws SQLException {
		int cc = rsmd.getColumnCount();
		this.attributeExctractorList =
			new ArrayList<AttributeExtractor<?, ?, ?, A, E, ?>>(cc);

		ColumnMap cm = em.getBaseTable().columnMap();

		for (int c = 1; c <= cc; c++) {
			String cl = rsmd.getColumnLabel(c);
			Column col = cm.get(cl);
			int t = col.getDataType().getDataType();

			AttributeExtractor<?, ?, ?, A, E, ?> ae = createAttributeExtractor(t, c, col, em, vef);

			if (ae != null) {
				this.attributeExctractorList.add(ae);
			}

//			ValueExtractor<?, ?, ?> ve = vef.createExtractor(rsmd, c);
//
////			A a = em.getAttribute(col);
//
//			if (a != null) {
//				logger().info("attribute for column label: " + cl + " => " + a);
//				AttributeExtractor<?, ?, ?, A, E> ae = createAttributeExtractor(a, ve);
//				this.attributeExctractorList.add(ae);
//			}
//			else {
//				logger().warn("no attribute for column label: " + cl);
//				logger().warn("column: " + col);
//			}


//			Set<R> refs = em.getReferences(col);
//
//			for (R r : refs) {
//				ForeignKey fk = em.getForeignKey(r);
//			}
		}
	}

	public AttributeExtractor<?, ?, ?, A, E, ?> createAttributeExtractor(int sqltype, int c, Column col, EntityMetaData<A, R, T, E> em, ValueExtractorFactory vef)
				throws SQLException {

		final A attribute = em.getAttribute(col);
		AttributeExtractor<?, ?, ?, A, E, ?> ae = null;

		switch (sqltype) {
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.TINYINT:
				ae = new IntegerAttributeExtractor<A, E>(attribute, em, vef, c);
				break;
			case Types.VARCHAR:
//				e = new VarcharExtractor(col);
			case Types.CHAR:
//				e = new CharExtractor(col);
				break;
			case Types.DATE:
//				e = new DateExtractor(col);
				break;
			case Types.TIMESTAMP:
//				e = new TimestampExtractor(col);
				break;
			default:
				//
	//			e = new ObjectExtractor(colno);
				break;
		}

		logger().debug("createExtractor - exit " + col + ": " + sqltype + " => " + ae);

		return ae;
	}

	public ExtractorMap(QueryExpression e, TableReference b) throws SQLException {
		// e.getTableExpr().getSelect().getSelectList()
//		e.getTableExpr().getSelect().get
	}


	public void extract(ResultSet src, E e) throws SQLException {
		int ec = attributeExctractorList.size();

		for (int i = 0; i < ec; i++) {
			attributeExctractorList.get(i).extract(src, e);
		}
	}

	private static Logger logger() {
		return ExtractorMap.logger;
	}
}
