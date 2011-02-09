/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

public class Decimal
	implements Serializable {
	
	private int signum;
	private int scale;
	private byte[] unscaled;
		
	public int signum() {
		return this.signum;
	}

	public int getScale() {
		return scale;
	}
}
