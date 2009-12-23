package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.QueryContext;

public class OrderByClause
	extends ListExpression<QueryExpression>
	implements QueryExpression {
	
	private List<ListItem> itemList; 
	
	public enum Order {
		ASC,
		DESC
	}
	
	private static class ListItem
		implements QueryExpression {
		
		private QueryExpression expression;
		private Order order;
		
		public ListItem(QueryExpression expression) {
			this(expression, null);
		}
		
		public ListItem(QueryExpression expression, Order order) {
			super();
			this.expression = expression;
			this.order = ((order == null) ? Order.ASC : order);
		}

		@Override
		public void generate(QueryContext qc, StringBuffer dest) {
			expression.generate(qc, dest);
			
			if (this.order == Order.DESC) {
				dest.append(" ");
				dest.append(this.order);
			}
			
			dest.append(" ");			
		}		
	}
	

	protected OrderByClause(String delim) {
		super(",");
	}
	
	public void add(ColumnExpr c, Order o) {
		getItemList().add(new ListItem(c, o));
	}
	
	public void add(int ord, Order o) {
		getItemList().add(new ListItem(new Ordinal(ord), o));
	}

	@Override
	protected List<? extends QueryExpression> getElementList() {
		return getItemList();
	}

	private List<ListItem> getItemList() {
		if (itemList == null) {
			itemList = new ArrayList<ListItem>();			
		}

		return itemList;
	}
	
	
	private static class Ordinal
		implements QueryExpression {
		
		private int ordinal;
		
		public Ordinal(int ordinal) {
			super();
			this.ordinal = ordinal;
		}

		@Override
		public void generate(QueryContext qc, StringBuffer dest) {
			dest.append(Integer.toString(this.ordinal));
			dest.append(" ");			
		}		
	}
}
