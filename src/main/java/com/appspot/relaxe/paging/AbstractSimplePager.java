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
package com.appspot.relaxe.paging;

import java.io.Serializable;

import com.appspot.relaxe.model.ImmutableValueModel;


public abstract class AbstractSimplePager<
	RP extends Serializable, 
	P extends PagerModel<RP, P, SimplePagerModel.Command>
>
	extends AbstractPagerModel<RP, P,  SimplePagerModel.Command> 
	implements SimplePagerModel<RP, P> {

	
	/* 
	 * Returns the offset of the first the of the current page.
	 */
	@Override
	public abstract ImmutableValueModel<Long> currentPageOffset();
	
	/*
	 * Returns the offset of the first the of the current page.
	 */
	@Override
	public abstract ImmutableValueModel<Long> available();	

}
