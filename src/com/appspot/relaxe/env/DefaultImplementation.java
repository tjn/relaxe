/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import com.appspot.relaxe.DefaultValueAssignerFactory;
import com.appspot.relaxe.DefaultValueExtractorFactory;
import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;

public abstract class DefaultImplementation<I extends Implementation<I>>	
	implements Implementation<I> {

	private ValueExtractorFactory valueExtractorFactory; 
	private ValueAssignerFactory valueAssignerFactory;	
//	private Driver driver;
	
//	private AttributeWriterFactory attributeWriterFactory;
	
//	private static Logger logger = Logger.getLogger(DefaultImplementation.class);
	
	@Override
	public abstract CatalogFactory catalogFactory();

	@Override
	public ValueExtractorFactory getValueExtractorFactory() {
		if (valueExtractorFactory == null) {			
			valueExtractorFactory = createValueExtractorFactory();			
		}

		return valueExtractorFactory;
	}
	
//	@Override
//	public AttributeWriterFactory getAttributeWriterFactory() {
//		if (attributeWriterFactory == null) {
//			attributeWriterFactory = createAttributeWriterFactory();
//			
//		}
//
//		return attributeWriterFactory;
//	}
//	
//	protected AttributeWriterFactory createAttributeWriterFactory() {
//		return new DefaultAttributeWriterFactory();
//	}

	protected ValueExtractorFactory createValueExtractorFactory() {
		return new DefaultValueExtractorFactory();
	}
	
	@Override
	public ValueAssignerFactory getValueAssignerFactory() {
		if (valueAssignerFactory == null) {
			this.valueAssignerFactory = createValueAssignerFactory();			
		}

		return valueAssignerFactory;
	}

	protected ValueAssignerFactory createValueAssignerFactory() {
		return new DefaultValueAssignerFactory();
	}
	
//	@Override
//	public Driver getDriver() {
//		if (driver == null) {
//			try {
//				this.driver = createDriver();
//			}
//			catch (Exception e) {
//				logger().error(e.getMessage(), e);
//			}
//		}
//		
//		return this.driver;
//	}
	
//	private static Logger logger() {
//		return DefaultImplementation.logger;
//	}
		
//	protected Driver createDriver() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//		Class<?> c = Class.forName(driverClassName());
//		Driver d = (Driver) c.newInstance();
//		return d;
//	}
	
}
