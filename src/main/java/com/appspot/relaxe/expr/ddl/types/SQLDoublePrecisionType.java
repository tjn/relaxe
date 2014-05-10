package com.appspot.relaxe.expr.ddl.types;

import com.appspot.relaxe.expr.ElementVisitor;
import com.appspot.relaxe.expr.SQLKeyword;
import com.appspot.relaxe.expr.VisitContext;

public class SQLDoublePrecisionType
    extends ApproximateNumericType {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -269346141333155275L;

	private static final SQLDoublePrecisionType TYPE = new SQLDoublePrecisionType();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	private SQLDoublePrecisionType() {		
	}
			
	public static SQLDoublePrecisionType get() {
		return TYPE;
	}
            
    @Override
    public void traverseContent(VisitContext vc, ElementVisitor v) {
    	traverseName(vc, v);
    }
    
    @Override
    protected void traverseName(VisitContext vc, ElementVisitor v) {
    	SQLKeyword.DOUBLE.traverse(vc, v);
    	SQLKeyword.PRECISION.traverse(vc, v);
    }
}