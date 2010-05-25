/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui.swing;

import javax.swing.JTextField;

import fi.tnie.db.ui.Annotation;
import fi.tnie.db.ui.TextEditor;

public class SwingTextEditor
    extends TextEditor {
    
    private JTextField input;    

    @Override
    public String getValue() {
        return getInput().getText();
    }

    @Override
    public void setValue(String value) {
        getInput().setText(value);        
    }

    public JTextField getInput() {
        if (input == null) {
            input = new JTextField();            
        }

        return input;
    }

    @Override
    public void annotate(Annotation a) {
        // TODO Auto-generated method stub        
    }    
}
