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

/**
 * @author Administrator
 *
 */
class Token {
    
    enum Type {
        OPTION,
        ARGUMENT
    }
    
    private String token;
    private Type type;
    
    
    static Token arg(String value) {
        return new Token(Type.ARGUMENT, value);
    }
    
    static Token opt(String value) {
        return new Token(Type.OPTION, value);
    }
    
    Token(Type type, String value) {
        this.type = type;
        this.token = value;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }
    
    @Override
    public String toString() {
        return this.type.name().substring(0, 1) + ":" + this.token;
    }
}