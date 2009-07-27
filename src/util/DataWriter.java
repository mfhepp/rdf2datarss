package util;

import java.io.BufferedWriter;

/**
 * Class that helps to manage the output of the converter
 * 
 * For any bug reporting please don't hesitate to contact me by email
 * 
 * @version 1.0b
 * @author Thomas Irmscher (thomas.irmscher@unibw.de)
 */

public class DataWriter {
	
	private BufferedWriter bw;
	
	public DataWriter(BufferedWriter bw) {
		// TODO Auto-generated constructor stub
		this.bw = bw;
	}

	public void write(String input) {
		try {
			bw.write(input);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public void writeln(String input) {
		try {
			bw.write(input+"\n");
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public void writeln() {
		try {
			bw.write("\n");
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public BufferedWriter getBw() {
		return bw;
	}
}
