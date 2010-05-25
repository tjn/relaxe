/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ui.swing;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fi.tnie.db.Identifiable;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Schema;

public class CatalogOutline
    extends JTree {
    
    private Catalog catalog;
    
    private DefaultMutableTreeNode root;

    public CatalogOutline(Catalog catalog) {
        super();
        setModel(new DefaultTreeModel(getRoot()));
        setCatalog(catalog);
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        if (catalog == this.catalog) {
            return;
        }
        
        DefaultMutableTreeNode n = getRoot();
        n.setUserObject(catalog.getName());
                
        n.removeAllChildren();        
        this.catalog = catalog;
        
        for (Schema s : catalog.schemas().values()) {
            DefaultMutableTreeNode sn = node(s.getUnqualifiedName());
            n.add(sn);
            
            DefaultMutableTreeNode btn = node("Base tables");            
            sn.add(btn);
        
            for (BaseTable t : s.baseTables().values()) {
                DefaultMutableTreeNode tn = node(t.getUnqualifiedName());
                btn.add(tn);
                
                DefaultMutableTreeNode cols = node("Columns");
                tn.add(cols);
                                
                for (Column c : t.columnMap().values()) {
                    DefaultMutableTreeNode cn = node(c.getUnqualifiedName());
                    cols.add(cn);                                        
                }
                
                DefaultMutableTreeNode pn = node("Primary Keys");
                tn.add(pn);
                
                DefaultMutableTreeNode fn = node("Foreign Keys");
                tn.add(fn);
                
                
                
                
            }
        }
    }

    public DefaultMutableTreeNode getRoot() {
        if (root == null) {
            root = new DefaultMutableTreeNode();            
        }

        return root;
    }
    
    private DefaultMutableTreeNode node(Identifier obj) {
        String n = obj.getName();
        return new DefaultMutableTreeNode(n == null ? "<unnamed>" : n);    
    }
    
    private DefaultMutableTreeNode node(String title) {        
        return new DefaultMutableTreeNode(title);    
    }
    
}
