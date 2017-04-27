package com.appspot.relaxe;

import com.appspot.relaxe.types.BlobType;
import com.appspot.relaxe.types.ValueType;
import com.appspot.relaxe.value.AbstractValueHolder;

public class InputStreamKeyHolder
	extends AbstractValueHolder<InputStreamKey, BlobType, InputStreamKeyHolder>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5550087454590122664L;
	
	private InputStreamKey value;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private InputStreamKeyHolder() {
	}

	public InputStreamKeyHolder(InputStreamKey value) {
		super();
		this.value = value;
	}

	@Override
	public InputStreamKeyHolder self() {
		return this;
	}
	
	@Override
	public int getSqlType() {
		return ValueType.BLOB;
	}

	@Override
	public InputStreamKey value() {
		return this.value;	
	}

	@Override
	public BlobType getType() {
		return BlobType.TYPE;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}
	
	@Override
	public InputStreamKeyHolder asInputStreamKeyHolder() {
		return this;
	}

}
