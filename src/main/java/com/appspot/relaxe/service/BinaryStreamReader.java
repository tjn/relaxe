package com.appspot.relaxe.service;

import java.io.IOException;
import java.io.OutputStream;

import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.query.QueryException;

public interface BinaryStreamReader {


	long read(SelectStatement ss, int column, OutputStream out) throws QueryException, IOException;
}
