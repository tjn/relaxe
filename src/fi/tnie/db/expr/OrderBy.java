/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class OrderBy	
	extends AbstractClause {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8892530221014201090L;
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
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3707453043715435079L;
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
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1230384738747966326L;
		private Expression expression;
		
		/**
		* No-argument constructor for GWT Serialization
		*/
		@SuppressWarnings("unused")
		private ExprSortKey() {	
		}
	
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
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8039564149907068654L;
		private Ordinal ordinal;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private OrdinalSortKey() {	
		}
		
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
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1184979707913414800L;
		private int ordinal;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private Ordinal() {	
		}
		
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
		super(SQLKeyword.ORDER_BY);
	}
	
	public void add(SortKey k) {
		getSortKeyList().add(k);
	}
	
	public void add(ColumnReference c, Order o) {
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
