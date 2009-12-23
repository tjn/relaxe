package fi.tnie.db.meta.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public interface QueryProcessor {

	public void prepare(String statement)  throws SQLException;
	public void finish();
	public void updated(int updateCount)  throws SQLException;
	public void process(ResultSet rs, long ordinal)  throws SQLException;
	public void abort(Throwable e);
	public void endQuery()  throws SQLException;
	public void startQuery(ResultSetMetaData m) throws SQLException;
}
