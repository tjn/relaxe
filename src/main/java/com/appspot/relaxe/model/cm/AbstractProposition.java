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
/**
 * 
 */
package com.appspot.relaxe.model.cm;

public abstract class AbstractProposition	
	implements Proposition {
	
	private int rejectCount;
	private boolean committed;
				
	public AbstractProposition() {
		super();		
//		System.err.println(this);		
	}

	@Override
	public void reject() {		
		// new Exception("").printStackTrace(System.err);		
		this.rejectCount++;
		
		Proposition ip = impliedBy();
		
		if (ip != null) {
			ip.reject();
		}		
	}
	
	@Override
	public boolean isRejected() {
		return this.rejectCount > 0;
	}
		
	@Override
	public boolean isCommitted() {
		return committed;
	}
	
	@Override
	public void commit()
		throws IllegalStateException {
		
		if (isRejected()) {
			throw new IllegalStateException("can not commit rejected proposition");
		}
		
		apply();
		this.committed = true;
	}
	
	public boolean isCompleted() {
		return isRejected() || isCommitted();
	}

	protected abstract void apply();
}