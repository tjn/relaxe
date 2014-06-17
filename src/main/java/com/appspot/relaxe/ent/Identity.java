package com.appspot.relaxe.ent;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.value.ValueHolder;

public interface Identity {
			
	ValueHolder<?, ?, ?> get(Column column);
	
}
