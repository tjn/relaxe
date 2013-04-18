/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaName;
import fi.tnie.db.map.JavaType;
import fi.tnie.db.map.TableMapper;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;

public class DefaultTableMapper
	implements TableMapper {
	    
	private String rootPackage;		
	private String contextPackage;
			
	private Map<Part, JavaType> createEntityTypeMap(Table table) {	    		
//		String pkg = getPackageName(table.getSchema());		
		String pkg = getPackageName(table.getName().getQualifier());
		String u = getSimpleName(table);
	
		EnumMap<Part, JavaType> types = new EnumMap<Part, JavaType>(Part.class);
		
		for (Part p : Part.values()) {
		    types.put(p, map(table, p, pkg, u));
        }
		
//		types.put(Part.INTERFACE, new JavaType(p, u));
//		types.put(Part.ABSTRACT, new JavaType(p, "Abstract" + u));
//		types.put(Part.HOOK, new JavaType(p, "Default" + u));
//		types.put(Part.IMPLEMENTATION, new JavaType(p + ".impl", u + "Impl"));
		
		return types;
	}
	
	protected JavaType map(Table t, Part p, String rootPackage, String name) {	    
	    String pp = getPackageName(t, p, rootPackage);
	    String n = getClassName(t, p, name);	    
	    return (pp == null || n == null) ? null : new JavaType(pp, n);
	}

    protected String getPackageName(Table t, Part p, String pkg) {
        return pkg;
    }
    
    protected String getClassName(Table t, Part p, String name) {
        if (p == Part.ABSTRACT) {
            return "Abstract" + name;
        }
        
        if (p == Part.HOOK) {
            return "Default" + name;
        }
        
        if (p == Part.HAS) {
            return "Has" + name;
        }
        
        if (p == Part.HAS_KEY) {
            return "Has" + name + "Key";
        }        
        
        if (p == Part.LITERAL_TABLE_ENUM) {
            return "Literal" + name + "Column";
        }
        
        if (p == Part.IMPLEMENTATION) {
            return name + "Impl";
        }
        
        return name;
    }
    
    
    private String getPackageName(SchemaName schema) {
        StringBuffer n = new StringBuffer();
        
        String p = getRootPackage();
        
        if (p != null) {
            n.append(p);
            n.append(".");
        }
        
        String s = schema.getSchemaName().getName().toLowerCase();
        
        if (s.equals("public")) {
            s = "pub";
        }
        
        s = s.replace("_", "");
        
        n.append(s);        
        
        return n.toString();
	}
	
	public String getSimpleName(Table table) {                
        return getSimpleName(table.getUnqualifiedName());	    
	}
	
	public String getSimpleName(Identifier identifier) {	            
	    return translate(identifier.getName());        
	}

    public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}

	public String getRootPackage() {
		return rootPackage;
	}

	private String translate(String n) {
		String[] tokens = n.split("_");
		
		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < tokens.length; i++) {
			capitalize(tokens[i], buf);			
		}
		
		return buf.toString();
	}
	
	private void capitalize(String t, StringBuffer dest) {		
		dest.append(Character.toUpperCase(t.charAt(0)));
		
		if (t.length() > 1) {
			dest.append(t.substring(1).toLowerCase());
		}		
	}
	
	public DefaultTableMapper(String rootPackage) {
		this(rootPackage, null);
	}
	
	public DefaultTableMapper(String rootPackage, String contextPackage) {
		super();
		setRootPackage(rootPackage);
		setContextPackage(contextPackage);
	}
	
	    
