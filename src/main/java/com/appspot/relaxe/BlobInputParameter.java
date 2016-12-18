package com.appspot.relaxe;

import com.appspot.relaxe.expr.ImmutableValueParameter;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.types.BlobType;

public class BlobInputParameter
	extends ImmutableValueParameter<InputStreamKey, BlobType, InputStreamKeyHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 118955713131797773L;
	
	public BlobInputParameter(Column column, InputStreamKey key) {
		super(column, new InputStreamKeyHolder(key));
	}

}
