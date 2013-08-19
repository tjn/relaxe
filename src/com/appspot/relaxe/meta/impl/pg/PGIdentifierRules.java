/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import java.util.Comparator;

import com.appspot.relaxe.expr.AbstractIdentifierRules;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.meta.Folding;
import com.appspot.relaxe.meta.FoldingComparator;
import com.appspot.relaxe.meta.IdentifierRules;


public class PGIdentifierRules
	extends AbstractIdentifierRules
	implements IdentifierRules {
	
	@Override
	public Comparator<Identifier> comparator() {		
		return FoldingComparator.LOWERCASE;
	}
	
	@Override
	public Folding getFolding() {		
		return Folding.LOWERCASE;
	}
}
