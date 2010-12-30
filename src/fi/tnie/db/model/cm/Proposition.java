/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;


/**
 * TODO: 
 * State model for this class is way too vague.
 * And so are relationships with ChangeSet and ConstrainedValueModel.
 *   
 * 
 * @author tnie
 */
public interface Proposition {	
	void reject();
	void commit();
	boolean isRejected();
	boolean isCommitted();
}
