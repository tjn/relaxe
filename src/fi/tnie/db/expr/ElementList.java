/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ElementList<E extends Element>
	extends CompoundElement {
	
	private Symbol delim;
	private List<E> content;
			
	public ElementList(Symbol delim) {
		super();
		this.delim = delim;
	}
	
	public ElementList() {
		this(Symbol.COMMA);
	}
	
	

//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		List<? extends E> el = getContent();
//		
//		if (el == null) {
//			throw new NullPointerException("element-list must not be null");
//		}
//		
//		if (el.isEmpty()) {
//			throw new IllegalArgumentException("element-list must not be empty");
//		}		
//		
//		dest.append(" ");
//		
//		for(Iterator<? extends E> ei = el.iterator(); ei.hasNext();) {
//			Element e = ei.next();
//			e.generate(qc, dest);
//						
//			if (delim != null) {
//				if (ei.hasNext()) {
//					dest.append(delim);
//				}
//			}
//		}
//		
//		dest.append(" ");
//	}

	public List<E> getContent() {
		if (content == null) {
			content = new ArrayList<E>();			
		}

		return content;
	}

	public boolean add(E e) {
		return getContent().add(e);
	}
	
	
	public void set(E e) {
		if (e == null) {
			throw new NullPointerException("'e' must not be null");
		}
		
		getContent().clear();
		add(e);
	}
	
	public boolean isEmpty() {
		return this.content == null || this.content.isEmpty();  
	}
	
	public void copyTo(ElementList<E> dest) {
		if (!isEmpty()) {
			List<E> el = dest.getContent();
			
			for (E e : this.content) {
				el.add(e);
			}
		}
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		if (isEmpty()) {
			throw new IllegalStateException("list is empty: " + this);
		}
		else {
			Iterator<E> ei = getContent().iterator();
			
			Symbol p = getDelim();
			
			while (ei.hasNext()) {
				E e = ei.next();
				
				e.traverse(vc, v);
				
				if (p != null && ei.hasNext()) {
					p.traverse(vc, v);
				}
			}			
		}
	}

	public Symbol getDelim() {
		return delim;
	}
}
