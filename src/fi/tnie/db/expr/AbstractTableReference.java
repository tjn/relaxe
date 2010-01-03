/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


import fi.tnie.db.QueryContext;

public abstract class AbstractTableReference
	extends CompoundElement
	implements TableRefList {
								
	public AbstractTableReference() {
		super();				
	}
	
	public abstract OrdinaryIdentifier getCorrelationName(QueryContext qctx);

//	public OrdinaryIdentifier getCorrelationName(QueryContext qctx) {
//		return qctx.correlationName(this);
//	}
	
	public AbstractTableReference innerJoin(AbstractTableReference right, JoinCondition jc) {
		return new JoinedTable(this, right, JoinType.INNER, jc);
	}
		
	public AbstractTableReference leftJoin(AbstractTableReference right, JoinCondition jc) {
		return new JoinedTable(this, right, JoinType.LEFT, jc);
	}	
	
	/**
	 * List of uncorrelated column names.
	 * 
	 * @return
	 */
	protected abstract ElementList<? extends ColumnName> getUncorrelatedColumnNameList();
	
	
	/**
	 * List of (possibly) correlated column names.
	 * 
	 * @return
	 */	
	public abstract ElementList<? extends ColumnName> getColumnNameList();
	
	

	
//	/**
//	 * 
//	 * @return
//	 */
//	public abstract ElementList<SelectListElement> getSelectList();

//	protected void copyElementList(AbstractTableReference src, ElementList<SelectListElement> dest) {
//		if (src != null) {
//			src.getSelectList().copyTo(dest);
//		}
//	}
	
	public abstract void addAll(ElementList<SelectListElement> dest);

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		traverseContent(vc, v);
		v.end(this);		
	}
	

	
	
}
