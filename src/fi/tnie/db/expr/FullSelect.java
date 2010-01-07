/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class FullSelect
	extends SelectQuery {

	private Subselect subselect;
	private OrderBy orderBy;	
	
	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public Subselect getSubselect() {
		return subselect;
	}

	public void setSubselect(Subselect subselect) {
		this.subselect = subselect;
	}
	
	public Subselect nest() {
		return new NestedSelect(this);
	}
	
	@Override
	Select getSelect() {		
		return getSubselect().getSelect();
	}
	
	
	
	
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		getSubselect().traverse(vc, v);
		
		OrderBy o = getOrderBy();
						
		if (o != null) {
			o.traverse(vc, v);		
		}				
	}
}
