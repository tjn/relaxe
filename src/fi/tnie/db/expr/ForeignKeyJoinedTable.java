package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.QueryContext;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.ForeignKey;

public class ForeignKeyJoinedTable 
	extends JoinedTable {
	
	private ForeignKey foreignKey;
			
	public ForeignKeyJoinedTable(BaseTableReference left, BaseTableReference right,
			JoinType joinType, JoinCondition joinCondition) {
		super(left, right, joinType, joinCondition);
	}

//	@Override
//	public void generate(QueryContext qc, StringBuffer dest) {
//		if (getLeft() == null) {
//			throw new NullPointerException("'left' must not be null");
//		}		
//		
//		if (getRight() == null) {
//			throw new NullPointerException("'right' must not be null");
//		}
//		
//		if (joinType == null) {
//			throw new NullPointerException("'joinType' must not be null");
//		}		
//		if (joinCondition == null) {
//			throw new NullPointerException("'joinCondition' must not be null");
//		}
//		
//		left.generate(qc, dest);		
//		dest.append(" ");		
//		dest.append(joinType);
//		dest.append(" JOIN ");
//		right.generate(qc, dest);
//		dest.append(" ON (");
//		joinCondition.generate(qc, dest);
//		dest.append(") ");		
//	}

	@Override
	public SelectList<QueryExpression> getSelectList() {
		List<QueryExpression> el = new ArrayList<QueryExpression>();		
		add(el, getLeft().getSelectList());
		add(el, getRight().getSelectList());			
		return new SelectList<QueryExpression>(el);
	}
	
	private void add(List<QueryExpression> list, QueryExpression item) {
		if (item != null) {
			list.add(item);
		}
	}
	
	public ForeignKey getForeignKey() {
		return foreignKey;
	}
	
	public ForeignKeyJoinedTable(QueryContext qc, ForeignKey foreignKey) {
		this(qc, foreignKey, JoinType.INNER, false);
	}
	
	private ForeignKeyJoinedTable(QueryContext qc, ForeignKey foreignKey, JoinType joinType) {
		super(qc, joinType);
		
		if (foreignKey == null) {
			throw new NullPointerException("'foreignKey' must not be null");
		}
		
		setForeignKey(foreignKey);
	}	
	
	public ForeignKeyJoinedTable(BaseTableReference lr, String fkname, JoinType joinType) {
		super(lr.getContext(), joinType);
		
		if (fkname == null) {
			throw new NullPointerException("'foreignKeyName' must not be null");
		}
		
		BaseTable left = lr.getBaseTable();
		BaseTable right = null;
		
		ForeignKey fk = left.foreignKeys().get(fkname);
		boolean invert = (fk == null);
				
		if (fk != null) {
			right = fk.getReferenced();
		}
		else {
			fk = left.references().get(fkname);
			
			if (fk == null) {
				throw new IllegalArgumentException();
			}
			
			right = fk.getReferencing();			
		}
						
		BaseTableReference rr = new BaseTableReference(getContext(), right);
				
		setForeignKey(fk);
		setLeft(lr);
		setRight(rr);
		
		BaseTableReference referencing = invert ? rr : lr;
		BaseTableReference referenced  = invert ? lr : rr;
		
		setJoinCondition(
				new ForeignKeyJoinCondition(fk, referencing, referenced)
		);
	}
	
	public ForeignKeyJoinedTable(QueryContext qc, ForeignKey foreignKey, JoinType joinType, boolean invert) {
		this(qc, foreignKey, joinType);				
		
		BaseTableReference referencing = new BaseTableReference(qc, foreignKey.getReferencing()); 
		BaseTableReference referenced = new BaseTableReference(qc, foreignKey.getReferenced());
		
		BaseTableReference a = referencing;
		BaseTableReference b = referenced;		
						
		setLeft(invert ? b : a);		
		setRight(invert ? a : b);		
				
		setJoinCondition(new ForeignKeyJoinCondition(foreignKey, referencing, referenced));
	}
	
	private void setForeignKey(ForeignKey foreignKey) {
		this.foreignKey = foreignKey;
	}
	
	public BaseTableReference getLeftBaseTable() {
		return (BaseTableReference) getLeft();
	}
	
	public BaseTableReference getRightBaseTable() {
		return (BaseTableReference) getRight();
	}

	public ForeignKeyJoinedTable innerJoin(String fkname) {
				
//		return new ForeignKeyJoinedTable(this.getLeftBaseTable(), fkname, JoinType.INNER);
		return null;
	}


	
	
}
