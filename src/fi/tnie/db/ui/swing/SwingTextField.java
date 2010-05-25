/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui.swing;

import fi.tnie.db.EntityMetaData;
import fi.tnie.db.Identifiable;
import fi.tnie.db.ui.Annotation;
import fi.tnie.db.ui.EditContext;
import fi.tnie.db.ui.UIField;
import fi.tnie.db.ui.ValueEditor;

public class SwingTextField
    <A extends Enum<A> & Identifiable>
    implements UIField<A, String> {
    
    private EntityMetaData<A, ?, ?, ?> meta;
    private A attribute;
    private EditContext context;
    private SwingTextEditor editor;
        
    public SwingTextField(EntityMetaData<A, ?, ?, ?> meta, A attribute, EditContext ectx) {
        super();
        this.meta = meta;
        this.attribute = attribute;
        this.context = ectx;
    }

    @Override
    public A getAttribute() {
        return this.attribute;
    }

    @Override
    public EditContext getContext() {
        return this.context;
    }

    @Override
    public ValueEditor<String> getEditor() {
        if (this.editor == null) {
            this.editor = new SwingTextEditor();            
        }

        return this.editor;
    }

    @Override
    public ValueEditor<String> getLabel() {
        // TODO Auto-generated method stub
        
        
        return null;
    }

    @Override
    public void setContext(EditContext ec) {
        this.context = ec;
    }

    @Override
    public void annotate(Annotation a) {
        // TODO Auto-generated method stub        
    }

}
