/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
public @interface Transliterarable {
	
}
