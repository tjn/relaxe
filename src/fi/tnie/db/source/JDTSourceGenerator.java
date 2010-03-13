/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ITrackedNodePosition;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditVisitor;
import org.eclipse.text.edits.UndoEdit;

import fi.tnie.db.DefaultTableMapper;
import fi.tnie.db.Identifiable;
import fi.tnie.db.PersistentEntity;
import fi.tnie.db.QueryException;
import fi.tnie.db.TableMapper;
import fi.tnie.db.TableMapper.Part;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.CatalogMap;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.impl.pg.PGEnvironment;
import fi.tnie.db.meta.util.Tool;
import fi.tnie.util.io.IOHelper;

public class JDTSourceGenerator
	extends Tool {
	
	private static Logger logger = Logger.getLogger(JDTSourceGenerator.class);
		
	public static final String KEY_SOURCE_ROOT_DIR = "root-dir";
		
	private Map<Class<?>, Class<?>> wrapperMap;
	
	public static void main(String[] args) {
		
		try {						
			new JDTSourceGenerator().run(args);			
		}			
		catch (Exception e) {
			logger().error(e.getMessage(), e);
		}
	}

	public static Logger logger() {
		return JDTSourceGenerator.logger;
	}
	
	@Override
	public void run(Connection c, Properties config) 
		throws QueryException, IOException {
		
		try {
			String pkg = config.getProperty("package");
			
			String r = config.getProperty("root-dir");
			File root = (r == null) ? new File(".") : new File(r);
							
			if (!root.isDirectory()) {
				throw new IllegalArgumentException("No root directory: " + root.getAbsolutePath());
			}		
			
			TableMapper tm = new DefaultTableMapper(pkg); 
					
			PGEnvironment env = new PGEnvironment();
			CatalogFactory cf = env.catalogFactory();
//			DatabaseMetaData meta = c.getMetaData();
			
			CatalogMap cm = cf.createAll(c);
			
//			Catalog catalog = cf.create(meta, c.getCatalog());
									
			for (Catalog cat : cm.values()) {
				for (Schema s : cat.schemas().values()) {
					process(s, root, tm);
				}				
			}
			

		}
		catch (SQLException e) {
			throw new QueryException(e.getMessage(), e);
		} 
		catch (MalformedTreeException e) {
		    throw new QueryException(e.getMessage(), e);            
        }
		catch (BadLocationException e) {
		    throw new QueryException(e.getMessage(), e);
        }
		
	}

	private void process(Schema s, final File root, final TableMapper tm) 
		throws IOException, MalformedTreeException, BadLocationException {
		
		for (BaseTable t : s.baseTables().values()) {
//			Map<Part, JavaType> types = tm.entityTypeMap(t);
			
			{			    
    			JavaType intf = tm.entityType(t, TableMapper.Part.INTERFACE);
    			
    			if (intf != null) {
    				CharSequence source = generateInterface(t, intf, tm);
    				write(root, intf, source);
    			}
			}
			
			{
                JavaType at = tm.entityType(t, TableMapper.Part.ABSTRACT);
                
                if (at != null) {
                    CharSequence source = generateAbstract(t, at, tm);                                        
                    write(root, at, source);
                }
			}
            
			{
                JavaType ht = tm.entityType(t, TableMapper.Part.HOOK);
                
                if (ht != null) {
                    File src = getSourceFile(root, ht);
                    
                    // hook is special: it is only written when it does not exist
                    
                    if (!src.exists()) {                    
                        CharSequence source = generateHook(t, ht, tm);
                        write(root, ht, source);
                    }
                }
			}
			
			JavaType impl = tm.entityType(t, TableMapper.Part.IMPLEMENTATION);
			
			if (impl != null) {
				CharSequence source = generateImplementation(t, impl, tm);
				write(root, impl, source);	
			}
		}						
	}	
	
	private File getSourceFile(File root, JavaType type) 
	    throws IOException {
	    File pd = packageDir(root, type.getPackageName());
	    return getSourceFile(pd, type.getUnqualifiedName());
	}
	
	private void write(File root, JavaType type, CharSequence source) 
		throws IOException {						
		IOHelper.write(source, getSourceFile(root, type));		
	}

	public CharSequence generateInterface(BaseTable t, JavaType mt, TableMapper tm) 
	    throws MalformedTreeException, BadLocationException {
	    
	    Document d = new Document();
	    ASTParser p = ASTParser.newParser(AST.JLS3);
	    p.setSource(new char[] {});
	    	    	    	    
	    final CompilationUnit cu = (CompilationUnit) p.createAST(null);
	    cu.recordModifications();
	    AST ast = cu.getAST();
	    
	    ASTRewrite rewriter = ASTRewrite.create(ast);
	    	    	    	    
	    // package
	    {  
    	    PackageDeclaration pd = ast.newPackageDeclaration();
    	    pd.setName(ast.newName(mt.getPackageName()));
    	    cu.setPackage(pd);
	    }
	    
	    // imports	    
	    imp(cu, PersistentEntity.class);
	    imp(cu, Identifiable.class);	    
	    
	    TypeDeclaration td = ast.newTypeDeclaration();
	    td.setName(ast.newSimpleName(mt.getUnqualifiedName()));
	        	        	    	    	    
	    td.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));
	    td.setInterface(true);
	    
	    final String uname = mt.getUnqualifiedName();
	    
	    ParameterizedType it = ast.newParameterizedType(ast.newSimpleType(ast.newName(PersistentEntity.class.getName())));
	    List typeArgs = it.typeArguments();
	    
	    add(ast, typeArgs, getAttributeType(uname));	    
	    add(ast, typeArgs, getReferenceType(uname));
	    add(ast, typeArgs, getQueryType(uname));	    
	    add(ast, typeArgs, mt.getUnqualifiedName());
	    	    	    
	    td.superInterfaceTypes().add(it);
	    	    	    	    
	    cu.types().add(td);
	    	    	    
	    // attribute name type:	    
	    {
	        EnumDeclaration ad = ast.newEnumDeclaration();      
	        ad.setName(ast.newSimpleName(getAttributeType()));  
	        add(ast, ad.superInterfaceTypes(), Identifiable.class);
	        
	        
	        
	        td.bodyDeclarations().add(ad);
	    }
	     
        {
            EnumDeclaration ad = ast.newEnumDeclaration();      
            ad.setName(ast.newSimpleName(getReferenceType()));  
            add(ast, ad.superInterfaceTypes(), Identifiable.class);
            
            
            implementIdentifiable(p, ad);
            
            td.bodyDeclarations().add(ad);
        }	    
	    
        {
            EnumDeclaration ad = ast.newEnumDeclaration();      
            ad.setName(ast.newSimpleName(getQueryType()));  
            add(ast, ad.superInterfaceTypes(), Identifiable.class);
            
            td.bodyDeclarations().add(ad);
        }
                
	    TextEdit edits = cu.rewrite(d, null);	    
	    UndoEdit ue = edits.apply(d);
	    
	    int len = d.getLength();
	    
