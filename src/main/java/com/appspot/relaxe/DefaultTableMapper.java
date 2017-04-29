/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaName;
import com.appspot.relaxe.map.JavaType;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.Table;


public class DefaultTableMapper
	implements TableMapper {
	    
	private String rootPackage;		
	private String contextPackage;
	
	private static Logger logger = LoggerFactory.getLogger(DefaultTableMapper.class);
			
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
    	
        if (p == Part.TYPE) {
            return name + "$Type";
        }
        
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
        
        if (p == Part.METADATA_IMPLEMENTATION) {
            return name + "MetaData";
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
        
        String s = schema.getSchemaName().getContent().toLowerCase();
        
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
	    return translate(identifier.getContent());        
	}

    public void setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
	}

	@Override
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
	

    @Override
    public JavaType entityType(Table table, Part part) {
        // TODO: cache JavaType's keyed by table+part
        return createEntityTypeMap(table).get(part);        
    }

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

	@Override
	public String toJavaIdentifier(CharSequence identifier) {
		
		logger().debug("identifier: '" + identifier + "'");
		
		int len = identifier.length();
						
		if (len == 0) {
			return "_";
		}
		
		StringBuilder buf = new StringBuilder(len);
		
		char fc = identifier.charAt(0);
		
		if (!Character.isJavaIdentifierStart(fc)) {
			buf.append("_");			
		}
		
		if (Character.isJavaIdentifierPart(fc)) {
			buf.append(fc);
		}		
		
		for (int i = 1; i < len; i++) {
			char c = identifier.charAt(i);
			
			if (Character.isJavaIdentifierPart(c)) {
				buf.append(c);				
			}
		}
		
		return buf.toString();
	}


	private static Logger logger() {
		return DefaultTableMapper.logger;
	}
}

    
