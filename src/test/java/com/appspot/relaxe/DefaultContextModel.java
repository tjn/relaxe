/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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

import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.model.DefaultMutableValueModel;
import com.appspot.relaxe.model.ValueModel;

public abstract class DefaultContextModel
	<I extends Implementation<I>>
{
	
	private ValueModel<TestContext<I>> contextModel;
	
	// private static DefaultContextModel<I> instance;
	
	public void init(ValueModel<TestContext<I>> cm) {
		this.contextModel = cm;
	}
	
//	public static DefaultContextModel<TestContext<I>> getInstance() {
//		if (instance == null) {
//			instance = new DefaultContextModel<TestContext<I>>();			
//		}
//
//		return instance;
//	}
	
	public TestContext<I> getContext() {
		if (this.contextModel == null) {
			TestContext<I> tc = createDefaultContext();
			this.contextModel = new DefaultMutableValueModel<TestContext<I>>(tc);
		}
		
		return this.contextModel.get();
	}

	protected abstract TestContext<I> createDefaultContext();	
}
