/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class OrderBy	
	extends AbstractClause {

	private ElementList<SortKey> sortKeyList;
			
	public enum Order implements Element {
		ASC,
		DESC;

		@Override
		public void traverse(VisitContext vc, ElementVisitor v) {
			v.start(vc, this);
			v.end(this);
		}

		@Override
		public String getTerminalSymbol() {
			return super.toString();			
		}
	}
	
	public abstract static class SortKey	
		extends CompoundElement {
		
		protected Order order;
		
		protected SortKey() {
			this(null);
		}
		
		protected SortKey(Order order) {
			super();
			this.order = ((order == null) ? Order.ASC : order);
		}
	}
	
	public static class ExprSortKey
		extends SortKey {
		
		private Expression expression;
	
		public ExprSortKey(Expression expression, Order order) {
			super(order);
			this.expression = expression;
		}

//		@Override
//		public void generate(SimpleQueryContext qc, StringBuffer dest) {
//			
//			expression.generate(qc, dest);	
//						
//			if (this.order == Order.DESC) {								
//				dest.append(this.order);
//				dest.append(" ");
//			}	
//		}
		
		@Override
		public void traverseContent(VisitContext vc, ElementVisitor v) {
			this.expression.traverse(vc, v);
			this.order.traverse(vc, v);
		}
	}
		
	public static class OrdinalSortKey
		extends SortKey {
		
		private Ordinal ordinal;
		
		public OrdinalSortKey(int ordinal, Order order) {
			super(order);
			this.ordinal = new Ordinal(ordinal);
		}

		public int getOrdinal() {
			return this.ordinal.ordinal;
		}
		
		@Override
		public void traverseContent(VisitContext vc, ElementVisitor v) {
			this.ordinal.traverse(vc, v);
			this.order.traverse(vc, v);
		}
	}
	
	public static class Ordinal
		extends SimpleElement {
		
		private int ordinal;
		
		public Ordinal(int ordinal) {
			this.ordinal = ordinal;
		}

		@Override
		public String getTerminalSymbol() {
			return Integer.toString(this.ordinal);
		}

		@Override
		public void traverse(VisitContext vc, ElementVisitor v) {
			v.start(vc, this);
			v.end(this);			
		}		
	}

	public OrderBy() {
		super(Keyword.ORDER_BY);
	}
	
	public void add(TableColumnExpr c, Order o) {
		getSortKeyList().add(new ExprSortKey(c, o));
	}
	
	public void add(int ord, Order o) {
		getSortKeyList().add(new OrdinalSortKey(ord, o));
	}

	public ElementList<SortKey> getSortKeyList() {
		if (sortKeyList == null) {
			sortKeyList = new ElementList<SortKey>();
		}

		return sortKeyList;
	}
	
	
//	private static class Ordinal
//		implements Element {
//		
//		private int ordinal;
//		
//		public Ordinal(int ordinal) {
//			super();
//			this.ordinal = ordinal;
//		}
//
//		@Override
//		public void generate(SimpleQueryContext qc, StringBuffer dest) {
//			dest.append(Integer.toString(this.ordinal));
//			dest.append(" ");			
//		}		
//	}


//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		dest.append("ORDER BY ");
//		getSortKeyList().generate(qc, dest);		
//	}
	

	
	@Override
	protected Element getContent() {
		return getSortKeyList();
	}		
}
