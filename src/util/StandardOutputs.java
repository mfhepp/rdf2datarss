package util;

import converter.Converter;

/**
 * helper-class that contains some standardized output information
 * that are needed to create well-formed atom/xml feeds
 * 
 * For any bug reporting please don't hesitate to contact me by email
 * 
 * @version 1.0b
 * @author Thomas Irmscher (thomas.irmscher@unibw.de)
 */

public class StandardOutputs {

	/**
	 * inserts a standard header for every DataRSS file with atom and datarss specific
	 * aspects
	 */
	public static void insertHeader(DataWriter writer, String baseurl, String currentdate, HandleNS namespaces) {
		
		// throw warning because of old version of vcard
		if(Converter.getVcardwarning()) {
			writer.writeln("<!-- Warning: The submitted file uses the vCard 2001 elements for \n" +
					"contact details. Yahoo SearchMonkey, however, does consider vCard 2006 elements only. \n" +
					"Unless you update and convert your file again, the contact details in the created DataRSS \n" +
					"feed will be ignored by Yahoo.-->\n\n");
		}
		
		// add the doctype and encoding of the xml file
		writer.writeln("<?profile http://search.yahoo.com/searchmonkey-profile ?>");
		//writer.writeln("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		
		String gr = "<name>GR RDF2DataRSS Converter</name><uri>"+baseurl+"</uri>";

		writer.write("<feed ");
		writer.writeln("xmlns=\"http://www.w3.org/2005/Atom\"");
		namespaces.printNamespaces(writer);
		writer.writeln("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		writer.writeln("xsi:schemaLocation=\"http://www.w3.org/2005/Atom  ../latest/xsd/datarss.xsd\">");
		writer.writeln("<id>"+baseurl+"</id>");
		writer.writeln("<updated>"+currentdate+"</updated>");
		writer.writeln("<link rel=\"self\" href=\""+baseurl+"\"/>");
		writer.writeln("<author>"+gr+"</author>");
		writer.writeln("<contributor>"+gr+"</contributor>");
		writer.writeln("<logo>http://www.heppnetz.de/projects/goodrelations/goodrelations-logo-small.gif</logo>");
		writer.writeln("<title>Converted rdf to DataRSS data for Yahoo SearchMonkey</title>");
		writer.writeln("<entry>");
		writer.writeln("<id>"+baseurl+"</id>");
		writer.writeln("<link rel=\"self\" href=\""+baseurl+"\"/>");
		writer.writeln("<title>Converted RDF data from " + baseurl + "</title>");
		writer.writeln("<updated>"+currentdate+"</updated>");
		writer.writeln("<content type=\"application/xml\">");
		writer.writeln("<y:adjunct version=\"1.0\" id=\"com.yahoo.extraction.shopping\" name=\"shopping\">");
	}
	
	/**
	 * Returns the essential footer with the closing tags
	 */
	public static void insertFooter(DataWriter writer) {
		writer.writeln("</y:adjunct>");
		writer.writeln("</content>");
		writer.writeln("</entry>");
		writer.writeln("</feed>");
	}
	
}
