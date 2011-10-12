/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.model.DefaultMutableValueModel;
import fi.tnie.db.model.ValueModel;

public class DefaultContextModel {
	
	private ValueModel<TestContext> contextModel;
	
	private static DefaultContextModel instance;
	
	public void init(ValueModel<TestContext> cm) {
		this.contextModel = cm;
	}
	
	public static DefaultContextModel getInstance() {
		if (instance == null) {
			instance = new DefaultContextModel();			
		}

		return instance;
	}
	
	public TestContext getContext() {
		if (this.contextModel == null) {
			TestContext tc = createDefaultContext();
			this.contextModel = new DefaultMutableValueModel<TestContext>(tc);
		}
		
		return this.contextModel.get();
	}

	private TestContext createDefaultContext() {
		return new SimpleTestContext(new PGImplementation());		
	}
	
}
