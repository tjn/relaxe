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
package com.appspot.relaxe.tools;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaMap;

import fi.tnie.util.cli.Parser;

public class CatalogInfo
    extends CatalogTool {
        
    /**
     * @param args
     */
    public static void main(String[] args) {        
        System.exit(new CatalogInfo().run(args));
    }
    
    @Override
    public void run() {
        Identifier n = getCatalog().getName();
        String cat = (n == null) ? null : n.getName();
        message("Catalog loaded: " + ((cat == null) ? "<unnamed>" : cat));
        
        SchemaMap sm = getCatalog().schemas();
        
        message("schemas (" + sm.values().size() + ") {");
        
        for (Schema s : sm.values()) {
        	message(s.getUnqualifiedName().getName());        		
		}
        
        message("}");
        
        
    }
    
    @Override
    protected void prepare(Parser p) {        
        super.prepare(p);
        addOption(p, OPTION_VERBOSE);        
    }
}
