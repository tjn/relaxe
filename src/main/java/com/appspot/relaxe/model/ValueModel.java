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


/**
 * Value represents a value of monitorable value.
 * 
 * @author tnie
 *
 * @param <V> AbstractType of the value.
 */
public interface ValueModel<V> {	
	/** 
	 * Current value of the model.
	 * 
	 * @return
	 */
	V get();
	
	/**
	 * Registers a listener for value changes.
	 *    
	 * @param ch
	 * @return
	 */
	Registration addChangeHandler(ChangeListener<V> ch);

	/**
	 * If this model is mutable, returns a model by which this model can be manipulated (typically <code>this</code>)
	 * 
	 * Returns <code>null</code> if this model is read-only. 
	 *   
	 * @return
	 */
	MutableValueModel<V> asMutable();
	
	/**
	 * Returns true if and only if this model is mutable. 
	 *   
	 * @return
	 */	
	boolean isMutable();
	
	/**
	 * If this model is immutable, returns itself, otherwise, wraps itself in immutable model and returns the immutable view. 
	 *   
	 * @return
	 */	
	ImmutableValueModel<V> asImmutable();
	
	/**
	 * Returns true if and only if this model is a constant model. 
	 * Constant model provides a guarantee that every invocation of get() returns always a reference to the same object.
	 *   
	 * @return
	 */
	boolean isConstant();
	
	/**
	 * Returns this model's current status as a constant model.
	 * 
	 * @return
	 */
	ConstantValueModel<V> asConstant();
}