//	    cu.rewrite(d, Collections.emptyMap());	    
	    String source = d.get();
	    
	    
	    
//		StringWriter sw = new StringWriter();
//		PrintWriter p = new PrintWriter(sw);
//		
//		StringBuffer cb = new StringBuffer();
		
//		appendPackageName(mt, cb);
//				
//		a(cb, "import fi.tnie.db.PersistentEntity;", 1);
//		a(cb, "import fi.tnie.db.Identifiable;", 2);
//		
//		p.print(cb);
//		
//		final String uname = mt.getUnqualifiedName();
//		
//		p.print("public interface ");
//		p.println(uname);		
//		p.print("\textends PersistentEntity<");		
//		p.print(getAttributeType(uname));
//		p.print(", ");
//		p.print(getReferenceType(uname));
//		p.print(", ");
//		p.print(getQueryType(uname));
//		p.print(", ");
//		p.print(uname);
//		p.println(">");
//		p.println("{");
//		p.println();
//		
//		p.print(interfaceMembers(t, uname, tm));		
//		
//		p.println("\n}\n");				
//		p.close();
//		
		logger().debug("interface: " + source);
//		
//		return sw.toString();
	    
	    return source;
	}	
	
	private void implementIdentifiable(ASTParser p, EnumDeclaration ad) {

	    AST ast = ad.getAST();
	    
	    VariableDeclarationFragment vdf = ast.newVariableDeclarationFragment();	    
	    vdf.setName(ast.newSimpleName("identifier"));
	    FieldDeclaration fd = ast.newFieldDeclaration(vdf);	    
	    fd.setType(newSimpleType(ast, String.class));
	    add(ast, fd.modifiers(), Modifier.PRIVATE);	    
	    ad.bodyDeclarations().add(fd);
	    
	    final String idvar = "identifier";	    

        MethodDeclaration md = ast.newMethodDeclaration();     
        md.setName(ast.newSimpleName(idvar));
        md.setReturnType2(newSimpleType(ast, String.class));        
        add(ast, md.modifiers(), Modifier.PUBLIC);
        
        Block mb = ast.newBlock();        
        md.setBody(mb);
        
        // produce programmatically something like:
        // return (identifier == null) ? name() : identifier;
        
                       
        ReturnStatement rs = ast.newReturnStatement();
        mb.statements().add(rs);
        ConditionalExpression ce = ast.newConditionalExpression();
        rs.setExpression(ce);
        
        InfixExpression eq = ast.newInfixExpression();
        ce.setExpression(parenthesized(eq));        
        eq.setOperator(Operator.EQUALS);
        eq.setLeftOperand(ast.newSimpleName(idvar));
        eq.setRightOperand(ast.newNullLiteral());
        
        MethodInvocation ni = ast.newMethodInvocation();
        ni.setName(ast.newSimpleName("name"));
        ce.setThenExpression(ni);        
        ce.setElseExpression(ast.newSimpleName(idvar));        
                
        ad.bodyDeclarations().add(md);
    }
	
	private ParenthesizedExpression parenthesized(Expression e) {
	    ParenthesizedExpression pe = e.getAST().newParenthesizedExpression();
	    pe.setExpression(e);
	    return pe;
	}		
	
	private void add(AST ast, List mods, int modifier) {
	    mods.addAll(ast.newModifiers(modifier));
	}

    private void add(AST ast, List dest, String qualifiedName) {
	    dest.add(newSimpleType(ast, qualifiedName));
	}
	
	private void add(AST ast, List dest, Class clazz) {
	    dest.add(newSimpleType(ast, clazz));
	}
	
	private SimpleType newSimpleType(AST ast, Class clazz) {
	    return newSimpleType(ast, clazz.getName());
	}

    private SimpleType newSimpleType(AST ast, String qualifiedName) {
        return ast.newSimpleType(ast.newName(qualifiedName));
    }
	
	public CharSequence generateAbstract(BaseTable t, JavaType mt, TableMapper tm) {    
	    
	    return "// TODO";
	}
	
	
	private void imp(CompilationUnit cu, Class clazz) {
	    AST ast = cu.getAST();
	    ImportDeclaration imp = ast.newImportDeclaration();
	    imp.setName(ast.newName(clazz.getName()));
	    cu.imports().add(imp);
	}
	
	public CharSequence generateHook(BaseTable t, JavaType mt, TableMapper tm) {
	        
	    StringBuffer cb = new StringBuffer();
	    
	    mt.getPackageName();
	    	    
	    return cb.toString();
	}
	
	public CharSequence generateImplementation(BaseTable t, JavaType imp, TableMapper tm) {
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
				
		JavaType ifp = tm.entityType(t, Part.INTERFACE);	
		
		String intf = null;
		
		if (ifp != null) {
			intf = ifp.getQualifiedName();	
		}
		
		String ctype = (intf == null) ? imp.getUnqualifiedName() : intf;
		
		String pkg = imp.getPackageName();
		
		if (pkg != null) {			
			w.print("package ");
			w.print(pkg);
			w.println(";");
			w.println();
		}
	
		w.println("import fi.tnie.db.DefaultPersistentEntity;");
		w.println("import fi.tnie.db.DefaultEntityMetaData;");
		w.println("import fi.tnie.db.EntityException;");
		w.println("import fi.tnie.db.EntityFactory;");
												
		w.println();
		w.println();
						
		w.print("public class ");
		w.println(imp.getUnqualifiedName());		
		w.print("\textends DefaultPersistentEntity<");
		w.print(getAttributeType(ctype));
		w.print(",");
		w.print(getReferenceType(ctype));
		w.print(",");
		w.print(getQueryType(ctype));
		w.print(",");
		w.print(ctype);		
		w.println(">");
		
		if (intf != null) {		
			w.print("\timplements ");
			w.println(intf);
		}
		
		w.println("{");		
		w.print(implMembers(t, tm));
		w.print("\n}\n");
		w.close();
				
		return sw.toString();
	}

	
	private String interfaceMembers(BaseTable t, String etype, TableMapper tm) {
		
		StringBuffer content = new StringBuffer();
		
		attrs(t, content);
		content.append("\n\n");
		
		refs(t, content);
		content.append("\n\n");		

		queries(t, content);
		content.append("\n\n");		

//		factory(etype, content);
//		content.append("\n\n");
		
		logger().debug("members: " + content);
		
		// getter & setter prototypes:
        accessors(t, content, tm, false);
        content.append("\n\n");    
		
		
		
		return content.toString();
	}

	private void accessors(BaseTable t, StringBuffer content, TableMapper tm, boolean impl) {
        Set<Identifier> fkcols = foreignKeyColumns(t);
                
        for (Column c : t.columns()) {      
            // only non-fk-columns are included in attributes.
            // fk-columns  are not intended to be set individually,
            // but atomically with ref -methods
            if (!fkcols.contains(c.getColumnName())) {
                Class<?> jt = tm.getAttributeType(t, c);
                
                if (jt != null) {                    
                    String code = formatAccessors(c, jt, impl);
                    content.append(code);
                }
            }           
        }
    }

    private String formatAccessors(Column c, Class<?> attributeType, boolean impl) {        
        final String attributeName = attr(c);        
        final String type = attributeType.getName();
        
        StringBuffer nb = new StringBuffer();
        final String n = name(c.getColumnName().getName());
                
        a(nb, "public ");
        a(nb, type);
        a(nb, " get");
        a(nb, n);
        a(nb, "()");
        
        
        if (!impl) {
            a(nb, ";", 1);
        }
        else {        
            // getter implementation
            a(nb, " {", 1);
            
            if (!attributeType.isPrimitive()) {
                a(nb, "return ");
                // call super & cast:
                expr(nb, attributeType, attributeName);            
                a(nb, ";", 1);            
            }
            else {
                Class<?> wt = wrapper(attributeType);            
                a(nb, wt.getName());
                
                a(nb, " o = ");
                // call super & cast:
                expr(nb, attributeType, attributeName);            
                a(nb, ";", 2);
                            
                a(nb, "return (o == null) ? ");
                
                // default value:
                if (attributeType.equals(Boolean.TYPE)) {
                    a(nb, " false ");
                }
                else {
                    // TODO: should we try to parse DB default here?
                    a(nb, " 0 ");
                }         
                
                a(nb, " : ");
                a(nb, "o.intValue()");   
                a(nb, ";", 1);
            }
                                    
            a(nb, "}", 2);
        }
        
        
        
        a(nb, "public void set");        
        a(nb, n);
        a(nb, "(");
        a(nb, type);
        a(nb, " ");
        a(nb, "newValue)");
        
        if (!impl) {
            a(nb, ";", 1);
        }
        else {
            a(nb, " {", 1);
            a(nb, "set(");
            a(nb, getAttributeType());
            a(nb, ".");
            a(nb, attributeName);
            a(nb, ", ");
                
            if (!attributeType.isPrimitive()) {                
                a(nb, "newValue");
            }
            else {
                Class<?> wt = wrapper(attributeType);            
                a(nb, wt.getName());
                a(nb, ".valueOf(");
                a(nb, "newValue");
                a(nb, ")");
            }        
            a(nb, ");", 1);        
            a(nb, "}", 2);
        }
        
        return nb.toString();
    }
        
    private void expr(StringBuffer sb, Class<?> attributeType, String attributeName) {        
        Class<?> castTo = attributeType.isPrimitive() ? 
                wrapper(attributeType) : 
                attributeType;
        
        sb.append("(");
        sb.append(castTo.getName());
        sb.append(") super.get(");            
        sb.append(getAttributeType());
        sb.append(".");
        sb.append(attributeName);            
        sb.append(")");        
    }


    private String name(String name) {
        int len = name.length();
        StringBuffer nb = new StringBuffer(len);
        boolean upper = true;        
        
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            
            if (c == '_') {
                upper = true;
                continue;
            }
            else {
                upper = (i == 0);
            }
            
            nb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
        
        return nb.toString();
    }

    private String implMembers(BaseTable t, TableMapper tm) {
		
		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
		
		final JavaType ctype = null; // TODO: getContainerType(parts);
		final String cn = null; // TODO: getContainerTypeName(parts);
		
		String at = getAttributeType(ctype.getUnqualifiedName());
		String rt = getReferenceType(ctype.getUnqualifiedName());
		String qt = getQueryType(ctype.getUnqualifiedName());
		
		String imp = tm.entityType(t, Part.IMPLEMENTATION).getUnqualifiedName();
		
		String metaDataType = ctype.getUnqualifiedName() + "MetaData";
						
		w.println("public static class " + metaDataType);
		w.println("	extends DefaultEntityMetaData");
		w.println("	<");
		w.println("		" + at + ", ");
		w.println("		" + rt + ", ");
		w.println("		" + qt + ", ");
		w.println("		" + cn + "> {");
		w.println("");
		
		w.println("	private static " + metaDataType + " instance = new " + metaDataType + "();");
		
		w.println("	public " + metaDataType + "() {");
		w.println("		super(" + at + ".class, " + rt + ".class,	" + qt + ".class);");
		w.println("	}");
		w.println("");
		w.println("	@Override");
		w.println("	public EntityFactory<" + at + ", " + rt + ", " + qt + ", " + cn + "> getFactory() {			");
		w.println("		return new EntityFactory<" + at + ", " + rt + ", " + qt + ", " + cn + ">() {");
		w.println("			@Override");
		w.println("			public " + cn + " newInstance()");
		w.println("						throws EntityException {");
		w.println("					return new " + imp + "();");
		w.println("				}		");
		w.println("		};");
		w.println("	}");
		w.println("}");
		w.println("");
		w.println("public " + imp + "() {");		
		w.println("}");
		w.println("");
		w.println("	@Override		");
		w.println("	public " + metaDataType + " getMetaData() {		");
		w.println("		return " + metaDataType + ".instance;");
		w.println("	}");		
		
		w.println("");
		w.println("	@Override		");
		w.println("	public " + ctype.getUnqualifiedName() + " self() {		");
		w.println("		return this;");
		w.println("	}");
		
		
	      // getter & setter prototypes:
		StringBuffer content = new StringBuffer();
        accessors(t, content, tm, true);            
        w.println(content);
				
		w.close();
		
		return sw.toString();
	}

