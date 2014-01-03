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
package com.appspot.relaxe.cli;

public class Parameter {
        
    private String name;        
    private int minCount;
    private Integer maxCount;
               
    public Parameter() {
        this(null, false);
    }
    
    public Parameter(boolean optional) {
        this(null, optional);
    }
    
    public Parameter(String name) {
    	this(name, false);
    }
    
    public Parameter(String name, boolean optional) {
        super();
        setName(name);
        setMinCount(optional ? 0 : 1);
        setMaxCount(new Integer(1));
    }
    
    public Parameter(String name, int count) {
        this(name, count, count);
    }
    
    public Parameter(String name, int min, int max) {
        this(name, min, Integer.valueOf(max));        
    }
    
    public Parameter(String name, int min, Integer max) {
        super();
        validate(min, maxCount);
        
        setName(name);
        setMinCount(min);
        setMaxCount(max);
    }

    public boolean isOptional() {
        return (minCount == 0);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getMinCount() {
        return minCount;
    }
    
    private void validate(int min, Integer max) {
        if (min < 0) {
            throw new IllegalArgumentException("min: " + min);
        }

        if (max != null) {
            if (max.intValue() < 0) {
                throw new IllegalArgumentException("max: " + max);
            }
    
            if (max.intValue() < min) {
                throw new IllegalArgumentException("invalid max: " + max);            
            }
        }
    }

    private void setMinCount(int minCount) {        
        this.minCount = minCount;
    }
    
    public void setCount(int min, Integer max) {
        validate(min, max);        
        setMinCount(min);  
        setMaxCount(max);
    }

    public Integer getMaxCount() {
        return this.maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }
    
    @Override
    public String toString() {     
        return "parameter:" + name + "[" + this.getMinCount() + ", " + getMaxCount() + "]";
    }
}
