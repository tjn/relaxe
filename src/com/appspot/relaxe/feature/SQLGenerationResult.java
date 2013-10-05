/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.expr.Statement;


public class SQLGenerationResult {

    private List<Statement> statementList;    
    
    public void add(Statement s) {
        getStatementList().add(s);        
    }
        
    public List<Statement> changes() {
        return Collections.unmodifiableList(getStatementList());
    }

    private List<Statement> getStatementList() {
        if (statementList == null) {
            statementList = new ArrayList<Statement>();            
        }

        return statementList;
    }    
    
    public List<Statement> statements() {
        if (this.statementList == null) {
            return Collections.emptyList();
        }
        
        return Collections.unmodifiableList(statementList);        
    }
    
}
