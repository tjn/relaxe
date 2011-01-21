/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.rpc.PrimitiveHolder;

public abstract class PrimitiveType<T extends PrimitiveType<T>>
	extends Type<T> {
		
	protected PrimitiveType() {
		super();
	}

	@Override
	public final boolean isReferenceType() {	
		return false;
	}
	
	public abstract int getSqlType();
	
	public abstract PrimitiveHolder<?, T> nil();
	
	public static PrimitiveType<?> get(int sqltype) {
		PrimitiveType<?> p = null;
		
		switch (sqltype) {
		case Type.CHAR:
			p = CharType.TYPE;
			break;
		case Type.DATE:
			p = DateType.TYPE;
			break;
		case Type.DECIMAL:
			p = DecimalType.TYPE;
			break;
		case Type.DOUBLE:
			p = DoubleType.TYPE;
			break;
		case Type.FLOAT:
			p = FloatType.TYPE;
			break;
		case Type.INTEGER:
			p = IntegerType.TYPE;
			break;
		case Type.LONGVARCHAR:
			p = LongVarcharType.TYPE;
			break;
		case Type.OTHER:
			p = OtherType.TYPE;
			break;
		case Type.TIMESTAMP:
			p = TimestampType.TYPE;
			break;
		case Type.VARCHAR:
			p = VarcharType.TYPE;
			break;			
		default:
			break;
		}
		
		return p;
	}
}
