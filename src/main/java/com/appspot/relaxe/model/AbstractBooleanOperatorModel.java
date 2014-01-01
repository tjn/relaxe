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
package com.appspot.relaxe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractBooleanOperatorModel
	extends AbstractBooleanModel
	implements BooleanModel {

	private Boolean current;
	private ArrayList<BooleanModel> inputList;
	private transient List<BooleanModel> inputListView;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractBooleanOperatorModel() {
	}
		 	
	public AbstractBooleanOperatorModel(List<BooleanModel> input) {
		super();
		
		ChangeListener<Boolean> cl = new ChangeListener<Boolean>() {			
			@Override
			public void changed(Boolean from, Boolean to) {
				computeAndFireIfChanged();	
			}
		};
		
		inputList = new ArrayList<BooleanModel>(input.size());
		
		for (BooleanModel m : input) {
			m.addChangeHandler(cl);	
			this.inputList.add(m);
		}		
		
		compute(input);
	}

	private void computeAndFireIfChanged() {
		Boolean previous = compute(inputList());
		fireIfChanged(previous, this.current);
	}

	private Boolean compute(List<BooleanModel> input) {
		Boolean previous = this.current;
		this.current = computeNewValue(input);
		return previous;
	}
	
	protected abstract Boolean computeNewValue(List<BooleanModel> input);

	@Override
	public Boolean get() {
		return current;
	}

	@Override
	public MutableBooleanModel asMutable() {
		return null;
	}

	public List<BooleanModel> inputList() {
		if (inputListView == null) {
			inputListView = Collections.unmodifiableList(this.inputList);	
		}
		
		return inputListView;
	}
	
	protected static List<BooleanModel> concat(BooleanModel ... input) {
		ArrayList<BooleanModel> a = new ArrayList<BooleanModel>();
		
		for (BooleanModel m : input) {
			a.add(m);
		}
		
		return a;
	}

}
