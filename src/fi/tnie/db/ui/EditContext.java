/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.ui;

import fi.tnie.db.EntityMetaData;
import fi.tnie.db.Identifiable;

public interface EditContext {
    Editor<?> newEditor(int sqltype);
    EntityEditor newEntityEditor(EntityMetaData<?, ?, ?, ?> meta);
    
    <A extends Enum<A> & Identifiable>
    String getLabel(EntityMetaData<A, ?, ?, ?> meta, A attribute);
}