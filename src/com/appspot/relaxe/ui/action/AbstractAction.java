/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ui.action;

import com.appspot.relaxe.model.BooleanModel;
import com.appspot.relaxe.model.ConstantBooleanModel;
import com.appspot.relaxe.model.DefaultConstantValueModel;
import com.appspot.relaxe.model.ValueModel;

public abstract class AbstractAction
	implements Action {
	
	private BooleanModel enablement;
	private ValueModel<String> displayName;
	
	private static final ValueModel<String> UNNAMED = DefaultConstantValueModel.valueOf("<unnamed action>");
		
	public AbstractAction(ValueModel<String> nameModel) {
		this(null, nameModel);
	}
	
	protected AbstractAction() {
		this(ConstantBooleanModel.TRUE, (ValueModel<String>) null);
	}
	
	public AbstractAction(String name) {
		this(null, name);		
	}
	
	public AbstractAction(BooleanModel em, String name) {
		this(em, DefaultConstantValueModel.valueOf(name));
	}
	
	public AbstractAction(BooleanModel em, ValueModel<String> nameModel) {
		init(nameModel, em);
	}
	
	
	protected final void init(ValueModel<String> nameModel, BooleanModel enablement) {
		if (nameModel == null) {
			nameModel = UNNAMED;
		}
		
		if (enablement == null) {
			enablement = ConstantBooleanModel.TRUE;
		}
						
		this.enablement = enablement;
		this.displayName = nameModel;
	}


	@Override
	public BooleanModel enablement() {
		return this.enablement;
	}
	
	@Override
	public final boolean execute() {
		boolean e = isEnabled();
		
		if (e) {
			run();			
		}
		else {
			rejected();
		}
	
		return e;
	}	
	
	protected void run() {
		
	}

	protected void rejected() {
		
	}

	@Override
	public boolean isEnabled() {
		Boolean e = enablement().get();
		return (e != null) && e.booleanValue();
	}

	
	@Override
	public ValueModel<String> displayName() {
		return this.displayName;
	}
	
	
}
