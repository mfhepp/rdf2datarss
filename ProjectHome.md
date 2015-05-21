This tool converts RDF/XML or N3 content into DataRSS feeds as accepted by Yahoo SearchMonkey. The tool can be used to feed GoodRelations-based e-commerce descriptions into the Yahoo family of technology.

It is based on the Sesame 2.2.4 framework from http://www.openrdf.org

You can see a working sample here: http://www.ebusiness-unibw.org/tools/rdf2datarss

If you want to build your own application out of the source you have to consider the following instructions:

WebContent/WEB-INF/web.xml:
I) create a context parameter by using the code:


&lt;context-param&gt;


> 

&lt;param-name&gt;

RDFDir

&lt;/param-name&gt;


> 

&lt;param-value&gt;

/home/projects/rdf2datarss/user\_files

&lt;/param-value&gt;


> 

&lt;/context-param&gt;



this is the folder where the converted rss files will be stored

II) register the specific classes as servlets:


&lt;servlet&gt;


> 

&lt;description&gt;


> 

&lt;/description&gt;


> 

&lt;display-name&gt;


> RDF2DataRSSServlet

&lt;/display-name&gt;


> 

&lt;servlet-name&gt;

RDF2DataRSSServlet

&lt;/servlet-name&gt;


> 

&lt;servlet-class&gt;


> servlet.RDF2DataRSSServlet

&lt;/servlet-class&gt;


> 

&lt;/servlet&gt;



III) additionally let's do some uri mappings:


&lt;servlet-mapping&gt;


> 

&lt;servlet-name&gt;

RDF2DataRSSServlet

&lt;/servlet-name&gt;


> 

&lt;url-pattern&gt;

/RDF2DataRSSServlet

&lt;/url-pattern&gt;


> 

&lt;/servlet-mapping&gt;


> 

&lt;servlet-mapping&gt;


> > 

&lt;servlet-name&gt;

RDF2DataRSSServlet

&lt;/servlet-name&gt;


> > 

&lt;url-pattern&gt;

/RDF2dataRSSServlet

&lt;/url-pattern&gt;



> 

&lt;/servlet-mapping&gt;



WebContent/META-INF/context.xml
I) make sure that the library-files won't be locked by the application server:


&lt;Context antiResourceLocking="true" antiJARLocking="true"&gt;



> <!-- Default set of monitored resources -->
> 

&lt;WatchedResource&gt;

WEB-INF/web.xml

&lt;/WatchedResource&gt;


> <!-- Uncomment this to disable session persistence across Tomcat restarts -->
> <!--
> -->



&lt;/Context&gt;