//	private void factory(String uname, StringBuffer content) {
////		String ivfact = "factory";
////		
////		String atype = getAttributeType();
////		
////		content.append("private static EntityFactory<" + atype + ", " + uname + "> " + ivfact + ";\n\n");		
////		
////		content.append("@Override\n");		
////		
////		content.append(
////		"public EntityFactory<" + atype + ", " + uname + "> getFactory() {\n" +
////		"  if (factory == null) {\n" +
////		"    factory = new DefaultEntityFactory<" + atype + ", " + uname + ">() {\n" +
////		"      @Override\n" +
////		"      public " + uname + " newInstance() {\n" +
////		"        return new " + uname + "();\n" +
////		"      }\n" +
////		"    };\n" +
////		"  }\n\n" +
////		"  return factory;\n" +
////		"}\n\n");
////			
////		
//	}


	private void attrs(BaseTable t, StringBuffer content) {								
//		content.append("public enum ");
//		content.append(getAttributeType());
//		content.append(" {\n");
				
		List<String> elements = new ArrayList<String>();
		
		Set<Identifier> fkcols = foreignKeyColumns(t);
				
		for (Column c : t.columns()) {		
			// only non-fk-columns are included in attributes.
			// fk-columns  are not intended to be set individually,
			// but atomically with ref -method
			if (!fkcols.contains(c.getColumnName())) {
				elements.add(attr(c));
			}			
		}
		
		content.append(enumMember(getAttributeType(), elements));		
	}
	
	private void refs(BaseTable t, StringBuffer content) {								
		List<String> elements = new ArrayList<String>();	
			
		for (ForeignKey fk : t.foreignKeys().values()) {		
			elements.add(format(fk));
		}
		
		content.append(enumMember(getReferenceType(), elements));		
	}	
	
	private String format(ForeignKey fk) {		
		final String kn = fk.getUnqualifiedName().getName();
		String t = fk.getReferencing().getUnqualifiedName().getName().toUpperCase();
		
		String p = "^(FK_)?(" + Pattern.quote(t) + "_)";
		
		logger().debug("input {" + kn.toUpperCase() + "}");
		logger().debug("p {" + p + "}");
		
		String n = kn.toUpperCase().replaceFirst(p, "");
		
		String expr;
		
		if (n.equals(kn)) {
			expr = n;
		}
		else {
			StringBuffer buf = new StringBuffer(n);
			buf.append("(");
			buf.append('"');
			buf.append(kn);
			buf.append('"');
			buf.append(")");
			
			expr = buf.toString();
		}
		
		return expr;
	}
	
	
	private void queries(BaseTable t, StringBuffer content) {								
		List<String> elements = new ArrayList<String>();	
			
		// TODO:
//		for (ForeignKey fk : t.foreignKeys().values()) {		
//			elements.add(fk.getUnqualifiedName().getName());
//		}
		
		content.append(enumMember(getQueryType(), elements));		
	}
	
	
	
	private Set<Identifier> foreignKeyColumns(BaseTable t) {
		Comparator<Identifier> icmp = t.getSchema().getCatalog().getEnvironment().identifierComparator();
		Set<Identifier> cs = new TreeSet<Identifier>(icmp);
		
		logger().debug("table: " + t.getQualifiedName());
				
		for (ForeignKey fk : t.foreignKeys().values()) {
			for (Column c : fk.columns().keySet()) {
				cs.add(c.getUnqualifiedName());
			}			
		}
		
		return cs;
	}


	private String attr(Column c) {
		ColumnName n = c.getColumnName();
		
		String attr = n.getName().toUpperCase();		
		
		if (!n.isOrdinary()) {
			attr = attr.replace(' ', '_');
		}
		
		return attr;
	}

	private void indent(int indentLevel, StringBuffer dest) {
		String indent = "  ";
		
		for (int level = 0; level < indentLevel; level++) {
			dest.append(indent);			
		}
	}

	private File getSourceFile(File pd, String etype) {		
		return new File(pd, etype + ".java");
	}

	private File packageDir(File root, String pkg) 
		throws IOException {
		if (pkg == null) {
			return root;
		}
						
		String[] elems = pkg.split(Pattern.quote("."));
		
		StringBuffer path = new StringBuffer(elems[0]); 
		
		for (int i = 1; i < elems.length; i++) {
			path.append(File.separatorChar);	
			path.append(elems[i]);
		}
		
		File pd = new File(root, path.toString());
		
		pd.mkdirs();
		
		if (!pd.isDirectory()) {
			throw new IOException("unable to create directory: " + pd.getPath());			
		}
			
		return pd;
	}

