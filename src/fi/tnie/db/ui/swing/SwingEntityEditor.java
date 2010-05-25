/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui.swing;

import fi.tnie.db.Entity;
import fi.tnie.db.ui.Annotation;
import fi.tnie.db.ui.EntityEditor;

public class SwingEntityEditor
    implements EntityEditor {

    private Entity<?, ?, ?, ?> value;
    
    @Override
    public void annotate(Annotation a) {
        // TODO Auto-generated method stub        
    }

    @Override
    public Entity<?, ?, ?, ?> getValue() {
        return this.value;
    }

    @Override
    public void setValue(Entity<?, ?, ?, ?> value) {
        this.value = value;
    }

}
