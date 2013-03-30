/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.meta.impl.pg.PGTest;

public class PGBlobTest
	extends PGTest
{	
	public void testBlob() throws Exception {
		dumpMetaData("SELECT staff_id, picture FROM public.staff");				
	}
}
