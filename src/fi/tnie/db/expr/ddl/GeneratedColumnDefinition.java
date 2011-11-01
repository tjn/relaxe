/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.CompoundElement;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.IntLiteral;
import fi.tnie.db.expr.LongLiteral;
import fi.tnie.db.expr.SQLKeyword;
import fi.tnie.db.expr.Symbol;
import fi.tnie.db.expr.Token;
import fi.tnie.db.expr.VisitContext;

public class GeneratedColumnDefinition
	extends CompoundElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391609797552285230L;
	
	
	private boolean always = false;
	private Token start;
	private Token increment;
	
	public GeneratedColumnDefinition(boolean always) {
		this(false, (IntLiteral) null, (IntLiteral) null);
	}

	public GeneratedColumnDefinition() {
		this(false);
	}
	
	public GeneratedColumnDefinition(boolean always, int start, int increment) {
		this(always, new IntLiteral(start), new IntLiteral(increment));
	}

	
	public GeneratedColumnDefinition(boolean always, IntLiteral start, IntLiteral increment) {
		super();
		this.always = always;
		this.start = start;
		this.increment = increment;
	}
	
	public GeneratedColumnDefinition(boolean always, LongLiteral start, LongLiteral increment) {
		super();
		this.always = always;
		this.start = start;
		this.increment = increment;
	}


	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
	
//		GENERATED { ALWAYS | BY DEFAULT } AS IDENTITY 
//		[ ( START WITH IntegerConstant 
//		[ ,INCREMENT BY IntegerConstant] ) ]  ]  ]		
//		// TODO Auto-generated method stub
		
		SQLKeyword.GENERATED.traverse(vc, v);
	
		if (this.always) {
			SQLKeyword.ALWAYS.traverse(vc, v);	
		}
		else {
			SQLKeyword.BY.traverse(vc, v);
			SQLKeyword.DEFAULT.traverse(vc, v);
		}
		
		SQLKeyword.AS.traverse(vc, v);
		SQLKeyword.IDENTITY.traverse(vc, v);
		
		if (start != null) {
			Symbol.PAREN_LEFT.traverse(vc, v);
			
			SQLKeyword.START.traverse(vc, v);
			SQLKeyword.WITH.traverse(vc, v);
			start.traverse(vc, v);
			
			if (this.increment != null) {
				SQLKeyword.INCREMENT.traverse(vc, v);
				SQLKeyword.BY.traverse(vc, v);
				this.increment.traverse(vc, v);
			}
		
			Symbol.PAREN_RIGHT.traverse(vc, v);
		}		
	}
	
}
