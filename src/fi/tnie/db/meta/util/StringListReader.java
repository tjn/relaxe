package fi.tnie.db.meta.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class StringListReader extends AbstractQueryProcessor {
		
	private Collection<String> destination;
	private int column;
	
	public StringListReader(Collection<String> dest) {
		this(dest, 1);
	}
	
	public StringListReader(Collection<String> dest, int column) {
		super();
		
		if (dest == null) {
			throw new NullPointerException("'dest' must not be null");
		}
		
		this.destination = dest;
		this.column = column;
	}

	@Override
	public void process(ResultSet rs, long ordinal) throws SQLException {
		String value = rs.getString(column);
		this.destination.add(value);
	}
}
