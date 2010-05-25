/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui;

import fi.tnie.db.Identifiable;

public interface UIField<
    A extends Enum<A> & Identifiable,
    T
>
{
    EditContext getContext();
    void setContext(EditContext ec);
    
    A getAttribute(); 
    ValueEditor<T> getEditor();
    ValueEditor<String> getLabel();
    
    void annotate(Annotation a);
}
