package converter;

import info.aduna.xml.XMLUtil;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.sail.SailConnection;
import org.openrdf.sail.memory.MemoryStore;

import util.DataWriter;
import util.FunctionLib;
import util.HandleNS;
import util.StandardOutputs;

/**
 * Main class of the RDF2DataRSSConverter
 * it is based on the Sesame 2.2.4 framework which is availible
 * under http://www.openrdf.org
 * 
 * It converts RDF data to Yahoo SearchMonkey specific DataRSS
 * This class uses a BufferedWriter for outputting the DataRSS code
 * 
 * For any bug reporting please don't hesitate to contact me by email
 * 
 * @version 1.0b
 * @author Thomas Irmscher (thomas.irmscher@unibw.de)
 */

public class Converter {
	
	private RepositoryConnection con;
	
	private boolean redund=false;
	
	private HandleNS ns;
	
	private DataWriter writer;
	
	private String base_url;
	
	private FunctionLib flib;
	
	private HashSet<String> doneSubj;
	
	private static boolean vcardwarning=false;
	
	public Converter(BufferedWriter bw) {
		writer = new DataWriter(bw);
		ns = new HandleNS();
		base_url = "http://purl.org/goodrelations/v1#";
		flib = new FunctionLib();
		doneSubj = new HashSet<String>();
	}
	
	public Converter(BufferedWriter bw, String base_url) {
		this(bw);
		this.base_url = base_url;
	}
	
	public void close() throws RepositoryException {
		Repository rep = con.getRepository();
		con.clear();
		con.clearNamespaces();
		con.close();
		rep.shutDown();
	}
	
	public void convert(String uri, String format) throws Exception {
		
		MemoryStore ms = new MemoryStore();
		ms.setPersist(false);
		SailRepository rep = new SailRepository(ms);
		
		rep.initialize();
		con = rep.getConnection();

		RDFFormat rf = null;
		if(format.equals("xml")) {
			rf = RDFFormat.RDFXML;
		} else if(format.equals("n3")) {
			rf = RDFFormat.N3;
		} else {
			throw new Exception("no format specified!");
		}
		
		con.add(new URL(uri), base_url, rf);
		
		// save namespaces in repository to handler
		ns.retrieveNS(con);
		
		StandardOutputs.insertHeader(writer, base_url, flib.getCurrentDate(), ns);
		init();
		StandardOutputs.insertFooter(writer);
	}
	
	public void convert(InputStream in, String format) throws Exception {
		MemoryStore ms = new MemoryStore();
		ms.setPersist(false);
		Repository rep = new SailRepository(ms);
		
		rep.initialize();
		con = rep.getConnection();

		RDFFormat rf = null;
		if(format.equals("xml")) {
			rf = RDFFormat.RDFXML;
		} else if(format.equals("n3")) {
			rf = RDFFormat.N3;
		} else {
			throw new Exception("no format specified!");
		}
		
		con.add(in, base_url, rf);
		
		// save namespaces in repository to handler
		ns.retrieveNS(con);
		
		StandardOutputs.insertHeader(writer, base_url, flib.getCurrentDate(), ns);
		init();
		StandardOutputs.insertFooter(writer);
		
	}
	
	public void init() throws Exception {

		HashSet<Resource> subjs = new HashSet<Resource>();
		
		RepositoryResult<Statement> rit = con.getStatements(null, null, null, false);
		
		while(rit.hasNext()) {
			Statement stmt = rit.next();
			Value obj = stmt.getObject();
			int splitter = XMLUtil.findURISplitIndex(obj.stringValue());
			if(splitter!=-1) {
				if(ns.containsNS(obj.stringValue().substring(0,splitter))) subjs.add(stmt.getSubject());
			}
		}
		
		rit.close();
		
		Iterator<Resource> it = subjs.iterator();
		
		while(it.hasNext()) {
			Resource subj = it.next();
			showItem(subj,null,null,0);
		}
	}
	
