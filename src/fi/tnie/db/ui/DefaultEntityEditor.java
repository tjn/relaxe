/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.ui;

import fi.tnie.db.Entity;
import fi.tnie.db.EntityMetaData;

public class DefaultEntityEditor
    implements EntityEditor {
    

    private Entity<?, ?, ?, ?> value;
    
    public DefaultEntityEditor(EntityMetaData<?, ?, ?, ?> meta) {
        super();        
    }
    
    @Override
    public void annotate(Annotation a) {           
    }

    @Override
    public Entity<?, ?, ?, ?> getValue() {    
        return value;
    }

    @Override
    public void setValue(Entity<?, ?, ?, ?> value) {
        this.value = value;
    }    
}