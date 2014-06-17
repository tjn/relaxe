package com.appspot.relaxe.ent;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.value.ValueHolder;

public abstract class ImmutableColumnMap
	implements Map<Column, ValueHolder<?, ?, ?>> {
			

	public static Map<Column, ValueHolder<?, ?, ?>> of(Comparator<Identifier> cmp, Column key, ValueHolder<?, ?, ?> value) {
		if (cmp == null) {
			throw new NullPointerException("cmp");
		}
		
		if (key == null) {
			return null;
		}
		
		if (value == null || value.isNull()) {
			return null;
		}
				
		return null;		
	}	
	

	public static class Map1
		implements Map<Column, ValueHolder<?, ?, ?>> {
		
		private Column key;
		private ValueHolder<?, ?, ?> value;
		private Comparator<Identifier> nameComparator;
		
		@Override
		public int size() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean containsKey(Object key) {
			return (value(key) != null);
		}

		private ValueHolder<?, ?, ?> value(Object key) {
			if (key == this.key) {
				return this.value;
			}
			
			if (key == null || (!(key instanceof Column))) {
				return null;
			}
			
			Column c = (Column) key;
			
			Identifier cn = c.getColumnName();
			
			if (this.nameComparator.compare(this.key.getColumnName(), cn) != 0) {
				return null;
			}
			
			
			
			DataType t1 = this.key.getDataType();
			DataType t2 = c.getDataType();
			
			
			
			
			return this.value;
		}

		@Override
		public boolean containsValue(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public ValueHolder<?, ?, ?> get(Object key) {
			return null;
		}

		@Override
		public ValueHolder<?, ?, ?> put(Column key, ValueHolder<?, ?, ?> value) {
			throw new UnsupportedOperationException(); 
		}

		@Override
		public ValueHolder<?, ?, ?> remove(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void putAll(Map<? extends Column, ? extends ValueHolder<?, ?, ?>> m) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			throw new UnsupportedOperationException();			
		}

		@Override
		public Set<Column> keySet() {
			return null;
		}

		@Override
		public Collection<ValueHolder<?, ?, ?>> values() {
			return null;
		}

		@Override
		public Set<java.util.Map.Entry<Column, ValueHolder<?, ?, ?>>> entrySet() {
			// TODO Auto-generated method stub
			return null;
		}		
	}
	
	
	
	
}