	public void showItem(Resource subj, URI pred, ArrayList<Value> oldlist, int depth) throws Exception {

		boolean go=false;
		
		boolean cont = doneSubj.contains(subj.stringValue());
		
		// avoid to display type items again
		if(oldlist!=null && !oldlist.contains(subj)) {
			go = true;
		} else if(oldlist==null) {
			go = true;
		}

		if(go) {
		
			// generate a list of typeof elements
			RepositoryResult<Statement> sts = con.getStatements(subj, RDF.TYPE, null, false);
			List<Statement> li = sts.asList();
			ArrayList<Value> objlist = new ArrayList<Value>();
	
			for(int i=0;i<li.size();i++) {
				// the NS has to be known
				Value obj = li.get(i).getObject();
				int ns_split = XMLUtil.findURISplitIndex(obj.stringValue());
				if(ns.containsNS(obj.stringValue().substring(0, ns_split))) objlist.add(obj);
			}
			sts.close();
			
			if(depth>5) return;
			
			if(depth==0) {
				
				URI type = null;
				
				RepositoryResult<Statement> typeStmts = con.getStatements(subj, null, null, false);
				for (Statement stmt: typeStmts.asList()) {
					if (stmt.getObject() instanceof URI) {
						type = (URI) stmt.getObject();
						break;
					}
				}
				
				typeStmts.close();
	
				if(type!=null) {
					printTab(depth);
					
					String addbe = getName(type);
					if(addbe.equals("gr:BusinessEntity")) {
						addbe = "dc:subject";
					}
					
					if (subj instanceof URI) {
							writer.write("<y:item rel=\"" + addbe + "\" resource=\"" + subj + "\"");
					} else {
							writer.write("<y:item rel=\"" + addbe + "\"");
					}
					if(getChildrenCount(subj)==0 || (!redund && cont)) writer.write("/");
					writer.writeln(">");
				}
			} else {
				printTab(depth);
					
				if (subj instanceof URI) {
						writer.write("<y:item rel=\"" + getName(pred) + "\" resource=\"" + subj + "\"");
				} else {
						writer.write("<y:item rel=\"" + getName(pred) + "\"");
				}
				if(getChildrenCount(subj)==0 || (!redund && cont)) writer.write("/");
				writer.writeln(">");
			}
			
			// iterate through the values that belong to the typeof attribute
			if(objlist.size()>0 && (redund || !doneSubj.contains(subj.stringValue()))) {
				
				String addbe = getName(objlist.get(0));
				
				for(int i=1; i<objlist.size();i++) {
					addbe += " "+getName(objlist.get(i));
				}
				if(addbe.equals("gr:BusinessEntity")) {
					addbe += " commerce:Business extraction:Product";
				}
				
				printTab(depth);
				writer.write("<y:type typeof=\""+addbe+"\"");
				if(subj instanceof URI) writer.write(" resource=\""+subj+"\"");
				writer.writeln(">");
			}
			
			if(redund || !cont) {
			
				// iterate through the properties
				RepositoryResult<Statement> rit = con.getStatements(subj, null, null, false);
				
				while(rit.hasNext()) {
					Statement stmt = rit.next();
					
					Value obj = stmt.getObject();
					if(obj instanceof Literal) {
		
						if(ns.containsNS(stmt.getPredicate().getNamespace())) {
							
							printTab(depth+1);
							
							writer.write("<y:meta property=\"" + getName(stmt.getPredicate()) + "\"");
							
							/*// check for language
							String lang = ((Literal)obj).getLanguage();
							if (lang != null && !lang.equals("")) {
								writer.write(" xml:lang=\"" + lang + "\"");
							}*/
							
							// check for datatype
							URI datatype = ((Literal)obj).getDatatype();
							if(datatype!=null && !datatype.equals("")) {
								writer.write(" datatype=\""+getName(datatype)+"\"");
							}
							
							writer.write(">");
							writer.write(((Literal)obj).getLabel());
							writer.writeln("</y:meta>");
						
						}
						
					} else if(obj instanceof URI) {
						showItem((URI)obj,stmt.getPredicate(), objlist, depth+1);
					} else {
						showItem((BNode)obj,stmt.getPredicate(), objlist, depth+1);
					}
				}
				
				rit.close();
			}
	
			if(getChildrenCount(subj)>0 && (redund || !cont)) {
				
				if(objlist.size()>0) {
					printTab(depth);
					writer.writeln("</y:type>");
				}
				
				printTab(depth);			
				writer.writeln("</y:item>");
			}
		
			// save the subj to the set to avoid redundancy
			if(!redund) {
				String suri = subj.stringValue();
				int split = XMLUtil.findURISplitIndex(suri);
				
				if(split!=-1) {
					if(!ns.containsNS(suri.substring(0,split))) doneSubj.add(subj.stringValue());
				}
			}
			
		}
		
	}
	
	public String getName(Resource res) {
		
		String result = "not availible";
		
		if(res!=null) {
			if(res instanceof BNode) {
				
				result = "[" + res.toString() + "]";
				
			} else {
				String uri = res.toString();
				
				int split = XMLUtil.findURISplitIndex(uri);
				
				if(split!=-1) {
				
					String namespace = uri.substring(0, split);
					String localName = uri.substring(split);
					
					// check whether namespace uri is contained in the ns list
					if(namespace!=null && localName!=null) {
						if(ns.containsNS(namespace)) {
							
							// get the short name of the ns
							String shortns = ns.getNS(namespace);
							if(shortns!=null) {
								result = shortns+":"+localName;
							}
						} else {
							// internal object (not known ns) - leave the localname here
							result = localName;
						}
					}
				}
			}
		}
		
		return result;
	
	}
	
	public String getName(Value obj) {
		
		String result = "not availible";
		
		if(obj!=null) {
			if(obj instanceof BNode) {
				
				result = "[" + obj.toString() + "]";
				
			} else {
				String uri = obj.toString();
				
				int split = XMLUtil.findURISplitIndex(uri);
				
				if(split!=-1) {
				
					String namespace = uri.substring(0, split);
					String localName = uri.substring(split);
					
					// check whether namespace uri is contained in the ns list
					if(namespace!=null && localName!=null) {
						result = namespace;
						if(ns.containsNS(namespace)) {
							
							// get the short name of the ns
							String shortns = ns.getNS(namespace);
							if(shortns!=null) {
								result = shortns+":"+localName;
							}
						}
					}
				}
			}
		}
		
		return result;
	
	}
	
	
	public int getChildrenCount(Resource res) throws Exception {
		int erg;
		RepositoryResult<Statement> stmt = con.getStatements(res, null, null, false);
		erg = stmt.asList().size();
		stmt.close();
		return erg;
	}
	
	public void printTab(int depth) {
		for(int i=0;i<depth;i++) {
			writer.write("\t");
		}
	}
	
	public DataWriter getWriter() {
		return writer;
	}
	
	/**
	 * Setter for respecting and parsing resources in the data sources every time it appears
	 * @param redund
	 */
	public void setRedund(boolean redund) {
		this.redund = redund;
	}
	
	public boolean getRedund() {
		return redund;
	}
	
	public static boolean getVcardwarning() {
		return vcardwarning;
	}
	
	public static void setVcardwarning(boolean vcw) {
		vcardwarning = vcw;
	}
}
