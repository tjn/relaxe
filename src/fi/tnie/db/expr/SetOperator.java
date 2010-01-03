/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class SetOperator
	extends CompoundElement
	implements Subselect {
	
	private Name operator;
	private Keyword all;
	private Subselect left;
	private Subselect right;
	
	public enum Name
		implements Element {
		UNION,
		INTERSECT,
		EXCEPT;
		
		private Keyword operator;
		
		private Name() {
			this.operator = Keyword.valueOf(this.toString());
		}
		
		@Override
		public void traverse(VisitContext vc, ElementVisitor v) {
			vc = v.start(vc, this);
			this.operator.traverse(vc, v);
		}
		
		@Override
		public String getTerminalSymbol() {			
			return null;
		}
	}

	public SetOperator(Name operator, boolean all, Subselect left, Subselect right) {
		super();
		
		if (operator == null) {
			throw new NullPointerException("'operator' must not be null");
		}
		
		if (left == null) {
			throw new NullPointerException("'left' must not be null");
		}
		
		if (right == null) {
			throw new NullPointerException("'right' must not be null");
		}
		
		this.operator = operator;
		this.left = left;
		this.right = right;
		this.all = all ? Keyword.ALL : null;
	}

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		left.generate(qc, dest);
//		operator.generate(qc, dest);
//		
//		if (isAll()) {
//			dest.append("ALL ");			
//		}
//		
//		right.generate(qc, dest);
//	}

	public boolean isAll() {
		return this.all != null;
	}

	public void setAll(boolean all) {
		this.all = all ? Keyword.ALL : null;
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		left.traverse(vc, v);
		operator.traverse(vc, v);
		
		if (this.all != null) {
			this.all.traverse(vc, v);						
		}
		
		right.traverse(vc, v);
	}
		
	@Override
	public Select getSelect() {		
		return this.left.getSelect();
	}
}
