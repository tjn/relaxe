/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.mysql;

public class MySQLBlobTest
	extends MySQLTestCase {

	public void testBlob() throws Exception {
		dumpMetaData("SELECT staff_id, picture FROM sakila.staff");				
	}
	
}
