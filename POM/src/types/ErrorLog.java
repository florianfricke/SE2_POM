package pom_service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ErrorLog {

	public static void write(SQLException e){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
			PrintWriter pw;
			try {
				pw = new PrintWriter(new FileOutputStream("Log.log", true));
				e.printStackTrace(pw);
				pw.println(dateFormat.format(cal));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				e.printStackTrace(); // wenn datei nicht geöffnet werden kann -> Konsolenausgabe
			}              
		}

	public static void write(Exception e){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
			PrintWriter pw;
			try {
				pw = new PrintWriter(new FileOutputStream("Log.log", true));
				e.printStackTrace(pw);
				pw.println(dateFormat.format(cal));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				e.printStackTrace(); // wenn datei nicht geöffnet werden kann -> Konsolenausgabe
			}              
		}
	
	public static void write(){}
	

}
	

