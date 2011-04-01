/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class DefaultListModel<E extends Comparable<E>>
	extends AbstractValueModel<List<E>> 
	implements ListModel<E> {
	
	// add(E), add(int, E), addAll(int, Collection), clear(), equals(Object), 
	// get(int), hashCode(), indexOf(Object), iterator(), lastIndexOf(Object), 
	// listIterator(), listIterator(int), remove(int), set(int, E), subList(int, int)
	
	private ModelList<E> content;
		
	private static class ModelList<I extends Comparable<I>>
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
			List<I> original = unmodifiable(getCurrent());			
			boolean changed = getCurrent().add(e);
			
			if (changed) {
				model.fireChanged(original, unmodifiable(getCurrent()));
			}
			
			return changed;
		}

		@Override
		public void add(int index, I element) {
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
			List<I> original = unmodifiable(getCurrent());			
			boolean changed = getCurrent().addAll(c);
			
			if (changed) {
				// added to end => untouched slice is still valid.				
				model.fireChanged(original, unmodifiable(getCurrent()));
			}
			
			return changed;
		}
		
		
		@Override
		public boolean addAll(int index, Collection<? extends I> c) {
			if (c.isEmpty()) {
				return false;
			}
			
			boolean changed = false;
			
			if (index == size()) {
				changed = addAll(c);
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
			return getCurrent().size();
		}
	}
	
	@Override
	public List<E> get() {
		return getContent();
	}

	private List<E> getContent() {
		if (content == null) {
			content = new ModelList<E>(this);
		}

		return content;
	}	
}