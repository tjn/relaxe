package com.appspot.relaxe.ent;

import java.util.ArrayList;
import java.util.List;

public class Operation {
	
	public static final class Context {
		private List<Callback> nodes = new ArrayList<Callback>();
		
		private Context() {			
		}
		
		public void register(Operation.Callback node) {
			this.nodes.add(node);			
		}		
	}	

	public interface Callback {
		public void finish(Operation.Context context);		
	}
		
	private final Context context = new Context();
	
	public void finish() {		
		for (Callback n : this.context.nodes) {
			n.finish(this.context);
		}
		
		this.context.nodes.clear();
	}
	
	public final Context getContext() {
		return context;
	}
}
