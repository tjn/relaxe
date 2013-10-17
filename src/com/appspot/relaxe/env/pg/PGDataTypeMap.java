/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.env.pg.expr.PGTextArrayTypeDefinition;
import com.appspot.relaxe.env.pg.expr.PGTextTypeDefinition;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.DefaultDataTypeMap;
import com.appspot.relaxe.types.PrimitiveType;

public class PGDataTypeMap
	extends DefaultDataTypeMap {
    
	protected PGDataTypeMap() {
	}
	
	@Override
	public SQLTypeDefinition getSQLTypeDefinition(DataType t) {
					
		if (t.getDataType() == PrimitiveType.VARCHAR && PGTextTypeDefinition.NAME.equals(t.getTypeName())) {
			return PGTextTypeDefinition.DEFINITION;
		}
		
		if (t.getDataType() == PrimitiveType.ARRAY && PGTextArrayTypeDefinition.NAME.equals(t.getTypeName())) {
			return PGTextArrayTypeDefinition.DEFINITION;
		}
		
		return super.getSQLTypeDefinition(t);
	}
	
	@Override
	public PrimitiveType<?> getType(DataType type) {
		int t = type.getDataType();
		String n = type.getTypeName();
		
		if ((t == PGTextType.TYPE.getSqlType()) && PGTextType.TYPE.getName().equals(n)) {
			return PGTextType.TYPE;
		}
		
		PrimitiveType<?> pt = super.getType(type);
		
		if (pt == null) {			
			switch (t) {
			case PrimitiveType.VARCHAR:
				if (PGTextType.TYPE.getName().equals(n)) {
					pt = PGTextType.TYPE;
				}
				break;			
			case PrimitiveType.ARRAY:
				if (PGTextArrayType.TYPE.getName().equals(n)) {
					pt = PGTextArrayType.TYPE;
				}
				break;
			case PrimitiveType.OTHER:
				if (PGTSVectorType.TYPE.getName().equals(n)) {
					pt = PGTSVectorType.TYPE;
				}
				break;				
			default:
				break;
			}
		}
		
		return pt;
	}
	
	@Override
	protected Integer getSize(DataType dataType) {
		if (SQLTypeDefinition.isIntegralType(dataType.getDataType())) {
			return null;
		}		
		
		return super.getSize(dataType);
	}

	@Override
	public SchemaElementName newName(String typeName) {
		return PGEnvironment.environment().getIdentifierRules().newName(typeName);
	}
	
}
