package com.appspot.relaxe.ent;

import java.io.Serializable;

public class Tuples {
		
	public static <E extends Serializable> Tuple<E> of(E element) {
		if (element == null) {
			throw new NullPointerException();
		}		

		return new One<E>(element);		
	}
	
	public static <E extends Serializable> Tuple<E> of(E a, E b) {
		if (a == null || b == null) {
			throw new NullPointerException();
		}		

		return new Two<E>(a, b);		
	}
	
	public static <E extends Serializable> Tuple<E> of(E a, E b, E c) {
		if (a == null || b == null || c == null) {
			throw new NullPointerException();
		}
		
		return new Three<E>(a, b, c);		
	}
	
	public static <E extends Serializable> Tuple<E> of(E[] elems) {
		if (elems == null) {
			throw new NullPointerException("elems");
		}
		
		
		switch (elems.length) {
		case 0:
			throw new IllegalArgumentException("array must not be empty");
		case 1:
			return of(elems[0]); 
		case 2:
			return of(elems[0], elems[1]); 
		case 3:
			return of(elems[0], elems[1], elems[2]); 
		default:
			break;
		}
		
		return new ArrayTuple<E>(elems);		
	}			
	
	public static final class One<E extends Serializable>
		implements Tuple<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4751217075075788720L;
		
		private E item;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */		
		private One() {
		}		
		
		private One(E item) {
			this.item = item;
		}

		@Override
		public int size() {
			return 1;
		}

		@Override
		public E get(int index) {
			if (index != 0) {
				throw new IndexOutOfBoundsException(Integer.toString(index));
			}			
			
			return this.item;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			
			if (o == null || (!(o instanceof Tuple<?>))) {
				return false;
			}
			
			Tuple<?> t = (Tuple<?>) o;
			
			if (t.size() != 1) {
				return false;
			}
			
			return this.item.equals(t.get(0));			
		}		
		
		
		@Override
		public int hashCode() {
			return this.item.hashCode();
		}
	}
	
	public static final class Two<E extends Serializable>
		implements Tuple<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2966560196262883213L;
		
		private E a;
		private E b;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */		
		private Two() {
		}		
		
		private Two(E a, E b) {
			this.a = a;
			this.b = b;
		}
	
		@Override
		public int size() {
			return 2;
		}
	
		@Override
		public E get(int index) {
			switch (index) {
			case 0:
				return this.a;
			case 1:
				return this.b;	
			default:
				throw new IndexOutOfBoundsException(Integer.toString(index));				
			}
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			
			if (o == null || (!(o instanceof Tuple<?>))) {
				return false;
			}
			
			Tuple<?> t = (Tuple<?>) o;
			
			if (t.size() != 2) {
				return false;
			}
			
			return this.a.equals(t.get(0)) && this.b.equals(t.get(1));
		}		
		
		@Override
		public int hashCode() {
			return a.hashCode() ^ b.hashCode();
		}
	}
	
	public static final class Three<E extends Serializable>
			implements Tuple<E> {
	
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -3901850153662721615L;
		
		private E a;
		private E b;
		private E c;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */		
		private Three() {
		}		
		
		private Three(E a, E b, E c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	
		@Override
		public int size() {
			return 3;
		}
	
		@Override
		public E get(int index) {
			switch (index) {
			case 0:
				return this.a;
			case 1:
				return this.b;	
			case 2:
				return this.c;
			default:
				throw new IndexOutOfBoundsException(Integer.toString(index));				
			}
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			
			if (o == null || (!(o instanceof Tuple<?>))) {
				return false;
			}
			
			Tuple<?> t = (Tuple<?>) o;
			
			if (t.size() != 3) {
				return false;
			}
			
			return a.equals(t.get(0)) && b.equals(t.get(1)) && c.equals(t.get(2));
		}		
		
		@Override
		public int hashCode() {
			return (a.hashCode() ^ b.hashCode()) ^ c.hashCode();
		}
	}
	
	public static final class ArrayTuple<E extends Serializable>
		implements Tuple<E> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = 5143183541977834923L;
		
		private E[] content;
		private int hash;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */		
		private ArrayTuple() {
		}		
		
		private ArrayTuple(E[] content) {
			if (content == null) {
				throw new NullPointerException("content");
			}
			
			this.content = content;
			int h = content[0].hashCode();
			
			for (int i = 1; i < content.length; i++) {
				E e = content[i];
				h ^= e.hashCode();			
			}
			
			this.hash = h;
		}
		
		@Override
		public int size() {
			return this.content.length;
		}
		
		@Override
		public E get(int index) {
			if (index < 0 || index >= content.length) {
				throw new IndexOutOfBoundsException(Integer.toString(index));				
			}
		
			return content[index];
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			
			if (o == null || (!(o instanceof Tuple<?>))) {
				return false;
			}
			
			Tuple<?> t = (Tuple<?>) o;
			
			int size = this.size();
			
			if (t.size() != size) {
				return false;
			}
						
			for (int i = 0; i < size; i++) {
				if (!this.get(i).equals(t.get(i))) {
					return false;
				}
			}			
			
			return true;
		}		
		
		@Override
		public int hashCode() {
			return this.hash;
		}
	}
}