//    public AttributeInfo getAttributeInfo(Table table, Column c) { 
//    	
//    	// logger().is
//    	
//    	
//        DefaultAttributeInfo a = new DefaultAttributeInfo(table, c);
//        
//        int type = c.getDataType().getDataType();                
//
//        switch (type) {
//        case Types.CHAR:
//        	a.setAttributeType(String.class);
//        	a.setHolderType(CharHolder.class);
//        	a.setKeyType(CharKey.class);
//        	a.setAccessorType(CharAccessor.class);
//        	a.setPrimitiveType(CharType.TYPE);        	
//        case Types.VARCHAR:
//        	a.setAttributeType(String.class);
//        	a.setHolderType(VarcharHolder.class);
//        	a.setKeyType(VarcharKey.class);
//        	a.setAccessorType(VarcharAccessor.class);
//        	a.setPrimitiveType(VarcharType.TYPE);        	
//        	a.setIdentityMapType(VarcharIdentityMap.class);
//            break;            	
//        case Types.LONGNVARCHAR:
//        	break;        	
//        case Types.SMALLINT:        	
//        case Types.INTEGER:
//        case Types.TINYINT:
//        	a.setAttributeType(Integer.class);
//        	a.setHolderType(IntegerHolder.class);
//        	a.setKeyType(IntegerKey.class);
//        	a.setAccessorType(IntegerAccessor.class);
//        	a.setPrimitiveType(IntegerType.TYPE);
//        	a.setIdentityMapType(IntegerIdentityMap.class);
//            break;
//        case Types.BIGINT:                        
//        case Types.BIT:
//                       
//        case Types.REAL:
//            break;
//        case Types.FLOAT:                
//        case Types.DOUBLE:
//        	a.setAttributeType(Double.class);
//        	a.setHolderType(DoubleHolder.class);
//        	a.setKeyType(DoubleKey.class);
//        	a.setAccessorType(DoubleAccessor.class);
//        	a.setPrimitiveType(DoubleType.TYPE);
//            break;
//        case Types.DECIMAL:
//        case Types.NUMERIC:
//        	a.setAttributeType(Decimal.class);
//        	a.setHolderType(DecimalHolder.class);
//        	a.setKeyType(DecimalKey.class);
//        	a.setAccessorType(DecimalAccessor.class);
//        	a.setPrimitiveType(DecimalType.TYPE);
//            break;        	
//        case Types.DATE:            
//        	a.setAttributeType(Date.class);
//        	a.setHolderType(DateHolder.class);
//        	a.setKeyType(DateKey.class);
//        	a.setAccessorType(DateAccessor.class);
//        	a.setPrimitiveType(DateType.TYPE);
//            break;
//            
//        case Types.TIME:            
//        	a.setAttributeType(Date.class);
//        	a.setHolderType(TimeHolder.class);
//        	a.setKeyType(TimeKey.class);
//        	a.setAccessorType(TimeAccessor.class);
//        	a.setPrimitiveType(TimeType.TYPE);
//            break;
//            
//        case Types.TIMESTAMP:
//        	a.setAttributeType(Date.class);
//        	a.setHolderType(TimestampHolder.class);
//        	a.setKeyType(TimestampKey.class);
//        	a.setAccessorType(TimestampAccessor.class);
//        	a.setPrimitiveType(TimestampType.TYPE);
//            break;
//            
//        case Types.DISTINCT:
//	        {
//	        	String tn = c.getDataType().getTypeName();
//	        	
//	        	if (tn.equals("interval_ym")) {        	
//		        	a.setAttributeType(Interval.YearMonth.class);
//		        	a.setHolderType(IntervalHolder.YearMonth.class);
//		        	a.setKeyType(IntervalKey.YearMonth.class);
//		        	a.setAccessorType(IntervalAccessor.YearMonth.class);
//		        	a.setPrimitiveType(IntervalType.YearMonth.TYPE);
//	        	}
//	        }
//         	
//        	break;
//            
//        case Types.OTHER:
//	        {
//	        	String tn = c.getDataType().getTypeName();
//	        	
//	        	if (tn.equals("interval")) {        	
//		        	a.setAttributeType(Interval.DayTime.class);
//		        	a.setHolderType(IntervalHolder.DayTime.class);
//		        	a.setKeyType(IntervalKey.DayTime.class);
//		        	a.setAccessorType(IntervalAccessor.DayTime.class);
//		        	a.setPrimitiveType(IntervalType.DayTime.TYPE);
//	        	}
//	        }
//        	break;
//            
//        default:                
//            break;
//    }
//
//    	
//    	return a;
//    }

    	

