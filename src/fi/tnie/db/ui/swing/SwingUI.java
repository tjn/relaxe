/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui.swing;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import fi.tnie.db.meta.Catalog;
import fi.tnie.dbmeta.tools.CatalogTool;
import fi.tnie.dbmeta.tools.ToolException;

public class SwingUI
    extends CatalogTool {
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        new SwingUI().run(args);
    }
            
    @Override
    public int run() throws ToolException {     
        int result = -1;
        
        try {
            JFrame frame = new JFrame();            
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        
            frame.setSize(500, 500);            
            // frame.pack();                        
            Container cp = frame.getContentPane();
            
            JSplitPane sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            
            sp1.setDividerLocation(-1);
            
            sp1.setTopComponent(new JLabel("banner"));
            sp1.setBottomComponent(sp2);
                        
            sp2.setLeftComponent(new CatalogOutline(getCatalog()));
            sp2.setRightComponent(new JLabel("content"));
            cp.add(sp1);
            
            frame.setVisible(true);
            
            result = 0;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return result;        
    }
}
