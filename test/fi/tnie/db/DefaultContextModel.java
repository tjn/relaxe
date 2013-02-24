/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.model.DefaultMutableValueModel;
import fi.tnie.db.model.ValueModel;

public abstract class DefaultContextModel
	<I extends Implementation<I>>
{
	
	private ValueModel<TestContext<I>> contextModel;
	
	// private static DefaultContextModel<I> instance;
	
	public void init(ValueModel<TestContext<I>> cm) {
		this.contextModel = cm;
	}
	
//	public static DefaultContextModel<TestContext<I>> getInstance() {
//		if (instance == null) {
//			instance = new DefaultContextModel<TestContext<I>>();			
//		}
//
//		return instance;
//	}
	
	public TestContext<I> getContext() {
		if (this.contextModel == null) {
			TestContext<I> tc = createDefaultContext();
			this.contextModel = new DefaultMutableValueModel<TestContext<I>>(tc);
		}
		
		return this.contextModel.get();
	}

	protected abstract TestContext<I> createDefaultContext();	
}
