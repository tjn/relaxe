/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class JoinedTable
	extends AbstractTableReference {
	
	private AbstractTableReference left;
	private AbstractTableReference right;
	private JoinType joinType;
	private JoinCondition joinCondition;
				
	public JoinedTable(AbstractTableReference left, AbstractTableReference right,
			JoinType joinType, JoinCondition joinCondition) {
		super();
		this.left = left;
		this.right = right;
		this.joinType = joinType;
		this.joinCondition = joinCondition;
	}
	
	protected JoinedTable(JoinType joinType) {
		super();
		setJoinType(joinType);
	}

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		if (getLeft() == null) {
//			throw new NullPointerException("'left' must not be null");
//		}		
//		
//		if (getRight() == null) {
//			throw new NullPointerException("'right' must not be null");
//		}
//		
//		if (getJoinType() == null) {
//			throw new NullPointerException("'joinType' must not be null");
//		}		
//		if (getJoinCondition() == null) {
//			throw new NullPointerException("'joinCondition' must not be null");
//		}
//		
//		getLeft().generate(qc, dest);		
//		dest.append(" ");		
//		dest.append(getJoinType());
//		dest.append(" JOIN ");
//		getRight().generate(qc, dest);
//		dest.append(" ON (");
//		getJoinCondition().generate(qc, dest);
//		dest.append(") ");		
//	}

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

	@Override
	public ElementList<? extends ColumnName> getColumnNameList() {
		ElementList<ColumnName> names = new ElementList<ColumnName>();
		
		copyColumnNameList(getLeft(), names);
		copyColumnNameList(getRight(), names);
				
		return names;
	}
	
	private void copyColumnNameList(AbstractTableReference src, ElementList<ColumnName> dest) {
		if (src != null) {
			ElementList<? extends ColumnName> nl = src.getColumnNameList();
			
			if (!nl.isEmpty()) {
				dest.getContent().addAll(nl.getContent());
			}
		}
	}

	@Override
	public ElementList<SelectListElement> getSelectList() {
		ElementList<SelectListElement> el = new ElementList<SelectListElement>();
		
		copyElementList(getLeft(), el);
		copyElementList(getRight(), el);
				
		return el;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getLeft().traverse(vc, v);
		getJoinType().traverse(vc, v);
		getRight().traverse(vc, v);
		getJoinCondition().traverse(vc, v);	}
}
