/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.log;

import java.io.Serializable;

public enum Level
	implements Serializable {
	TRACE,
	DEBUG,
	INFO,
	WARN,
	ERROR,
	FATAL,
	OFF
}
