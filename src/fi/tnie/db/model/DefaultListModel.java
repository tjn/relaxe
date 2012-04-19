/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class DefaultListModel<E>
	extends AbstractValueModel<List<E>> 
	implements ListModel<E> {
	
	// add(E), add(int, E), addAll(int, Collection), clear(), equals(Object), 
	// get(int), hashCode(), indexOf(Object), iterator(), lastIndexOf(Object), 
	// listIterator(), listIterator(int), remove(int), set(int, E), subList(int, int)
	
	private ModelList<E> content;
		
	private static class ModelList<I>
		extends AbstractList<I> {
		
		private DefaultListModel<I> model;				
		private ArrayList<I> current;
				
		public ModelList(DefaultListModel<I> model) {
			super();
			
			if (model == null) {
				throw new NullPointerException("model");
			}
			
			this.model = model;
		}

		@Override
		public boolean add(I e) {
			this.model.adding(e);
			
			List<I> original = unmodifiable(getCurrent());			
			boolean changed = getCurrent().add(e);
			
			if (changed) {
				model.fireChanged(original, unmodifiable(getCurrent()));
			}
			
			return changed;
		}


		@Override
		public void add(int index, I element) {
			this.model.adding(element);
			
			if (index == size()) {
				add(element);
			}
			else {
				List<I> original = unmodifiable(new ArrayList<I>(getCurrent()));			
				getCurrent().add(index, element);
				model.fireChanged(original, unmodifiable(this));	
			}
		}

		@Override
		public boolean addAll(Collection<? extends I> c) {
			return addAll(0, c);
			
//			if (c == null) {
//				throw new NullPointerException("c");
//			}
//			
//			if (c.isEmpty()) {
//				return false;
//			}
//			
//			adding(c);
//			
//			List<I> original = unmodifiable(getCurrent());			
//			boolean changed = getCurrent().addAll(c);
//			
//			if (changed) {
//				// added to end => untouched slice is still valid.				
//				model.fireChanged(original, unmodifiable(getCurrent()));
//			}
//			
//			return changed;
		}

		private void adding(Collection<? extends I> c) {
			DefaultListModel<I> m = this.model;
			
			for (I e : c) {
				m.adding(e);	
			}
		}		

		@Override
		public boolean addAll(int index, Collection<? extends I> c) {
			if (c == null) {
				throw new NullPointerException("c");
			}
			
			if (c.isEmpty()) {
				return false;
			}
			
			adding(c);		
						
			boolean changed = false;
			
			if (index == size()) {
				List<I> original = unmodifiable(getCurrent());			
				changed = getCurrent().addAll(c);
				
				if (changed) {
					// added to end => untouched slice is still valid.				
					model.fireChanged(original, unmodifiable(getCurrent()));
				}			
			}
			else {
				List<I> original = unmodifiable(new ArrayList<I>(getCurrent()));			
				changed = getCurrent().addAll(index, c);
				
				if (changed) {
					model.fireChanged(original, unmodifiable(this));
				}
			}
			
			return changed;
		}
			
		
		@Override
		public void clear() {
			if (isEmpty()) {
				return;
			}
			
			List<I> original = unmodifiable(getCurrent());									
			this.current = new ArrayList<I>();						
			model.fireChanged(original, unmodifiable(getCurrent()));
		}
		
		@Override
		public I remove(int index) {
			I pe = getCurrent().get(index);
			List<I> pl = remove(getCurrent(), index);
			getCurrent().remove(index);
			model.fireChanged(pl, unmodifiable(getCurrent()));			
			return pe;
		}
				
		public boolean swap(int a, int b) {
			checkIndex(a);
			checkIndex(b);
			
			if (a == b) {
				return false;
			}
			
			ArrayList<I> cl = getCurrent();
			List<I> original = new ArrayList<I>(cl);
			
			I ae = cl.get(a);
			I be = cl.get(b);
			cl.set(b, ae);
			cl.set(a, be);
			
			model.fireChanged(original, unmodifiable(getCurrent()));
			return true;
		}

		private void checkIndex(int index) 
			throws IndexOutOfBoundsException {
			
			if (index < 0 || index >= size())  {
				throw new ArrayIndexOutOfBoundsException(index);				
			}
		}

		@Override
		public boolean removeAll(Collection<?> c) {	
			if (c.isEmpty()) {
				return false;
			}
			
			ArrayList<I> pl = new ArrayList<I>(getCurrent());
			getCurrent().removeAll(c);
			model.fireChanged(unmodifiable(pl), unmodifiable(getCurrent()));
			
			return true;
		}


		@Override
		public boolean retainAll(Collection<?> c) {
			if (c.isEmpty()) {
				return false;
			}
			
			ArrayList<I> pl = new ArrayList<I>(getCurrent());
			boolean changed = getCurrent().retainAll(c);
			
			if (changed) {
				model.fireChanged(unmodifiable(pl), unmodifiable(getCurrent()));
			}
			
			return changed;
		}

		@Override
		public I set(int index, I element) {
			I pe = getCurrent().set(index, element);
			
			if (pe != element) {
				List<I> pl = previous(this, index, element);
				model.fireChanged(pl, unmodifiable(this));
			}			
			
			return pe;
		}
		
		private List<I> unmodifiable(List<I> source) {
			return unmodifiable(source, 0, source.size());
		}
				
		private List<I> unmodifiable(final List<I> source, final int from, final int to) {
			return new AbstractList<I>() {
				@Override
				public I get(int index) {					
					return source.get(from + index);
				}

				@Override
				public int size() {	
					return to - from;
				}
			};
		}
				
		private List<I> previous(final List<I> source, final int replacedAt, final I previous) {
			return new AbstractList<I>() {
				@Override
				public I get(int index) {
					return (index == replacedAt) ? previous : source.get(index); 
				}

				@Override
				public int size() {	
					return source.size();
				}
			};
		}
		
		
		private List<I> remove(final List<I> source, final int pos) {
			return new AbstractList<I>() {
				@Override
				public I get(int index) {
					return source.get((index < pos) ? index : index + 1); 
				}

				@Override
				public int size() {	
					return source.size() - 1;
				}
			};
		}		

		private ArrayList<I> getCurrent() {
			if (current == null) {
				current = new ArrayList<I>();				
			}

			return current;
		}

		@Override
		public I get(int index) {
			return getCurrent().get(index);
		}

		@Override
		public int size() {
			return (this.current == null) ? 0 : this.current.size();
		}
	}
	
	@Override
	public List<E> get() {
		return getContent();
	}

	private ModelList<E> getContent() {
		if (content == null) {
			content = new ModelList<E>(this);
		}

		return content;
	}	
	
	@Override
	public boolean isEmpty() {
		return this.content == null || this.content.isEmpty();
	}
	
	public boolean swap(int a, int b) {		
		return getContent().swap(a, b);
	}
	
	protected void adding(E e) {
	}

}