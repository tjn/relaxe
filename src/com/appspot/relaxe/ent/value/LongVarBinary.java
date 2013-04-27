/*
 * Copyright (c) 2009-2013 Topi Nieminen
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
