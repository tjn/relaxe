package com.appspot.relaxe.env.mariadb;

import com.appspot.relaxe.env.mysql.MySQLAlterTableDropForeignKey;
import com.appspot.relaxe.env.mysql.MySQLAlterTableDropPrimaryKey;
import com.appspot.relaxe.env.mysql.MySQLDeleteStatement;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.DeleteStatement;
import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.expr.ddl.AlterTableDropConstraint;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.PrimaryKey;

public class MariaDBSyntax
    extends DefaultSQLSyntax {

    @Override
    public DeleteStatement newDeleteStatement(TableReference tref, Predicate p) {
        return new MySQLDeleteStatement(tref, p);
    }
            
    @Override
    public AlterTableDropConstraint newAlterTableDropForeignKey(ForeignKey fk) {
    	return new MySQLAlterTableDropForeignKey(fk);
    }
    
    @Override
    public AlterTableDropConstraint newAlterTableDropPrimaryKey(PrimaryKey pk) {
    	return new MySQLAlterTableDropPrimaryKey(pk);
    }
}