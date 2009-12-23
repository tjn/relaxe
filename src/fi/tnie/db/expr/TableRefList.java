package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;

public class TableRefList 
	extends ListExpression<AbstractTableReference>
	implements QueryExpression
{
	private List<AbstractTableReference> tableList;
	
	public TableRefList() {
		super(" ");
	}
	
	public TableRefList(AbstractTableReference tref) {
		this();
		getTableList().add(tref);
	}
	
			
	@Override
	protected List<AbstractTableReference> getElementList() {		
		return getTableList();
	}

	private List<AbstractTableReference> getTableList() {
		if (tableList == null) {
			tableList = new ArrayList<AbstractTableReference>();			
		}

		return tableList;
	}
}
