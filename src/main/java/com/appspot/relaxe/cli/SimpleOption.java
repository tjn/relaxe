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

public class SimpleOption
    implements Option {    
    
    private String name;
    private String flag;
    private String description;
    private Parameter argument;
    
    public SimpleOption(String flag) {
        this(null, flag, null, null);
    }
    
    public SimpleOption(String name, String flag) {
        this(name, flag, null, null);
    }
    
    public SimpleOption(String name, Parameter argument) {
        this(name, null, argument);
    }    
    
    public SimpleOption(String name, String flag, Parameter argument) {
        this(name, flag, argument, null);
    }
    
    public SimpleOption(String name, String flag, String desc) {
        this(name, flag, null, desc);
    }
            
    public SimpleOption(String name, String flag, Parameter argument, String desc) {
        super();
        
        if (name == null && flag == null) {
            throw new NullPointerException("name and flag can not be both null");
        }
        
        // TODO: perform some further validation on name and flag        
        
        this.name = name;
        this.flag = flag;
        this.argument = argument;
        this.description = desc;
    }
    
    public String name() {
        return this.name;
    }
    
    public Parameter getParameter() {
        return this.argument;
    }
    
    public String flag() {
        return this.flag;
    }  
    
    
    @Override
    public String toString() {     
        return name + " [" + flag + "] + " + argument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
