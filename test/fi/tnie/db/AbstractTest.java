/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import org.apache.log4j.Logger;

import junit.framework.TestCase;

public abstract class AbstractTest
	extends TestCase {
	
	private TestContext context;
	
	private Logger logger; 
	
	private void setTestContext(TestContext context) {
		this.context = context;
	}
	
	@Override
	protected final void setUp() throws Exception {
		super.setUp();		
		setTestContext(DefaultContextModel.getInstance().getContext());		
		initFixture();
		start(getName());
	}
	
	public void start(String name) {
		
	}

	@Override
	protected void tearDown() throws Exception {
		
		super.tearDown();
		closeFixture();
	}

	protected void closeFixture() {
		
	}

	protected void initFixture() {		
	}		
	
	public TestContext getContext() {
		return context;
	}
	
	protected Logger logger() {
		if (logger == null) {
			logger = Logger.getLogger(getClass());			
		}

		return logger;		
	}
}
