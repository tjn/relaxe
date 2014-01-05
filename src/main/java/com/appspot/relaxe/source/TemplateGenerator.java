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
package com.appspot.relaxe.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityContext;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.expr.ddl.types.SQLTypeDefinition;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.SchemaElementMap;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public class TemplateGenerator {

//	private static Logger logger = LoggerFactory.getLogger(TemplateGenerator.class);
		
	private File templateDir;
	private EntityContext context;
	
	public TemplateGenerator(File templateDir, EntityContext context) {
		super();
		setTemplateDir(templateDir);
		setContext(context);
	}

	public Properties run(Catalog cat, NamingPolicy np) throws IOException, XMLStreamException, TransformerException {
		Properties generated = new Properties();
		
		Collection<? extends Schema> sc = cat.schemas().values();		
		Map<Schema, File> dm = mkdirs(np, sc);				
		
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
		
		EntityContext ctx = getContext();
				
		for (Schema s : sc) {
			SchemaElementMap<? extends BaseTable> tem = s.baseTables();			
			Collection<? extends BaseTable> tables = tem.values();
			
			File sd = dm.get(s);
						
			for (BaseTable t : tables) {
				EntityMetaData<?, ?, ?, ?, ?, ?, ?> meta = ctx.getMetaData(t);
				
				
				String tn = np.getTemplate(t);				
				File tf = new File(sd, tn);
				
//				if (!tf.exists()) {					
					write(meta, np, tf, xof);					
					generated.put(t.getQualifiedName(), tf.getPath());
//				}				
			}
		}
		
		
		return generated;
	}
	
	protected 
	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,	
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,	 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	void generateTemplate(M meta, XMLStreamWriter w, NamingPolicy np) throws XMLStreamException {
		BaseTable t = meta.getBaseTable();
		
		w.writeStartDocument();
		w.writeStartElement("div");
		
		w.writeStartElement("table");
		writeClass(w, np.getTableStyle(t));
		
		generateAttributeRows(meta, w, np);
		
		generateReferenceRows(meta, w, np);
		
		w.writeEndElement();
		
		w.writeEndElement();
		w.writeEndDocument();
		
	}

	private 
	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,	
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,	 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	void generateAttributeRows(M meta, XMLStreamWriter w, NamingPolicy np) throws XMLStreamException {
		
		for(A a : meta.attributes()) {
			Column c = meta.getColumn(a);
			BaseTable t = meta.getBaseTable();
			generateAttribute(t, c, np, w);			
		}
	}
	
	private 
	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,	
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,	 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>
	void generateReferenceRows(M meta, XMLStreamWriter w, NamingPolicy np) throws XMLStreamException {		
		
		Set<R> rs = meta.relationships();
		
		for (R r : rs) {
			BaseTable t = meta.getBaseTable();
			ForeignKey fk = meta.getForeignKey(r);			
			generateReference(t, fk, np, w);			
		}		
	}
	
//	private String styles(String[] sa, String ... styles) {
//		
//	}
	

	private void generateReference(BaseTable t, ForeignKey fk, NamingPolicy np, XMLStreamWriter w) throws XMLStreamException {
		
		w.writeComment("foreign key: " + fk.getQualifiedName());
		
		BaseTable rt = fk.getReferencing();
		
		Collection<Column> cl = rt.getColumnMap().values();
		Column tc = null;
		
		for (Column c : cl) {		
			DataType ct = c.getDataType();
			
			if (SQLTypeDefinition.isTextType(ct.getDataType())) {
				tc = c;
				break;
			}
		}

		if (tc == null) {
			w.writeComment("no text columns available");
			return;
		}		
				
		String s = styles(
				np.getAttributeStyle(t)
		);
		
		w.writeStartElement("tr");
		writeClass(w, s);
		
		w.writeStartElement("td");
		writeClass(w, s);
		
		w.writeStartElement("div");
		String ls = np.getLabelIdentifier(fk);
		w.writeAttribute("id", ls);
		w.writeAttribute("class", ls);
		
		w.writeEndElement(); // div
		
		w.writeCharacters(np.getLabelText(fk));		
		
		w.writeEndElement(); // td
		
		// value cell
			
		w.writeStartElement("td");		
		writeClass(w, s);
		
		w.writeStartElement("div");		
		String id = np.getValueIdentifier(fk, tc);
		w.writeAttribute("id", id);
		w.writeEndElement();
		
		w.writeStartElement("span");
		w.writeAttribute("id", np.getSelectorIdentifier(fk, id));
		w.writeEndElement();
		w.writeStartElement("span");
		w.writeAttribute("id", np.getResetIdentifier(fk, id));
		w.writeEndElement();
		
		w.writeEndElement(); //td
		
		w.writeEndElement(); //tr
		
	}

	private String styles(String ... styles) {
		
		StringBuilder sb = new StringBuilder();
		
		for (String s : styles) {
			if (s != null) {
				sb.append(s);
				sb.append(' ');
			}
		}
		
		return sb.toString();
	}

	protected void generateAttribute(BaseTable t, Column c, NamingPolicy np, XMLStreamWriter w) throws XMLStreamException {
				
		DataType type = c.getDataType();
		
		String s = styles(
				np.getAttributeStyle(t), 
				np.getAttributeStyle(c), 
				np.getAttributeTypeStyle(type),
				np.getAttributeSizeStyle(type)
		);
		
		w.writeStartElement("tr");
		writeClass(w, s);
		
		w.writeStartElement("td");
		writeClass(w, s);
		w.writeStartElement("div");
		String ls = np.getLabelIdentifier(t, c);
		w.writeAttribute("id", ls);
		w.writeAttribute("class", ls);
		
		w.writeCharacters(np.getLabelText(t, c));
		
		w.writeEndElement();
		w.writeEndElement();
			
		w.writeStartElement("td");		
		writeClass(w, s);
		w.writeStartElement("div");
		String vs = np.getValueIdentifier(t, c);
		w.writeAttribute("id", vs);
		w.writeAttribute("class", vs);		
		w.writeEndElement();
		w.writeEndElement();
		
		w.writeEndElement();
	}
	private void writeClass(XMLStreamWriter w, String s) throws XMLStreamException {

		if ((s != null) && (!s.equals(""))) {
			w.writeAttribute("class", s);
		}		
	}

	private 
	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M>,	
		E extends Entity<A, R, T, E, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, H, M, F>,	 
		M extends EntityMetaData<A, R, T, E, H, F, M>
	>		
	void write(EntityMetaData<A, R, T, E, H, F, M> meta, NamingPolicy np, File tf, XMLOutputFactory xof) throws IOException, XMLStreamException, TransformerException {
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter w = xof.createXMLStreamWriter(os);
		
		generateTemplate(meta.self(), w, np);
		w.flush();
		w.close();		
		
		
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		Source source = new StreamSource(new ByteArrayInputStream(os.toByteArray()));		
		FileOutputStream fos = new FileOutputStream(tf);		
		Result result = new StreamResult(fos);

		transformer.transform(source, result);
	}

	private Map<Schema, File> mkdirs(NamingPolicy np, Collection<? extends Schema> sc)
			throws IOException {
	
		Map<Schema, File> dirMap = new HashMap<Schema, File>();
		
		for (Schema s : sc) {
			if (s.baseTables().keySet().isEmpty()) {
				continue;
			}
			
			
			String sp = np.getTemplateDir(s);						
			File sd = new File(getTemplateDir(), sp);
			
			if (!sd.exists()) {
				sd.mkdirs();
			}
			
			if (sd.isDirectory()) {
				dirMap.put(s, sd);
			}
			else {
				throw new IOException("can not create template directory: " + sd.getAbsolutePath());
			}			
		}
		
		return dirMap;
	}

	public File getTemplateDir() {
		return templateDir;
	}

	private void setTemplateDir(File templateDir) {
		this.templateDir = templateDir;
	}

	private EntityContext getContext() {
		return context;
	}

	private void setContext(EntityContext context) {
		this.context = context;
	}
	
	
	
	
	
}
