/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;


import fi.tnie.db.expr.CompoundElement;
import fi.tnie.db.expr.Element;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Token;
import fi.tnie.db.expr.VisitContext;

public abstract class DataTypeDefinition
    extends CompoundElement 
    implements ColumnDataType {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8759055758167527360L;

	public static class Specification
        implements Token {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 6921118145748582274L;
		private int number;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		protected Specification() {	
		}
                
        public Specification(int number) {
            super();
            this.number = number;
        }

        @Override
        public String getTerminalSymbol() {
            return Integer.toString(this.number);
        }

        @Override
        public boolean isOrdinary() {
            return false;
        }

        @Override
        public void traverse(VisitContext vc, ElementVisitor v) {
            v.start(vc, this);            
            v.end(this);
        }        
    }
    
    public abstract Element getName();
    
    @Override
    public void traverse(VisitContext vc, ElementVisitor v) {
        v.start(vc, this);
        traverseNonEmpty(getName(), vc, v);
        v.end(this);
    }
}
