/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.TableMapper.Part;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;
import fi.tnie.db.source.JavaType;

public class DefaultTableMapper
	implements TableMapper {
	    
	private String rootPackage;		
			
	private Map<Part, JavaType> createEntityTypeMap(BaseTable table) {	    		
		String pkg = getPackageName(table.getSchema());		
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
	
	protected JavaType map(BaseTable t, Part p, String rootPackage, String name) {	    
	    String pp = getPackageName(t, p, rootPackage);
	    String n = getClassName(t, p, name);	    
	    return (pp == null || n == null) ? null : new JavaType(pp, n);
	}

    protected String getPackageName(BaseTable t, Part p, String pkg) {
        return pkg;
    }
    
    protected String getClassName(BaseTable t, Part p, String name) {
        if (p == Part.ABSTRACT) {
            return "Abstract" + name;
        }
        
        if (p == Part.HOOK) {
            return "Default" + name;
        }
        
        if (p == Part.IMPLEMENTATION) {
            return name + "Impl";
        }
        
        return name;
    }
    
    
    private String getPackageName(Schema schema) {
        StringBuffer n = new StringBuffer();
        
        String p = getRootPackage();
        
        if (p != null) {
            n.append(p);
            n.append(".");
        }
        
        String s = schema.getUnqualifiedName().getName();

        if (s.equals("public")) {
            s = "pub";
        }
        
        n.append(s);        
        
        return n.toString();
	}
	
	private String getSimpleName(BaseTable table) {                
        return getSimpleName(table.getUnqualifiedName());	    
	}
	
	private String getSimpleName(Identifier identifier) {	            
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
		super();
		setRootPackage(rootPackage);
	}

    @Override
    public Class<?> getAttributeType(Table table, Column c) {
        int type = c.getDataType().getDataType();
        
        boolean nn = c.isDefinitelyNotNullable();

        Class<?> jtype = null;
                        
        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGNVARCHAR:
                jtype = String.class; 
                break;                 
            case Types.INTEGER:            
                jtype = nn ? Integer.TYPE : Integer.class;
                break;
            case Types.TINYINT:            
                jtype = nn ? Short.TYPE : Short.class;
                break;
            case Types.BIGINT:
                jtype = BigInteger.class;        
            case Types.BIT:
                jtype = nn ? Boolean.TYPE : Boolean.class;            
            case Types.REAL:
                jtype = nn ? Float.TYPE : Float.class;
                break;
            case Types.FLOAT:                
            case Types.DOUBLE:
                jtype = nn ? Double.TYPE : Double.class;
                break;                
            case Types.NUMERIC:
                jtype = BigDecimal.class;
                break;
            default:
                jtype = Object.class;
                break;
        }
                
        return jtype;
    }

    @Override
    public JavaType entityType(BaseTable table, Part part) {
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
        
        String p = getPackageName(schema);
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
        return new JavaType(getRootPackage(), "CatalogContext");
    }
    
}

    
