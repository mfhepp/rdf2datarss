package servlet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import converter.Converter;

/**
 * The servlet class which is something like the controller of the web app
 * You can access the web app in two ways:
 * 1. doPOST: you've got three options to convert your RDF code: a) uri b) file upload c) direct code input
 * 2. doGET: access the converter directly by using this URI: 
 * http://www.domain.com/dataRSSServlet/RDF2dataRSSServlet?type=xml&http://www.url.com/semanticweb.rdf
 * 
 * For any bug reporting please don't hesitate to contact me by email
 * 
 * @version 1.0b
 * @author Thomas Irmscher (thomas.irmscher@unibw.de)
 */

/**
 * Servlet implementation class RDF2dataRSSServlet
 */
public class RDF2DataRSSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RDF2DataRSSServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri,xml_flag,xml_uri,save = null;
		uri = request.getParameter("dl");
		xml_flag = request.getParameter("type");
		xml_uri = request.getParameter("uri");
		save = request.getParameter("save");

		// for xml progressing
		if(xml_flag!=null && (xml_flag.equals("xml") || xml_flag.equals("n3")) && xml_uri!=null) {
			
			PrintWriter out = response.getWriter();
			try {
				// the file where to store the rdf
				String f_name = createFileName();
				String f_path = getServletContext().getInitParameter("RDFDir");
				
				//File rdffile = new File(super.getServletContext().getRealPath("/rdf_files/"+f_name));
				File rdffile = new File(f_path+"/"+f_name);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rdffile),"UTF-8"));				
				
				Converter conv = new Converter(bw,xml_uri);
				
				conv.convert(xml_uri,xml_flag);
				conv.close();
				bw.close();
				
				
				// output of the rdf
				FileInputStream fis = new FileInputStream(rdffile);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
				
				response.setContentType("text/xml; Charset=UTF-8");

				String line;
				while((line = br.readLine())!=null) {
					out.println(line);
				}
				
				br.close();
				
				// delete file if no submission given
				if(save!=null && save.equals("0")) rdffile.delete();
				
				
			} catch(Exception ex) {
				out.println("warning: "+ex.toString());
			}
			
		}
		
		if(uri!=null) {
			if(uri.equals("rssfile")) {
				HttpSession session = request.getSession(true);
				
				String f_name = (String)session.getAttribute("rdf_file_name");
				String dontsave2 = (String)session.getAttribute("dontsave");
				String f_path = getServletContext().getInitParameter("RDFDir");
				
				File rss_file = new File(f_path+"/"+f_name);
				
				if(rss_file.exists()) {
	
					try {
			    		byte[] buffer = new byte[8192];
			    		response.reset();
			    		response.setHeader("Content-Type", "application/rss+xml; Charset=UTF-8");
			    		response.setHeader("Content-Disposition", "attachment; filename=semanticweb.rss");
			    		OutputStream w = response.getOutputStream();
			    		FileInputStream fileInputStream = new FileInputStream(rss_file);
			    		while (fileInputStream.available() > 0) {
			    			int cnt = fileInputStream.read(buffer);
			    			w.write(buffer, 0, cnt);
			    		}
			    		w.close();
			    		
						/*FileInputStream fis = new FileInputStream(rss_file);
						BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
						
						//response.setContentType("text/xml; Charset=UTF-8");
						response.setHeader("Content-Type", "application/rss+xml; Charset=UTF-8");
						response.setHeader("Content-Disposition", "attachement; filename=semanticweb.rss");
						
						String line;
						while((line = br.readLine())!=null) {
							out.println(line);
						}
						
						br.close();*/
						
					} catch(Exception ex) {
						//out.println(ex.toString());
					}
					
				} else {
					//out.println("no data availible");
				}
				
				// delete file if no submission given
				if(dontsave2.equals("1")) rss_file.delete();
			}
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri,code,dontsave,format = null;
		uri = request.getParameter("uri");
		code = request.getParameter("code");
		format = request.getParameter("format");
		dontsave = request.getParameter("dontsave");
		
		// the file where to store the rdf
		String f_name = createFileName();
		String f_path = getServletContext().getInitParameter("RDFDir");
		
		File rdffile = new File(f_path+"/"+f_name);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rdffile),"UTF-8"));
		
		/** forward the data bean to the jsp for output **/
		HttpSession session = request.getSession(true);

		session.setAttribute("rdf_file_name", f_name);
		session.setAttribute("dontsave", dontsave);

		try {
			Converter conv;
			if(uri!=null) {
				conv = new Converter(bw, uri);
			}
			else {
				conv = new Converter(bw);
			}
		
			if(ServletFileUpload.isMultipartContent(request)) {

				FileItemFactory factory = new DiskFileItemFactory();
					
				ServletFileUpload upload = new ServletFileUpload(factory);
					
				List items = upload.parseRequest(request);
					
				Iterator it = items.iterator();
					
				while(it.hasNext()) {
					FileItem item = (FileItem)it.next();
	
					   if (!item.isFormField()) {
						   conv.convert(item.getInputStream(), format);
					   } 
				}
			
			} else if(uri!=null) {
				//conv.setRedund(true);
				conv.convert(uri, format);
				
			} else if(code!=null) {
				InputStream in = IOUtils.toInputStream(code);
				//System.out.println(in.available());
				conv.convert(in, format);
			}
			
			conv.close();
		} catch(Exception ex) {
			bw.write(ex.toString());
		}

		bw.close();
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/output.jsp");
		dispatcher.forward(request, response);
		
	}
	
	public String createFileName() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SS");
		String f_name = "rdf2rss_"+s.format(cal.getTime())+".rss";
		
		return f_name;
	}

}