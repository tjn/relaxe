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
package com.appspot.relaxe.feature;

import com.appspot.relaxe.meta.Catalog;

public interface SQLGenerator {    
       
    
    /**
     * Generates new or modifies existing SQL Objects in the catalog. 
     * 
     * @param cat
     * @return
     * @throws SQLGenerationException
     */
    SQLGenerationResult modify(Catalog cat)
        throws SQLGenerationException;

    /**
     * Reverts the modifications made in the existing SQL Objects in the catalog. 
     * 
     * @param c
     * @param cat
     * @return
     * @throws SQLGenerationException
     */
    SQLGenerationResult revert(Catalog cat)
        throws SQLGenerationException;
}
