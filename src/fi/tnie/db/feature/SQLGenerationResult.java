/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.feature;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.Statement;

public class SQLGenerationResult {

    private ElementList<Statement> statementList;    
    
    public void add(Statement s) {
        getStatementList().add(s);        
    }
        
    public List<Statement> changes() {
        return Collections.unmodifiableList(getStatementList().getContent());
    }

    private ElementList<Statement> getStatementList() {
        if (statementList == null) {
            statementList = new ElementList<Statement>();            
        }

        return statementList;
    }    
    
    public List<Statement> statements() {
        if (this.statementList == null) {
            return Collections.emptyList();
        }
        
        return Collections.unmodifiableList(statementList.getContent());        
    }
    
}
