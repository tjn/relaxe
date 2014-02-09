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
package com.appspot.relaxe.feature;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ddl.AlterTableAddColumn;
import com.appspot.relaxe.expr.ddl.types.TimestampTypeDefinition;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Schema;

public class MetaData
    extends AbstractFeature
    implements SQLGenerator {

    public MetaData() {
        super(MetaData.class, 0, 1);
    }

    @Override
    public String getName() {
        return "MetaData";
    }

    @Override
    public SQLGenerator getSQLGenerator() {
        return this;
    }

    @Override
    public SQLGenerationResult modify(Catalog cat)
            throws SQLGenerationException {
        
        Environment env = cat.getEnvironment();
        SQLGenerationResult result = new SQLGenerationResult();
        
        final Identifier n = env.getIdentifierRules().toIdentifier("created_at");
                
        for (Schema s : cat.schemas().values()) {
            for (BaseTable t : s.baseTables().values()) {                
                Column c = t.getColumnMap().get(n);
                
                if (c == null) {                    
                    result.add(addStatement(t, n));
                }                                
            }
        }
        
        return result;
    }

    @Override
    public SQLGenerationResult revert(Catalog cat)
            throws SQLGenerationException {
        SQLGenerationResult result = new SQLGenerationResult();
        
        
        return result;
    }    
    
    private AlterTableAddColumn addStatement(BaseTable t, Identifier n) {        
        return new AlterTableAddColumn(t.getName(), n, TimestampTypeDefinition.get());
    }
 }
