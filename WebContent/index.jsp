<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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

<div class="firstinfo">
  <h3>What is it?</h3>
  <p>This tool converts RDF/XML or N3 content into <a href="http://developer.yahoo.com/searchmonkey/smguide/datarss.html" target="_blank">DataRSS feeds</a> as accepted by <a href="http://developer.yahoo.com/searchmonkey/" target="_blank">Yahoo SearchMonkey</a>. The tool can be used to feed GoodRelations-based e-commerce descriptions into the Yahoo family of technology.
  </p>
  <p>Hint: Use the <a href="http://www.ebusiness-unibw.org/tools/goodrelations-annotator/" target="_blank">GoodRelations Annotator tool</a> to describe your business.
  </p>
</div>

<p>&nbsp;</p>
<p>&nbsp;</p>
<h3>Form-based Web Access</h3>
<p>&nbsp;</p>
<div id="tabs1" class="tabset">
	<ul class="tabset_tabs">
		<li class="active"><a href="#convert-by-uri">Convert from URI</a></li>
		<li><a href="#convert_by_upload">File Upload</a></li>
		<li><a href="#convert-by-input">Direct Input</a></li>
  </ul>
	<div class="tabset_content_container">
		<div class="tabset_content">

			<form action="RDF2DataRSSServlet" method="post">
			<h4>Convert the RDF/XML or N3 file available at a given URI to DataRSS</h4>
			<p>Please specify the URI of the file to be converted:</p>
			<p>	<label title="Address of page to Validate" for="uri">URI:</label>
				<input type="text" name="uri" id="uri" size="45" />
			</p>
			<p>&nbsp;</p>
			<p>Select the format of the data source:<br>
				<select name="format">
					<option value="xml" selected>RDF/XML</option>
					<option value="n3">N3</option>
				</select>
			</p>
			<p>&nbsp;</p>
			<span style="font-size:12px;"><p>By using this service, I grant the E-Business and Web Science Research   Group the right to store and analyze your input for research purposes.    </p>
			<p>
			  <input name="dontsave" type="checkbox" id="dontsave" value="1" />
			  Check if you don't agree!</p></span>
			<p class="submit_button">

				<input type="submit" title="Submit for convertion" value="Start conversion" />
			</p>
		  </form>
		</div>
		<div class="tabset_content">
			<form action="RDF2DataRSSServlet" method="post" enctype="multipart/form-data">
			<h4>Upload an RDF/XML or N3 file for conversion to DataRSS</h4>
			<p>Please specify the local file to be converted:</p>

			<p>
			  <label for="uploadfile">File name: </label>
			  <input type="file" name="upload" id="upload" maxlength="10737418240" />
			</p>
			<p>&nbsp;</p>
			<p>Select the format of the data source:<br>
				<select name="format">
					<option value="xml" selected>RDF/XML</option>
					<option value="n3">N3</option>
				</select>
			</p>
			<p>&nbsp;</p>
			<span style="font-size:12px;"><p>By using this service, I grant the E-Business and Web Science Research   Group the right to store and analyze your input for research purposes.    </p>
			<p>
			  <input name="dontsave" type="checkbox" id="dontsave" value="1" />
			  Check if you don't agree!</p></span>
			<p class="submit_button">
				<input type="submit" title="Submit for convertion" value="Start conversion" />
			</p>
		  </form>
		</div>
		<div class="tabset_content">

			<form action="RDF2DataRSSServlet" method="post">
			<h4>Convert the RDF/XML or N3 content to DataRSS</h4>
			<p>Paste an RDF/XML or N3 document into the text field below:</p>
			<p>
			  <textarea id="code" name="code" rows="12" cols="80"></textarea>
			</p>
			<p>&nbsp;</p>
			<p>Select the format of the data source:<br>
				<select name="format">
					<option value="xml" selected>RDF/XML</option>
					<option value="n3">N3</option>
				</select>
			</p>
			<p>&nbsp;</p>
			<span style="font-size:12px;"><p>By using this service, I grant the E-Business and Web Science Research   Group the right to store and analyze your input for research purposes.    </p>
			<p>
			  <input name="dontsave" type="checkbox" id="dontsave" value="1" />
			  Check if you don't agree!</p></span>
			<p class="submit_button">
				<input type="submit" title="Submit for convertion" value="Start conversion" />
			</p>

		  </form>
		</div>
	</div>
</div>

<p>&nbsp;</p>
<p>&nbsp;</p>
<div class="webservice">
<h3>Web Service</h3>
<p>The RDF2DataRSS tool can also be directly accessed from applications in a REST style by sending an HTTP GET request to the URI  </p>
<p>&nbsp;</p>
<p style="font-family:'Courier New', Courier, monospace; font-size:12px;">http://www.ebusiness-unibw.org/tools/rdf2datarss/RDF2DataRSSServlet?type=[xml|n3]&amp;uri=</p>
<p style="font-family:'Courier New', Courier, monospace; font-size:12px;">&nbsp;</p> 
<p>with being the URI of a file to be converted and TYPE the format of the data source (either xml for RDF/XML or n3 for N3 format), e. g.
</p>
<p>&nbsp;</p>
<table width="95%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="10%" height="25" style="font-size:13px;">for RDF/XML:</td>
    <td width="90%" height="25"><span style="font-family:'Courier New', Courier, monospace; font-size:12px;">http://www.ebusiness-unibw.org/tools/rdf2datarss/RDF2DataRSSServlet?type=xml&amp;uri=http://www.host.com/rdffile.rdf</span></td>
  </tr>
  <tr>
    <td height="25" style="font-size:13px;">for N3:</td>
    <td height="25"><span style="font-family:'Courier New', Courier, monospace; font-size:12px;">http://www.ebusiness-unibw.org/tools/rdf2datarss/RDF2DataRSSServlet?type=n3&amp;uri=http://www.host.com/n3file.n3</span></td>
  </tr>
</table>
<p>&nbsp;</p>
<p>This returns an XML file containing the RDF content as DataRSS. </p>
<p>&nbsp;</p>
<p>Hint: set an additional parameter &quot;save&quot; with the value &quot;0&quot; (i. e. <span style="font-family:'Courier New', Courier, monospace; font-size:12px;">&amp;save=0</span>) if you don't want the output code to be stored on server for research and analyzing purpose.</p>
</div>

<p>&nbsp;</p>
<p>&nbsp;</p>
<div class="distribution">
  <h3>Distribution</h3>
  <p>The source code of this application will be shortly made available under an LPGL license. Please e-mail to <a href="mailto:tools@ebusiness-unibw.org">tools@ebusiness-unibw.org</a> if you need it right now. </p>
</div>

<p>&nbsp;</p>
<p>&nbsp;</p>
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

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-5400886-3");
pageTracker._trackPageview();
} catch(err) {}</script>

</body>
</html>
