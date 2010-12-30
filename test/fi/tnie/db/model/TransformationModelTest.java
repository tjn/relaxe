/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import fi.tnie.db.model.cm.AbstractTransformationModel;
import junit.framework.TestCase;

public class TransformationModelTest extends TestCase {

	public void testComputedModel() {		
		final MutableStringModel source = new MutableStringModel(null); 
		 
		final AbstractTransformationModel<Integer, String> cm =
			new AbstractTransformationModel<Integer, String>(source) {

				@Override
				public Integer transform(String source) {									
					return (source == null) ? null : Integer.valueOf(source.length());
				}
		};
		
		ChangeCounter<Integer> cc = new ChangeCounter<Integer>();				
		cm.addChangeHandler(cc);
		
		assertNull(cm.get());
		
		String input;		
	
		input = "";
		source.set(input);
		assertNotNull(cm.get());		
		assertEquals(input.length(), cm.get().intValue());
		assertEquals(1, cc.getCount());

		input = "abc"; 
		source.set(input);
		assertNotNull(cm.get());
		assertEquals(input.length(), cm.get().intValue());
		assertEquals(2, cc.getCount());

		input = "bac"; 
		source.set(input);
		assertNotNull(cm.get());
		assertEquals(input.length(), cm.get().intValue());
		// length did not changed
		assertEquals(2, cc.getCount());
	}	
}
