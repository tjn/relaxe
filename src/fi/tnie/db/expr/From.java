/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class From extends AbstractClause {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3675394470595921573L;
	private TableRefList tableReferenceList;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected From() {
	}
	
	public From(TableRefList from) {
		super(Keyword.FROM);
		
		if (from == null) {
			throw new NullPointerException("'from' must not be null");
		}
		
		setTableReferenceList(from);
	}

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("FROM ");
//		getTableReferenceList().generate(qc, dest);
//	}

	public TableRefList getTableReferenceList() {
		return tableReferenceList;
	}

	public void setTableReferenceList(TableRefList tableReferenceList) {
		this.tableReferenceList = tableReferenceList;
	}

	@Override
	protected Element getContent() {		
		return getTableReferenceList();
	}
	
	
}
