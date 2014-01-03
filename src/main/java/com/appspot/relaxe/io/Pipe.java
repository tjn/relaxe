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
package com.appspot.relaxe.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

public class Pipe
	implements Runnable {
	
	private InputStream in;
	private OutputStream out;
	
	private EnumSet<Endpoint> close;
	
	public enum Endpoint {
		IN,
		OUT;
		
		public static EnumSet<Endpoint> both() {
			return EnumSet.allOf(Endpoint.class);
		}
		
		public static EnumSet<Endpoint> none() {
			return EnumSet.noneOf(Endpoint.class);
		}
	}
	
	public Pipe(InputStream in, OutputStream out) {
		this(in, out, Endpoint.none());
	}	
	
	public Pipe(InputStream in, OutputStream out, Endpoint close) {
		this(in, out, EnumSet.of(close));
	}

	
	public Pipe(InputStream in, OutputStream out, EnumSet<Endpoint> toClose) {
		super();
		
		if (in == null) {
			throw new NullPointerException("'in' must not be null");
		}
			
		if (out == null) {
			throw new NullPointerException("'out' must not be null");
		}		
		
		this.in = in;
		this.out = out;
		close = (close == null) ? Endpoint.none() : EnumSet.copyOf(toClose);
	}

	@Override
	public void run() {		
		int b;
								
		try {
			try {
				while((b = in.read()) != -1) {
					out.write((byte) b);
				}
			}
			finally {
				if (this.close.contains(Endpoint.IN)) {
					in = doClose(in);
				}
				
				if (this.close.contains(Endpoint.OUT)) {
					out = doClose(out);	
				}					
			}
		}
		catch (IOException e) {
			onError(e);				
		}
	}
	
	private InputStream doClose(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} 
			catch (IOException e) {
				onError(e);
			}
		}
		
		return in;
	}
	
	private OutputStream doClose(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} 
			catch (IOException e) {	
				onError(e);
			}
		}
		
		return out;
	}
					
	public void onError(IOException e) {		
	}
}