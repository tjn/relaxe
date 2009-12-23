package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import fi.tnie.db.QueryContext;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class JoinedTable 
	extends AbstractTableReference {
	
	private AbstractTableReference left;
	private AbstractTableReference right;
	private JoinType joinType;
	private JoinCondition joinCondition;
				
	public JoinedTable(AbstractTableReference left, AbstractTableReference right,
			JoinType joinType, JoinCondition joinCondition) {
		super(left.getContext(), null);
		this.left = left;
		this.right = right;
		this.joinType = joinType;
		this.joinCondition = joinCondition;
	}
	
	protected JoinedTable(QueryContext qc, JoinType joinType) {
		super(qc, null);
		setJoinType(joinType);
	}

	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		if (getLeft() == null) {
			throw new NullPointerException("'left' must not be null");
		}		
		
		if (getRight() == null) {
			throw new NullPointerException("'right' must not be null");
		}
		
		if (getJoinType() == null) {
			throw new NullPointerException("'joinType' must not be null");
		}		
		if (getJoinCondition() == null) {
			throw new NullPointerException("'joinCondition' must not be null");
		}
		
		getLeft().generate(qc, dest);		
		dest.append(" ");		
		dest.append(getJoinType());
		dest.append(" JOIN ");
		getRight().generate(qc, dest);
		dest.append(" ON (");
		getJoinCondition().generate(qc, dest);
		dest.append(") ");		
	}

	@Override
	public SelectList<QueryExpression> getSelectList() {
		List<QueryExpression> el = new ArrayList<QueryExpression>();		
		add(el, left.getSelectList());
		add(el, right.getSelectList());			
		return new SelectList<QueryExpression>(el);
	}
	
	private void add(List<QueryExpression> list, QueryExpression item) {
		if (item != null) {
			list.add(item);
		}
	}
	
	protected AbstractTableReference getLeft() {
		return this.left;
	}
	
	protected AbstractTableReference getRight() {
		return this.right;
	}

	protected JoinType getJoinType() {
		return joinType;
	}

	protected JoinCondition getJoinCondition() {
		return joinCondition;
	}

	protected void setLeft(AbstractTableReference left) {
		this.left = left;
	}

	protected void setRight(AbstractTableReference right) {
		this.right = right;
	}

	protected void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	protected void setJoinCondition(JoinCondition joinCondition) {
		this.joinCondition = joinCondition;
	}
}
