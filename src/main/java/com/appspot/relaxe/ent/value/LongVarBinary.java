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
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

public class LongVarBinary
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5597113959168263629L;
	
	private byte[] value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private LongVarBinary() {
	}
		
	
	private LongVarBinary(byte[] content) {
		if (content == null) {
			throw new NullPointerException("content");
		}		
		
		this.value = content;
	}
		
	public byte[] toArray() {
		byte[] copy = new byte[this.value.length];
		System.arraycopy(this.value, 0, copy, 0, copy.length);		
		return copy;
	}	
	
	
	public static class Builder {
		private byte[] content;
		private int offset;
		
		public Builder(int capacity) {
			this.offset = 0;
			this.content = new byte[capacity];			
		}
		
		public Builder(byte[] content) {
			this.offset = content.length;
			this.content = content;	
		}
		
		public boolean add(byte b) {
			if (this.offset < content.length) {
				content[offset] = b;
				offset++;
				return true;
			}
			
			return false;
		}		
		
		public int remaining() {
			return (this.content.length - this.offset);
		}
		
		public LongVarBinary newLongVarBinary() {
			int r = remaining();
			
			byte[] dest = (r > 0) ? this.content : new byte[this.content.length - r];
			
			if (r > 0) {
				System.arraycopy(this.content, 0, dest, 0, dest.length);				
			}
			
			return new LongVarBinary(dest);
		}
	}
}
