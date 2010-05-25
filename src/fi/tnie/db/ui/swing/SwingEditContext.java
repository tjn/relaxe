/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.ui.swing;

import java.sql.Types;

import fi.tnie.db.EntityMetaData;
import fi.tnie.db.expr.ddl.SQLType;
import fi.tnie.db.ui.DefaultEntityEditor;
import fi.tnie.db.ui.EditContext;
import fi.tnie.db.ui.Editor;
import fi.tnie.db.ui.EntityEditor;
import fi.tnie.db.ui.swing.SwingIntEditor;

public abstract class SwingEditContext
    implements EditContext {
    
    public Editor<?> newEditor(int sqltype) {
        Editor<?> e = null;
        
        if (SQLType.isTextType(sqltype)) {
            e = new SwingTextEditor();            
        }
        else {
            switch (sqltype) {
            case Types.INTEGER:                            
                e = new SwingIntEditor();
                break;
            default:
                   e = new SwingTextEditor();
            }            
        }
     
        return e;
    }

    @Override
    public EntityEditor newEntityEditor(EntityMetaData<?, ?, ?, ?> meta) {
        return new DefaultEntityEditor(meta);
    }
}