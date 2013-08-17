/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.mysql;

import java.util.EnumSet;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.Token;
import com.appspot.relaxe.expr.VisitContext;

public enum MySQLKeyword
    implements Token {
    
    /**
     * <code>DELETE [LOW_PRIORITY] [QUICK] [IGNORE]
        FROM tbl_name[.*] [, tbl_name[.*]] ...
        <bold>USING</bold> table_references
        [WHERE where_condition]
        </code>
     */
    USING,
    MATCH,
    // Reported by JDBC Driver
    ACCESSIBLE,
    ANALYZE,
    ASENSITIVE,
    BEFORE,
    BIGINT,
    BINARY,
    BLOB,
    CALL,
    CHANGE,
    CONDITION,
    DATABASE,
    DATABASES,
    DAY_HOUR,
    DAY_MICROSECOND,
    DAY_MINUTE,
    DAY_SECOND,
    DELAYED,
    DETERMINISTIC,
    DISTINCTROW,
    DIV,
    DUAL,
    EACH,
    ELSEIF,
    ENCLOSED,
    ESCAPED,
    EXIT,
    EXPLAIN,
    FLOAT4,
    FLOAT8,
    FORCE,
    FULLTEXT,
    GENERAL,
    HIGH_PRIORITY,
    HOUR_MICROSECOND,
    HOUR_MINUTE,
    HOUR_SECOND,
    IF,
    IGNORE,
    IGNORE_SERVER_IDS,
    INFILE,
    INOUT,
    INT1,
    INT2,
    INT3,
    INT4,
    INT8,
    ITERATE,
    KEYS,
    KILL,
    LEAVE,
    LIMIT,
    LINEAR,
    LINES,
    LOAD,
    LOCALTIME,
    LOCALTIMESTAMP,
    LOCK,
    LONG,
    LONGBLOB,
    LONGTEXT,
    LOOP,
    LOW_PRIORITY,
    MASTER_HEARTBEAT_PERIOD,
    MAXVALUE,
    MEDIUMBLOB,
    MEDIUMINT,
    MEDIUMTEXT,
    MIDDLEINT,
    MINUTE_MICROSECOND,
    MINUTE_SECOND,
    MOD,
    MODIFIES,
    NO_WRITE_TO_BINLOG,
    OPTIMIZE,
    OPTIONALLY,
    OUT,
    OUTFILE,
    PURGE,
    RANGE,
    READS,
    READ_ONLY,
    READ_WRITE,
    REGEXP,
    RELEASE,
    RENAME,
    REPEAT,
    REPLACE,
    REQUIRE,
    RESIGNAL,
    RETURN,
    RLIKE,
    SCHEMAS,
    SECOND_MICROSECOND,
    SENSITIVE,
    SEPARATOR,
    SHOW,
    SIGNAL,
    SLOW,
    SPATIAL,
    SPECIFIC,
    SQLEXCEPTION,
    SQL_BIG_RESULT,
    SQL_CALC_FOUND_ROWS,
    SQL_SMALL_RESULT,
    SSL,
    STARTING,
    STRAIGHT_JOIN,
    TERMINATED,
    TINYBLOB,
    TINYINT,
    TINYTEXT,
    TRIGGER,
    UNDO,
    UNLOCK,
    UNSIGNED,
    USE,
    UTC_DATE,
    UTC_TIME,
    UTC_TIMESTAMP,
    VARBINARY,
    VARCHARACTER,
    WHILE,
    X509,
    XOR,
    YEAR_MONTH,
    ZEROFILL
    ,
    ;
    

    private static EnumSet<MySQLKeyword> keywords = EnumSet.allOf(MySQLKeyword.class);
    
    @Override
	public void traverse(VisitContext vc, ElementVisitor v) {
        v.start(vc, this);
        v.end(this);        
    }
    
    @Override
    public String getTerminalSymbol() {
        return super.toString();
    }

    @Override
    public boolean isOrdinary() {
        return true;
    }
    
    public static boolean isKeyword(String s) {
        s = s.trim().toUpperCase();     
        return keywords.contains(s);
    }
}
