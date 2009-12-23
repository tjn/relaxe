package fi.tnie.db;

import fi.tnie.db.meta.BaseTable;

public class DefaultEntityFactory<K extends Enum<K>, E extends DBEntity<K, E>> 
	implements EntityFactory<K, E> {
	
	private BaseTable table;
	private Class<K> columnNameType;	
	private Class<E> productType;
	
	public DefaultEntityFactory() {
		super();	
	}	

	public DefaultEntityFactory(BaseTable table, Class<K> columnNameType, Class<E> productType) {
		super();
		
		if (table == null) {
			throw new NullPointerException("'table' must not be null");
		}
		
		if (columnNameType == null) {
			throw new NullPointerException("'columnNameType' must not be null");
		}
		
		if (productType == null) {
			throw new NullPointerException("'productType' must not be null");
		}
				
		this.table = table;
		this.columnNameType = columnNameType;
		this.productType = productType;
	}
	
	

	@Override
	public BaseTable getTable() {		
		return this.table;
	}		

	@Override
	public EntityQuery<K, E> createQuery() {
		return new EntityQuery<K, E>(getTable(), columnNameType);
	}

	@Override
	public E newInstance() 
		throws InstantiationException, IllegalAccessException {
		return this.productType.newInstance();
	}

	public Class<K> getColumnNameType() {
		return this.columnNameType;
	}
	
}
