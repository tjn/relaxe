/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

import fi.tnie.db.QueryTask.ObjectExtractor;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.exec.QueryProcessorAdapter;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.TableExpression;
import fi.tnie.db.expr.ValueExpression;

public class DataObjectReader
	extends QueryProcessorAdapter {
	
	private List<DataObject> content;		
	private DefaultDataObject.MetaData meta;
	private ValueExtractor<?, ?>[] extractors;
									
	public DataObjectReader(QueryExpression qo, List<DataObject> content) {									
		TableExpression te = qo.getTableExpr();
		List<ValueExpression> el = te.getSelect().expandValueExprList();
		
		this.content = content;
		this.meta = new DefaultDataObject.MetaData(qo);			
		this.extractors = createExtractorArray(el);
	}

	private ValueExtractor<?, ?>[] createExtractorArray(List<ValueExpression> el) {
		int colno = 0;
		ValueExtractor<?, ?>[] xa = new ValueExtractor<?, ?>[el.size()];
																							
		for (ValueExpression expr : el) {
			colno++;				
			int sqltype = expr.getType();				
			
			ValueExtractor<?, ?> e = null;
				
			switch (sqltype) {
				case Types.INTEGER:					
				case Types.SMALLINT:
				case Types.TINYINT:
					e = new IntExtractor(colno);
					break;
				case Types.VARCHAR:
				case Types.CHAR:
					e = new VarcharExtractor(colno);	
					break;					
				default:
					e = new ObjectExtractor(colno);
					break;
			}
			
			xa[colno - 1] = e;
		}
		
		return xa;
	}
			
	@Override
	public void process(ResultSet rs, long ordinal) throws QueryException {
		try {
			DefaultDataObject o = new DefaultDataObject(this.meta);
			
			int count = this.extractors.length;
			
			for (int i = 0; i < count; i++) {
				ValueExtractor<?, ?> ve = this.extractors[i];					
				o.set(i, ve.extractValue(rs));
			}			
			
			this.content.add(o);																
		}
		catch (Throwable e) {
			throw new QueryException(e.getMessage(), e);
		}
	}
}