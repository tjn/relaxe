package fi.tnie.db;

import fi.tnie.db.expr.QueryExpression;

public interface EntityPredicate<K extends Enum<K>, E extends DBEntity<K, E>> 
	extends QueryExpression {

}
