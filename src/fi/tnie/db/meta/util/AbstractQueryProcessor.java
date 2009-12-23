package fi.tnie.db.meta.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class AbstractQueryProcessor implements QueryProcessor {

	@Override
	public void abort(Throwable e) {
	}

	@Override
	public void endQuery() throws SQLException {
	}

	@Override
	public void finish() {
	}

	@Override
	public void prepare(String statement) throws SQLException {
	}

	@Override
	public void process(ResultSet rs, long ordinal) throws SQLException {
	}

	@Override
	public void startQuery(ResultSetMetaData m) throws SQLException {
	}

	@Override
	public void updated(int updateCount) throws SQLException {
	}
}
