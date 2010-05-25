/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui;


public interface Editor<T> {    
    void setValue(T value);    
    T getValue();
    void annotate(Annotation a);
}





