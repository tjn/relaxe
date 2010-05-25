/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.ui;

import fi.tnie.db.EntityMetaData;


public abstract class DefaultEditContext
    implements EditContext {

    @Override
    public EntityEditor newEntityEditor(EntityMetaData<?, ?, ?, ?> meta) {
        return new DefaultEntityEditor(meta);
    }
}