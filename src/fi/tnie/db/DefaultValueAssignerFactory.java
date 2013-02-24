/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.meta.DataType;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.PrimitiveType;

public class DefaultValueAssignerFactory
	implements ValueAssignerFactory {
		
		
	@Override
	public <T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T, H>> ParameterAssignment create(H holder, DataType columnType) {
		ParameterAssignment pa = null;
				
		PrimitiveHolder<?, ?, ?> ph = holder;
		int t = ph.getSqlType();
						
		switch (t) {
			case PrimitiveType.INTEGER:	 
				pa = createIntegerAssignment(ph.asIntegerHolder());
				break;
			case PrimitiveType.VARCHAR:	
				pa = createVarcharAssignment(ph.asVarcharHolder());
				break;
			case PrimitiveType.DATE:	
				pa = createDateAssignment(ph.asDateHolder());
				break;
			case PrimitiveType.TIME:	
				pa = createTimeAssignment(ph.asTimeHolder());
				break;				
			case PrimitiveType.TIMESTAMP:	
				pa = createTimestampAssignment(ph.asTimestampHolder());
				break;
			case PrimitiveType.DECIMAL:	 
			case PrimitiveType.NUMERIC:
				pa = createDecimalAssignment(ph.asDecimalHolder());
				break;				
			case PrimitiveType.OTHER:
				if ("interval_dt".equals(columnType.getTypeName())) {				
					pa = createIntervalAssignment((IntervalHolder.DayTime) ph);
				}
				break;
			case PrimitiveType.DISTINCT:	
				pa = createIntervalAssignment((IntervalHolder.YearMonth) ph);
				break;								
		default:
			break;
		}
				
		return pa;
	}


//	protected ParameterAssignment createArrayAssignment(ArrayHolder h) {
//		return new ArrayAssignment(h);
//	}
//
//	protected ParameterAssignment createBigintAssignment(BigintHolder h) {
//		return new BigintAssignment(h);
//	}
//
//	protected ParameterAssignment createBinaryAssignment(BinaryHolder h) {
//		return new BinaryAssignment(h);
//	}
//
//	protected ParameterAssignment createBitAssignment(BitHolder h) {
//		return new BitAssignment(h);
//	}
//
//	protected ParameterAssignment createBlobAssignment(BlobHolder h) {
//		return new BlobAssignment(h);
//	}
//
//	protected ParameterAssignment createBooleanAssignment(BooleanHolder h) {
//		return new BooleanAssignment(h);
//	}

	protected ParameterAssignment createCharAssignment(CharHolder h) {
		return new CharAssignment(h);
	}

//	protected ParameterAssignment createClobAssignment(ClobHolder h) {
//		return new ClobAssignment(h);
//	}
//
//	protected ParameterAssignment createDatalinkAssignment(DatalinkHolder h) {
//		return new DatalinkAssignment(h);
//	}

	protected ParameterAssignment createDateAssignment(DateHolder h) {
		return new DateAssignment(h);
	}

	protected ParameterAssignment createDecimalAssignment(DecimalHolder h) {
		return new DecimalAssignment(h);
	}
//
//	protected ParameterAssignment createDistinctAssignment(DistinctHolder h) {
//		return new DistinctAssignment(h);
//	}

//	protected ParameterAssignment createDoubleAssignment(DoubleHolder h) {
//		return new DoubleAssignment(h);
//	}

//	protected ParameterAssignment createFloatAssignment(FloatHolder h) {
//		return new FloatAssignment(h);
//	}
//
	protected ParameterAssignment createIntegerAssignment(IntegerHolder h) {
		return new IntegerAssignment(h);
	}

//	protected ParameterAssignment createJava_objectAssignment(Java_objectHolder h) {
//		return new Java_objectAssignment(h);
//	}
//
//	protected ParameterAssignment createLongnvarcharAssignment(LongnvarcharHolder h) {
//		return new LongnvarcharAssignment(h);
//	}
//
//	protected ParameterAssignment createLongvarbinaryAssignment(LongvarbinaryHolder h) {
//		return new LongvarbinaryAssignment(h);
//	}
//
//	protected ParameterAssignment createLongvarcharAssignment(LongvarcharHolder h) {
//		return new LongvarcharAssignment(h);
//	}
//
//	protected ParameterAssignment createNcharAssignment(NcharHolder h) {
//		return new NcharAssignment(h);
//	}
//
//	protected ParameterAssignment createNclobAssignment(NclobHolder h) {
//		return new NclobAssignment(h);
//	}
//
//	protected ParameterAssignment createStructAssignment(StructHolder h) {
//		return new StructAssignment(h);
//	}
//
//	protected ParameterAssignment createNullAssignment(NullHolder h) {
//		return new NullAssignment(h);
//	}
//
//	protected ParameterAssignment createNumericAssignment(NumericHolder h) {
//		return new NumericAssignment(h);
//	}
//
//	protected ParameterAssignment createNvarcharAssignment(NvarcharHolder h) {
//		return new NvarcharAssignment(h);
//	}
//
//	protected ParameterAssignment createOtherAssignment(OtherHolder h) {
//		return new OtherAssignment(h);
//	}
//
//	protected ParameterAssignment createRealAssignment(RealHolder h) {
//		return new RealAssignment(h);
//	}
//
//	protected ParameterAssignment createRefAssignment(RefHolder h) {
//		return new RefAssignment(h);
//	}
//
//	protected ParameterAssignment createRowidAssignment(RowidHolder h) {
//		return new RowidAssignment(h);
//	}
//
//	protected ParameterAssignment createSmallintAssignment(SmallintHolder h) {
//		return new SmallintAssignment(h);
//	}
//
//	protected ParameterAssignment createSqlxmlAssignment(SqlxmlHolder h) {
//		return new SqlxmlAssignment(h);
//	}

	protected ParameterAssignment createTimeAssignment(TimeHolder h) {
		return new TimeAssignment(h);
	}

	protected ParameterAssignment createTimestampAssignment(TimestampHolder h) {
		return new TimestampAssignment(h);
	}
	
	protected ParameterAssignment createIntervalAssignment(IntervalHolder.DayTime h) {
		return new IntervalAssignment.DayTime(h);
	}

	protected ParameterAssignment createIntervalAssignment(IntervalHolder.YearMonth h) {
		return new IntervalAssignment.YearMonth(h);
	}

//	protected ParameterAssignment createTinyintAssignment(TinyintHolder h) {
//		return new TinyintAssignment(h);
//	}
//
//	protected ParameterAssignment createVarbinaryAssignment(VarbinaryHolder h) {
//		return new VarbinaryAssignment(h);
//	}

	protected ParameterAssignment createVarcharAssignment(VarcharHolder h) {
		return new VarcharAssignment(h);
	}


}
