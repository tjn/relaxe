/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.meta.BaseTable;

public class DefaultTableMapper
	implements TableMapper {
	
	private String rootPackage;

	@Override
	public Map<Part, Type> entityMetaDataType(BaseTable table) {
		StringBuffer n = new StringBuffer();
		
		String p = getRootPackage();
		
		if (p != null) {
			n.append(p);
			n.append(".");
		}
		
		String s = table.getSchema().getUnqualifiedName().getName();

		if (s.equals("public")) {
			s = "pub";
		}
		
		n.append(s);		
		
		String u = table.getUnqualifiedName().getName();		
		u = translate(u);
		
		Type intf = new Type(n.toString(), u);		
		Type impl = new Type(n.toString(), u + "Impl");
	
		EnumMap<Part, Type> types = new EnumMap<Part, Type>(Part.class);
		types.put(Part.INTERFACE, intf);
		types.put(Part.IMPLEMENTATION, impl);
		return types;
	}

	private void setRootPackage(String rootPackage) {
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

	
	

}
