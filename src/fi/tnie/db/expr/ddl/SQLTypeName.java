/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr.ddl;

import java.sql.Types;

import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Keyword;
import fi.tnie.db.expr.VisitContext;

enum SQLTypeName
    implements Element {
        
    CHAR(Types.CHAR, Keyword.CHARACTER),
    VARCHAR(Types.VARCHAR, Keyword.VARCHAR),
    CLOB(Types.CLOB, Keyword.CLOB),
    BIGINT(Types.BIGINT, Keyword.BIGINT),
    BIT(Types.BIT, Keyword.BIT),    
    // BIT_VARYING(Types.BITV, Keyword.BIT, Keyword.VARYING),
    BLOB(Types.BLOB, Keyword.BLOB),
    NUMERIC(Types.NUMERIC, Keyword.NUMERIC), 
    DECIMAL(Types.DECIMAL, Keyword.DECIMAL),
    INTEGER(Types.INTEGER, Keyword.INTEGER),
    // INT(Types.INTEGER, Keyword.INT),
    SMALLINT(Types.SMALLINT, Keyword.SMALLINT),
    TINYINT(Types.TINYINT, Keyword.TINYINT),
    FLOAT(Types.FLOAT, Keyword.FLOAT),
    DATE(Types.DATE, Keyword.DATE),
    TIME(Types.TIME, Keyword.TIME),
    TIMESTAMP(Types.TIMESTAMP, Keyword.TIMESTAMP),
    ;
                
    private SQLTypeName(int type, Keyword... kws) {
        this.type = type;
        this.keywords = kws;
    }
    
    private int type; 
    
    private Keyword[] keywords;
    
    @Override
    public String getTerminalSymbol() {     
        return null;
    }

    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        for (Keyword kw : keywords) {
            kw.traverse(vc, v);
        }
    }
    
    public int getType() {
        return this.type;
    }
}