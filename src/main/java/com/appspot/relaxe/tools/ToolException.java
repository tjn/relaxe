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

import java.io.IOException;
import java.sql.SQLException;

public class ToolException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 283736356679234853L;

	private int exitCode;    
    
    public static final int GENERAL_ERROR = -1;
    public static final int IO_ERROR = -2;    
    public static final int CONFIGURATION_ERROR = -3;
    public static final int SQL_ERROR = -4;
            
    public ToolException(String msg) {
        this(GENERAL_ERROR, msg, null);        
    }
    
    public ToolException(IOException cause) {
        this(IO_ERROR, cause.getMessage(), cause);        
    }
    
    public ToolException(SQLException cause) {
        this(SQL_ERROR, cause.getMessage(), cause);        
    }
    
    public ToolException(String msg, Throwable cause) {
        this(GENERAL_ERROR, msg, cause);        
    }
        
    protected ToolException(int exit, String msg) {
        this(exit, msg, null);        
    }

    protected ToolException(int exit, String msg, Throwable cause) {
        super(msg, cause);
        setExitCode(exit);
    }

    public int getExitCode() {
        return exitCode;
    }

    private void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }    
}
