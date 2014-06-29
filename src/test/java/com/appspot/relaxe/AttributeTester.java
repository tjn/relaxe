package com.appspot.relaxe;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.value.BooleanAccessor;
import com.appspot.relaxe.ent.value.BooleanAttribute;
import com.appspot.relaxe.ent.value.HasBoolean;
import com.appspot.relaxe.ent.value.HasBooleanAttribute;
import com.appspot.relaxe.types.BooleanType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.BooleanHolder;

public class AttributeTester
	implements HasBoolean.Read<AttributeName, AttributeTester, AttributeTester>, HasBoolean.Write<AttributeName, AttributeTester, AttributeTester> {
	

	private static class Name implements AttributeName {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5654643503473362041L;

		@Override
		public String identifier() {
			return "flag";
		}

		@Override
		public ValueType<?> type() {
			return BooleanType.TYPE;
		}
	}

	private static class Meta
		implements HasBooleanAttribute<AttributeName, AttributeTester, AttributeTester> {
		

		private BooleanAttribute<AttributeName, AttributeTester, AttributeTester> key;
		
		public Meta() {
			key = BooleanAttribute.get(this, new Name());
		}

		@Override
		public BooleanAttribute<AttributeName, AttributeTester, AttributeTester> getBooleanAttribute(AttributeName a) {
			return key;
		}

		@Override
		public void register(BooleanAttribute<AttributeName, AttributeTester, AttributeTester> key) {
			this.key = key;
		}
		
	}
	
	
	// private static BooleanAttribute<AttributeName, AttributeTester, AttributeTester> K1 = new BooleanAttribute<> 

	@Override
	public AttributeTester asRead() {
		return this;
	}

	@Override
	public AttributeTester asWrite() {
		return this;
	}

	@Override
	public void setBoolean(BooleanAttribute<AttributeName, AttributeTester, AttributeTester> key, BooleanHolder newValue) {
		
		
	}

	@Override
	public BooleanHolder getBoolean(
			BooleanAttribute<AttributeName, AttributeTester, AttributeTester> key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public BooleanAccessor<AttributeName, AttributeTester, AttributeTester> value() {
		Meta meta = new Meta();
		return new BooleanAccessor<AttributeName, AttributeTester, AttributeTester>(this, meta.key);		
	}

}
