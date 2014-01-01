/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.SQLException;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.paging.DataObjectFetcher;
import com.appspot.relaxe.paging.PageReceiver;
import com.appspot.relaxe.query.QueryException;


public class SynchronousDataObjectFetcher implements DataObjectFetcher {
	
	private QueryExecutor executor;
	private Connection connection;
	
	public SynchronousDataObjectFetcher(QueryExecutor executor, Connection connection) {
		super();
		this.executor = executor;
		this.connection = connection;
	}
	
	@Override
	public void fetch(QueryExpression qe, FetchOptions opts, PageReceiver<DataObjectQueryResult<DataObject>> receiver, PageReceiver<Throwable> errorReceiver) {
		try {
			DataObjectQueryResult<DataObject> qr = executor.execute(qe, opts, this.connection);
			receiver.receive(qr);			
		}
		catch (SQLException e) {
			errorReceiver.receive(e);			 
		}
		catch (QueryException e) {
			errorReceiver.receive(e);
		}
	}
}
