package com.appspot.relaxe.service;

public interface ClosableDataAccessSession
	extends DataAccessSession, TransactionControlSession, ClosableSession, BinaryAttributeReader, BinaryAttributeWriter {

}
