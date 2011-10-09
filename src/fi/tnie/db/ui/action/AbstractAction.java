/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui.action;

import fi.tnie.db.model.BooleanModel;
import fi.tnie.db.model.ConstantBooleanModel;
import fi.tnie.db.model.DefaultConstantValueModel;
import fi.tnie.db.model.ValueModel;

public abstract class AbstractAction
	implements Action {
	
	private BooleanModel enablement;
	private ValueModel<String> displayName;
	
	private static final ValueModel<String> UNNAMED = DefaultConstantValueModel.valueOf("<unnamed action>");
		
	public AbstractAction(ValueModel<String> nameModel) {
		this(null, nameModel);
	}
	
	protected AbstractAction() {		
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
