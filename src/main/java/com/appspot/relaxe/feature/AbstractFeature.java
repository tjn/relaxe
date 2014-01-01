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

import java.util.Collections;
import java.util.Set;

public abstract class AbstractFeature
    implements Feature {

    private int versionMajor;
    private int versionMinor;
    private String name;
    
    public AbstractFeature(Class<? extends Feature> featureType, int versionMajor, int versionMinor) {
        super();
        
        if (featureType == null) {
            throw new NullPointerException("'featureType' must not be null");
        } 
        
        String n = featureType.getCanonicalName();
        
        if (n == null) {
            throw new NullPointerException("canonical name 'featureType' must not be null");
        }
        
        this.name = n;
        
        this.versionMajor = versionMajor;
        this.versionMinor = versionMinor;
    }

    @Override
	public int getVersionMajor() {
        return versionMajor;
    }

    @Override
	public int getVersionMinor() {
        return versionMinor;
    }

    @Override
	public String getName() {
        return name;
    }
    
    @Override
    public Set<Dependency> dependencies() {        
        return Collections.emptySet();
    }
}
