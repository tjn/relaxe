/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.ui.swing;

import javax.swing.JTextField;

import fi.tnie.db.ui.Annotation;
import fi.tnie.db.ui.IntEditor;

public class SwingIntEditor
    implements IntEditor {
    
    private JTextField field;
    
    public SwingIntEditor() {
        this.field = new JTextField();
    }

    @Override
    public void annotate(Annotation a) {            
    }

    @Override
    public Integer getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setValue(Integer value) {
        // TODO Auto-generated method stub        
    }
    
}