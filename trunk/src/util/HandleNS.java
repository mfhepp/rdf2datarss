package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.openrdf.model.Namespace;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

import converter.Converter;

/**
 * This class is need to handle the needed
 * namespaces in Converter.java and to output the ns in StandardOutputs.java
 * 
 * There exisits a collection with all namespaces
 * that have to be considered during parsing
 * 
 * For any bug reporting please don't hesitate to contact me by email
 * 
 * @version 1.0b
 * @author Thomas Irmscher (thomas.irmscher@unibw.de)
 */

public class HandleNS {

	/* Collection with the namespaces */
	private HashMap<String,String> namespaces;
	
	private HashMap<String,String> standard;
	
	public HandleNS() {
		// TODO Auto-generated constructor stub
		
		
		
		standard = new HashMap<String,String>();
		
		standard.put("gr","http://purl.org/goodrelations/v1#");
		standard.put("vcard", "http://www.w3.org/2006/vcard/ns#");
		standard.put("dc", "http://purl.org/dc/elements/1.1/");
		standard.put("foaf", "http://xmlns.com/foaf/0.1/");
		standard.put("vcal", "http://www.w3.org/2002/12/cal/icaltzd#");
		standard.put("review", "http://www.purl.org/stuff/rev#");
		standard.put("sioc", "http://rdfs.org/sioc/ns#");
		standard.put("dpedia","http://dbpedia.org/");
		standard.put("fb","http://rdf.freebase.com/ns/");
		standard.put("owl", "http://www.w3.org/2002/07/owl#");
		
		
		namespaces = new HashMap<String,String>();
		
		// add automatically some NS
		//namespaces.put("gr","http://purl.org/goodrelations/v1#");
		//namespaces.put("toy","http://www.heppnetz.de/ontologies/examples/toy#");
		namespaces.put("y", "http://search.yahoo.com/datarss/");
		//namespaces.put("foaf", "http://xmlns.com/foaf/0.1/");
		namespaces.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		namespaces.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		//namespaces.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		//namespaces.put("dc", "http://purl.org/dc/elements/1.1/");
		//namespaces.put("vcard", "http://www.w3.org/2001/vcard-rdf/3.0#");
		//namespaces.put("vcard", "http://www.w3.org/2006/vcard/ns#");
		//namespaces.put("vcal", "http://www.w3.org/2002/12/cal/icaltzd#");
		namespaces.put("rel", "http://search.yahoo.com/searchmonkey-relation/");	
		//namespaces.put("review", "http://www.purl.org/stuff/rev#");
		namespaces.put("xsd", "http://www.w3.org/2001/XMLSchema#");
		namespaces.put("eco", "http://www.ebusiness-unibw.org/ontologies/eclass/5.1.4/#");
		namespaces.put("commerce", "http://search.yahoo.com/searchmonkey/commerce/");
		namespaces.put("extraction", "http://search.yahoo.com/searchmonkey/extraction/");
		//namespaces.put("use", "http://search.yahoo.com/searchmonkey-datatype/use/");
		//namespaces.put("currency", "http://search.yahoo.com/searchmonkey-datatype/currency/");
		//namespaces.put("product", "http://search.yahoo.com/searchmonkey/product/");
		//namespaces.put("action","http://search.yahoo.com/searchmonkey/action/");
	}
	
	public void retrieveNS(RepositoryConnection rc) throws RepositoryException {
		// clear the current NS map
		//namespaces.clear();
		
		// Set of the NS keys
		Set<String> ns_keys = namespaces.keySet();
		
		RepositoryResult<Namespace> it = rc.getNamespaces();
		while(it.hasNext()) {
			Namespace ns = it.next();
			if(!ns_keys.contains(ns.getPrefix().toLowerCase()) && !standard.containsKey(ns.getPrefix().toLowerCase()) && !ns.getPrefix().equalsIgnoreCase("owl")) namespaces.put(ns.getPrefix(),ns.getName());
		
			// mark when old vcard is used
			if(ns.getPrefix().equalsIgnoreCase("vcard")) {
				if(!ns.getName().equalsIgnoreCase("http://www.w3.org/2006/vcard/ns#")) {
					Converter.setVcardwarning(true);
				} else {
					Converter.setVcardwarning(false);
				}
			}
		}
		it.close();
	}
	
	/**
	 * adds a new namespace to the ns-handler
	 */
	public void addNS(String namespace, String uri)
	{
		namespaces.put(namespace,uri);
	}
	
	public boolean containsNS(String ns) {
		return (namespaces.containsKey(ns) || namespaces.containsValue(ns) || standard.containsKey(ns) || standard.containsValue(ns));
	}
	
	public String getNS(String namespace) {
		
		String result = null;
		
		Collection<String> vals = null;
		Set<String> keys = null;
		
		if(standard.containsValue(namespace)) {
			vals = standard.values();
			keys = standard.keySet();
		} else if (namespaces.containsValue(namespace)){
			vals = namespaces.values();
			keys = namespaces.keySet();
		}
		
		if(vals!=null && keys!=null)
		{
			Iterator<String> it1 = vals.iterator();
			Iterator<String> it2 = keys.iterator();
			
			
			
			while(it1.hasNext()) {
				String cns = it1.next();
				result = it2.next();
				if(cns.equals(namespace)) break;
			}
		}
		return result;
	}
	
	/**
	 * prints all namespaces to an output stream
	 * @param out
	 */
	public void printNamespaces(DataWriter writer) {
		
		Set<String> setofns = namespaces.keySet();
		Collection<String> setofuri = namespaces.values();
		
		Iterator<String> it1,it2 = null;
		
		it1 = setofns.iterator();
		it2 = setofuri.iterator();
		
		while(it1.hasNext() && it2.hasNext()) {
			writer.writeln("xmlns:"+it1.next()+"=\""+it2.next()+"\" ");
		}
		
	}

}
