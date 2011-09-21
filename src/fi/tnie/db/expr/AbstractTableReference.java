/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;



public abstract class AbstractTableReference
	extends CompoundElement
	implements TableRefList {
								
	/**
	 * 
	 */
	private static final long serialVersionUID = -712700693670065896L;

	public AbstractTableReference() {
		super();
	}
	
	public abstract OrdinaryIdentifier getCorrelationName(QueryContext qctx);
	
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
//	public abstract ElementList<ValueElement> getSelectList();

//	protected void copyElementList(AbstractTableReference src, ElementList<ValueElement> dest) {
//		if (src != null) {
//			src.getSelectList().copyTo(dest);
//		}
//	}
	
	public abstract int getColumnCount();
	
	public abstract void addAll(ElementList<SelectListElement> dest);

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		traverseContent(vc, v);
		v.end(this);		
	}

	@Override
	public final int getCount() {
		return 1;
	}

	@Override
	public AbstractTableReference getItem(int i) {		
		if (i == 0) {
			return this;
		}
		
		throw new IndexOutOfBoundsException("expected 0, actual: " + i);		
	}	

	public abstract SelectListElement getAllColumns();
}
