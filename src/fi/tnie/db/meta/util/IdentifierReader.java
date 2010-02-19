/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.Environment;

public class IdentifierReader extends AbstractQueryProcessor {
		
	private Collection<Identifier> destination;
	private int column;
	private Environment env;
	
	public IdentifierReader(Collection<Identifier> dest, Environment env) {
		this(dest, env, 1);		
	}
	
	public IdentifierReader(Collection<Identifier> dest, Environment env, int column) {
		super();
				
		if (dest == null) {
			throw new NullPointerException("'dest' must not be null");
		}
		
		if (env == null) {
			throw new NullPointerException("'env' must not be null");
		}
						
		this.destination = dest;
		this.env = env;
		this.column = column;
	}

	@Override
	public void process(ResultSet rs, long ordinal) 
		throws SQLException, IllegalIdentifierException {
		String value = rs.getString(column);		
		Identifier ident = env.createIdentifier(value);
		this.destination.add(ident);
	}
}
