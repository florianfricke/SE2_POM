package types;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class OpenConnectionFile {
	public static 
	String readFile() throws IOException{
		
	 	FileReader fr;
		
	 	try {
			fr = new FileReader("dbConnectionFile.txt");
			BufferedReader br = new BufferedReader(fr);
			 String connectionLine = br.readLine();
			 return connectionLine;
		}catch (FileNotFoundException e) {
			ErrorLog.write(e);
			//e.printStackTrace();
		}
	  return "";
}
}
