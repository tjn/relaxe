/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.IdentifierRules;

public class IdentifierReader extends AbstractQueryProcessor {
		
	private Collection<Identifier> destination;
	private int column;
	private IdentifierRules identifierRules;
	
	public IdentifierReader(Collection<Identifier> dest, IdentifierRules rules) {
		this(dest, rules, 1);		
	}
	
	public IdentifierReader(Collection<Identifier> dest, IdentifierRules rules, int column) {
		super();
				
		if (dest == null) {
			throw new NullPointerException("'dest' must not be null");
		}
		
		if (rules == null) {
			throw new NullPointerException("'env' must not be null");
		}
						
		this.destination = dest;
		this.identifierRules = rules;
		this.column = column;
	}

	@Override
	public void process(ResultSet rs, long ordinal) 
		throws SQLException, IllegalIdentifierException {
		String value = rs.getString(column);				
		Identifier ident = identifierRules.toIdentifier(value);
		this.destination.add(ident);
	}
}
