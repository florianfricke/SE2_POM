package types;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLog {
	public static void write(Exception e){
		String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileOutputStream("Log.log", true));
			pw.println("");
			pw.println("************************************************************************");
			pw.println("Exception Time Stamp: "+timeStamp);
			pw.println("************************************************************************");
			pw.println("");
			e.printStackTrace(pw);
			pw.flush();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			e.printStackTrace();
		}           
	}
}
	

