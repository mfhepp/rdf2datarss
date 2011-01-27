<%@page 
import="org.apache.commons.io.IOUtils" 
import="java.io.*"
language="java" 
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>GoodRelations RDF2DataRSS Converter</title>
	<link type="text/css" rel="stylesheet" href="style/tabs.css" media="all" />
	<link rel="stylesheet" type="text/css" media="screen" href="style/style.css" />
	<script type="text/javascript" src="scripts/prototype.js"></script>
	<script type="text/javascript" src="scripts/effects.js"></script>

	<script type="text/javascript" src="scripts/tabs.js"></script>
</head>
<body>

<div class="grlogo">
	<a href="http://purl.org/goodrelations/"><img src="images/goodrelations-logo.png" width="275" height="75" alt="GoodRelations Logo" longdesc="http://purl.org/goodrelations/" /></a>
</div>
<div>
  <h1>GoodRelations RDF2DataRSS Converter</h1>
</div>


<div style="clear:both"/>

<div id="tabs1" class="tabset">
	<ul class="tabset_tabs">
		<li class="active">DataRSS Output-Code</li>
	</ul>
	<div class="tabset_content_container">
		<form name="datarss_code" id="datarss_code" action="">
            <p>
              <textarea name="code_field" rows="25" cols="150" style="white-space: nowrap;">
<%
	String f_name = (String)request.getSession(true).getAttribute("rdf_file_name");
	String path = super.getServletContext().getInitParameter("RDFDir");
	FileInputStream fis = new FileInputStream(new File(path+"/"+f_name));
	BufferedReader br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
	
	String line;
	while((line = br.readLine())!=null) {
		out.println(line);
	} 
%></textarea>
            </p>
            <p>&nbsp;</p>
            <p><strong>Direct-Download</strong></p>
            <p>If you want to download a file containing the converted DataRSS-Code, please click <a href="?dl=rssfile">here</a>!</p>
      </form>
	</div>
</div>



<div id="footer">
    <p><strong>Developers:</strong> Thomas Irmscher, Andreas Wechselberger, Andreas Radinger and Martin Hepp</p>
	<p><strong>Disclaimer:</strong> This service is provided by the E-Business and Web Science  Research Group at Bundeswehr University Munich as it is with no  explicit or implicit guarantees.<br />
Contact: Prof. Dr. Martin Hepp, Professur für ABWL, insbesondere  E-Business, Universität der Bundeswehr München, Werner-Heisenberg-Weg  39, D-85579 Neubiberg, Germany,<br />
Phone +49-89-6004-4217, E-mail: <a href="mailto:tools@ebusiness-unibw.org">tools@ebusiness-unibw.org</a>, <a href="http://www.unibw.de/ebusiness/">http://www.unibw.de/ebusiness/</a>.</p>

<br />

	<table class="footerlogos">
			<tr>
				<td>
				<a href="http://www.unibw.de/"><img src="images/unibw_logo_opt.gif" width="214" height="40" alt="Universit&auml;t der Bundeswehr M&uuml;nchen" longdesc="http://www.unibw.de/" /></a>
				</td>
				<td></td>
				<td>
				<a href="http://www.unibw.de/ebusiness/"> <img src="images/erg_logo_opt.gif" width="173" height="40" alt="E-Business and Web Science Research Group" longdesc="http://www.unibw.de/ebusiness/" /></a>

				</td>
			</tr>
		</table>
</div>


<script type="text/javascript">
	document.observe('dom:loaded', function() {
		new Tab({id: "tabs1", rounded: 1, height: 1});
	});
</script>

</body>
</html>