//	private String qualifiedName(String pkg, String uname) {
//		return pkg == null || pkg.equals("") ? uname : pkg + "." + uname;
//	}
	
	public String getAttributeType(String uname) {
		return uname + "." + getAttributeType();
	}
	
	public String getReferenceType(String uname) {
		return uname + "." + getReferenceType();
	}
	
	public String getQueryType(String uname) {
		return uname + "." + getQueryType();
	}
	
	public String getAttributeType() {
		return "Attribute";		
	}
	
	public String getReferenceType() {
		return "Reference";		
	}

	
	public String getQueryType() {
		return "Query";		
	}
	
	private <E> String enumMember(String uname, Iterable<E> elements) {

		StringWriter sw = new StringWriter();
		PrintWriter w = new PrintWriter(sw);
						
		w.print("public enum ");
		w.print(uname);
		w.print(" implements Identifiable ");		
		w.println(" {");
		
		for (Object e : elements) {
			w.print("\t");
			w.print(e.toString());
			w.println(",");
		}		
		
		w.println(";");
		w.println();
				 		
		w.println("\tprivate String identifier;");
		w.println();
		w.println("\t" +  uname + "() {}");
		w.println();
		w.println("\t" + uname + "(String identifier) {");
		w.println("\tthis.identifier = identifier;");
		w.println("\t}");
		w.println();
						
		w.println("\t@Override");
		w.println("\tpublic String identifier() {");		
		w.println("\t\treturn (identifier == null) ? name() : identifier;");
		w.println("\t}");
				
		w.println("}");		
		w.println();
		w.close();
		
		return sw.toString();
	}


	private String getContainerTypeName(Map<Part, JavaType> types) {
		JavaType ifp = types.get(Part.INTERFACE);
		JavaType imp = types.get(Part.IMPLEMENTATION);	
		
		String intf = null;
		
		if (ifp != null) {
			intf = ifp.getQualifiedName();	
		}
		
		return (intf == null) ? imp.getUnqualifiedName() : intf;		
	}
	
	private JavaType getContainerType(Map<Part, JavaType> types) {
		JavaType intf = types.get(Part.INTERFACE);
		JavaType impl = types.get(Part.IMPLEMENTATION);	
	
		return (intf == null) ? impl : intf;		
	}

	
	private Class<?> wrapper(Class<?> primitiveType) {
	    if (primitiveType == null) {
            throw new NullPointerException();
        }
	    
	    if (!primitiveType.isPrimitive()) {
	        throw new IllegalArgumentException("primitive type expected");
	    }
	    
	    return getWrapperMap().get(primitiveType);
	}

	
	public Map<Class<?>, Class<?>> getWrapperMap() {
        if (this.wrapperMap == null) {
            Map<Class<?>, Class<?>> wm = this.wrapperMap = new HashMap<Class<?>, Class<?>>();
            
            wm.put(Boolean.TYPE, Boolean.class);
            wm.put(Byte.TYPE, Byte.class);
            wm.put(Character.TYPE, Character.class);
            wm.put(Double.TYPE, Double.class);
            wm.put(Float.TYPE, Float.class);            
            wm.put(Integer.TYPE, Integer.class);
            wm.put(Long.TYPE, Long.class);
            wm.put(Short.TYPE, Short.class);
        }
        
        return this.wrapperMap;
    }
	
	private void a(StringBuffer dest, String s) {
	    a(dest, s, 0);
	}
			
	private void a(StringBuffer dest, String s, int eols) {
	    dest.append(s);
	    
	    String sep = System.getProperty("line.separator");
	    
	    for (int i = 0; i < eols; i++) {
            dest.append(sep);
        }
	}
	
	
	private void appendPackageName(JavaType t, StringBuffer dest) {
	    String pkg = t.getPackageName();
	    
	    if (pkg != null) {          
	        a(dest, "package ");            
	        a(dest, pkg);
	        a(dest, ";", 2);
	    }	    
	}
}
