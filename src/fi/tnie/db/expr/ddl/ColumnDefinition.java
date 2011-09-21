/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr.ddl;

import fi.tnie.db.expr.CompoundElement;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.ElementVisitor;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.VisitContext;

public class ColumnDefinition
    extends CompoundElement
    implements BaseTableElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1225762403113588730L;
	private Identifier name;
    private ColumnDataType dataType;
    private ElementList<ColumnConstraint> constraintList;
    
    private DefaultDefinition defaultDefinition;
    
    /**
	 * No-argument constructor for GWT Serialization
	 */
	protected ColumnDefinition() {
	}
    
    public ColumnDefinition(Identifier name, ColumnDataType dataType) {
        this(name, dataType, null);
    }
    
    public ColumnDefinition(Identifier name, ColumnDataType dataType, DefaultDefinition defaultDefinition) {
        super();
        
        if (name == null) {
            throw new NullPointerException("'name' must not be null");
        }
        
        if (dataType == null) {
            throw new NullPointerException("'dataType' must not be null");
        }
        
        this.name = name;
        this.dataType = dataType;
        this.defaultDefinition = defaultDefinition;
    }
    
    
    @Override
    protected void traverseContent(VisitContext vc, ElementVisitor v) {
        this.name.traverse(vc, v);
        this.dataType.traverse(vc, v);
        
        traverseNonEmpty(this.defaultDefinition, vc, v);
        traverseNonEmpty(this.constraintList, vc, v);                
    }

    public ElementList<ColumnConstraint> getConstraintList() {
        if (constraintList == null) {
            constraintList = new ElementList<ColumnConstraint>();
        }

        return constraintList;
    }    

}
