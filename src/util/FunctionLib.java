package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * helper-class that contains some functions that are needed within 
 * the converter
 * 
 * For any bug reporting please don't hesitate to contact me by email
 * 
 * @version 1.0b
 * @author Thomas Irmscher (thomas.irmscher@unibw.de)
 */

public class FunctionLib {
	
	public static String getTabs(int x) {
		String tabs = "";
		for(int t=0;t<x;t++) {
			tabs+="\t";
		}
		return tabs;
	}
	
	public String getLocalNameElem(String elem) {
		String result = "";
		StringBuffer str = new StringBuffer(":");
		
		if(elem.contains(str)) {
			result = elem.substring(elem.indexOf(":")+1);
		} else {
			result = elem;
		}
		
		return result;
	}
	
	/**
	 * Returns a String with the current date in atom conform structure
	 * structure for atom: yyyy-MM-ddTHH:mm:ssZ
	 * @return String
	 */
	public String getCurrentDate() {
		String result = "";
		String offstr = null;
		
		Calendar cal = Calendar.getInstance();
		
		SimpleDateFormat sdf1,sdf2,offset = null;
		sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		sdf2 = new SimpleDateFormat("HH:mm:ss");
		offset = new SimpleDateFormat("Z");
		offstr = offset.format(cal.getTime());
		
		result = sdf1.format(cal.getTime()) + "T" + sdf2.format(cal.getTime()) + offstr.substring(0,3) + ":" + offstr.substring(3,5);
		
		return result;
	}
	
}
