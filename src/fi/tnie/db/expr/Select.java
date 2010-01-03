/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class Select
	extends AbstractClause {
	
	private ElementList<SelectListElement> selectList;	
	private boolean distinct;	
	
	public Select() {
		super(Keyword.SELECT);		
	}

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("SELECT ");
//		
//		if (isDistinct()) {
//			dest.append("DISTINCT ");
//		}
//		
//		getSelectList().generate(qc, dest);
//		dest.append(" ");
//	}

	public ElementList<SelectListElement> getSelectList() {
		if (selectList == null) {
			selectList = new ElementList<SelectListElement>();			
		}

		return selectList;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}
	
	public SelectListElement add(ValueExpression expr) {
		return add(expr, null);
	}
	
	public SelectListElement add(ValueExpression expr, String newName) {
		if (expr == null) {
			throw new NullPointerException("'expr' must not be null");
		}
		
		SelectListElement e = new SelectListElement(expr, newName);
		getSelectList().add(e);
		return e;
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getClause().traverse(vc, v);
		
		if (this.distinct) {
			Keyword.DISTINCT.traverse(vc, v);
		}
		
		getContent().traverse(vc, v);	}
	
	@Override
	protected Element getContent() {		
		return getSelectList();
	}
		
	public ElementList<? extends ColumnName> getColumnNameList() {		
		ElementList<ColumnName> cl = null;
		ElementList<SelectListElement> p = getSelectList();
		
		if (!p.isEmpty()) {
			cl = new ElementList<ColumnName>();
			
			for (SelectListElement e : p.getContent()) {
				cl.add(e.getColumnName());
			}
		}
		
		return cl;
	}	
}
