/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.ddl.SQLType;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.SchemaElementMap;

public class TemplateGenerator {

	private static Logger logger = Logger.getLogger(TemplateGenerator.class);
		
	private File templateDir;
	
	public TemplateGenerator(File templateDir) {
		super();
		setTemplateDir(templateDir);
	}

	public Properties run(Catalog cat, NamingPolicy np) throws IOException, XMLStreamException, TransformerException {
		Properties generated = new Properties();
		
		Collection<? extends Schema> sc = cat.schemas().values();		
		Map<Schema, File> dm = mkdirs(np, sc);				
		
		XMLOutputFactory xof = XMLOutputFactory.newInstance();
				
		for (Schema s : sc) {
			SchemaElementMap<? extends BaseTable> tem = s.baseTables();			
			Collection<? extends BaseTable> tables = tem.values();
			
			File sd = dm.get(s);
						
			for (BaseTable t : tables) {
				String tn = np.getTemplate(t);				
				File tf = new File(sd, tn);
				
//				if (!tf.exists()) {					
					write(t, np, tf, xof);					
					generated.put(t.getQualifiedName(), tf.getPath());
//				}				
			}
		}
		
		
		return generated;
	}
	
	protected void generateTemplate(BaseTable t, XMLStreamWriter w, NamingPolicy np) throws XMLStreamException {
		w.writeStartDocument();
		w.writeStartElement("div");
		
		w.writeStartElement("table");
		writeClass(w, np.getTableStyle(t));
		
		generateAttributeRows(t, w, np);
		
		generateReferenceRows(t, w, np);
		
		w.writeEndElement();
		
		w.writeEndElement();
		w.writeEndDocument();
		
	}

	private void generateAttributeRows(BaseTable t, XMLStreamWriter w, NamingPolicy np) throws XMLStreamException {	
		for(Column c : t.columnMap().values()) {						
			generateAttribute(t, c, np, w);			
		}
	}
	
	private void generateReferenceRows(BaseTable t, XMLStreamWriter w, NamingPolicy np) throws XMLStreamException {
						
		SchemaElementMap<ForeignKey> keys = t.foreignKeys();
		
		for (ForeignKey fk : keys.values()) {
			generateReference(t, fk, np, w);			
		}
		
		
	}
	
//	private String styles(String[] sa, String ... styles) {
//		
//	}
	

	private void generateReference(BaseTable t, ForeignKey fk, NamingPolicy np, XMLStreamWriter w) throws XMLStreamException {
		
		w.writeComment("foreign key: " + fk.getQualifiedName());
		
		BaseTable rt = fk.getReferencing();
		
		List<? extends Column> cl = rt.columns();
		Column tc = null;
		
		for (Column c : cl) {		
			DataType ct = c.getDataType();
			
			if (SQLType.isTextType(ct.getDataType())) {
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

	private void write(BaseTable t, NamingPolicy np, File tf, XMLOutputFactory xof) throws IOException, XMLStreamException, TransformerException {
		
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLStreamWriter w = xof.createXMLStreamWriter(os);
		generateTemplate(t, w, np);
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
	
	
	
	
	
}
