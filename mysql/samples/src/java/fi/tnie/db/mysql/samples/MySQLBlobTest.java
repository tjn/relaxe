/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.mysql.samples;

public class MySQLBlobTest
	extends MySQLSamplesTriggerTest {

	public void testBlob() throws Exception {
		// TODO: create table with blob in samples -database
		dumpMetaData("SELECT staff_id, picture FROM sakila.staff");				
	}
	
}