//    @Override
//    public Class<?> getAttributeHolderType(Table table, Column c) {
//        int type = c.getDataType().getDataType();
//        Class<?> jtype = null;
//                        
//        switch (type) {
//            case Types.CHAR:
//            	jtype = CharHolder.class;
//            case Types.VARCHAR:
//                jtype = VarcharHolder.class; 
//                break;            	
//            case Types.LONGNVARCHAR:
//            	break;
//            case Types.INTEGER:            
//                jtype = IntegerHolder.class;
//                break;
//            case Types.TINYINT:            
//                break;
//            case Types.BIGINT:                        
//            case Types.BIT:
//                           
//            case Types.REAL:
//                break;
//            case Types.FLOAT:                
//            case Types.DOUBLE:
//            	jtype = DoubleHolder.class;                
//                break;                
//            case Types.NUMERIC:                
//                break;
//            case Types.DATE:            
//                jtype = DateHolder.class;
//                break;
//            case Types.TIMESTAMP:
//                jtype = TimestampHolder.class;
//                break;                
//                
//            default:                
//                break;
//        }
//                
//        return jtype;
//    }

    @Override
    public JavaType entityType(Table table, Part part) {
        // TODO: cache JavaType's keyed by table+part
        return createEntityTypeMap(table).get(part);        
    }

//    @Override
//    public File getSourceDir(BaseTable table, Part part) {
//        return getSourceDirMap().get(part);
//    }

//    public void setSourceDir(Part part, File sourceDir) {
//        getSourceDirMap().put(part, sourceDir);
//    }
//
//    private EnumMap<Part, File> getSourceDirMap() {
//        if (sourceDirMap == null) {
//            sourceDirMap = new EnumMap<Part, File>(Part.class);            
//        }
//
//        return sourceDirMap;
//    }

    @Override
    public JavaType factoryType(Schema schema, Part part) {        
        String p = getPackageName(new SchemaName(schema, true));
        String n = getSimpleName(schema.getUnqualifiedName());
                
        if (part == Part.INTERFACE) {
            return new JavaType(p, n + "Factory");            
        }
        
        if (part == Part.IMPLEMENTATION) {
            return new JavaType(p + ".impl", n + "FactoryImpl");            
        }
        
        return null;
    }

    @Override
    public JavaType catalogContextType() {
    	String p = getContextPackage();
        return new JavaType(p, "CatalogContext");
    }

//	@Override
//	public Class<?> getAttributeType(Table table, Column c) {
//        int type = c.getDataType().getDataType();        
//        boolean nn = c.isDefinitelyNotNullable();
//
//        Class<?> jtype = null;
//                        
//        switch (type) {
//            case Types.CHAR:
//            case Types.VARCHAR:            	
//            case Types.LONGNVARCHAR:
//                jtype = String.class; 
//                break;                 
//            case Types.INTEGER:            
//                jtype = Integer.class;
//                break;
//            case Types.TINYINT:            
//                jtype = Short.class;
//                break;
//            case Types.BIGINT:
////                jtype = BigInteger.class;
//            	// not supported yet
//            	break;
//            case Types.BIT:
//                jtype = Boolean.class;            
//            case Types.REAL:
//                jtype = Float.class;
//                break;
//            case Types.FLOAT:                
//            case Types.DOUBLE:
//                jtype = Double.class;
//                break;                
//            case Types.NUMERIC:
//                jtype = BigDecimal.class;
//                break;
//            case Types.DATE:                
//            case Types.TIMESTAMP:
//                jtype = Date.class;
//                break;                
//            default:            	
////                jtype = Object.class;
//            	// generally not supported yet
//                break;
//        }
//                
//        return jtype;	
//    }

	public String getContextPackage() {
		if (contextPackage == null) {
			return getRootPackage();			
		}

		return contextPackage;
	}

	public void setContextPackage(String contextPackage) {
		this.contextPackage = contextPackage;
	}
	
	public void setContextSubPackage(String sp) {
		setContextPackage(getRootPackage() + "." + sp);
	}

	@Override
	public JavaType literalContextType() {
    	String p = getRootPackage();
        return new JavaType(p, "LiteralCatalog");
	}

//	@Override
//	public Class<?> getAttributeKeyType(Table table, Column c) {
//		// TODO:
//		return IntegerKey.class;
//	}
//
//	@Override
//	public Class<?> getAttributeValueType(Table table, Column c) {
//		// TODO
//		return IntegerValue.class;
//	}
}

    
