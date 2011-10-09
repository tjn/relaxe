/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

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
