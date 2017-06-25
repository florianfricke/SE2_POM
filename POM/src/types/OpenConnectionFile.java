package types;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class OpenConnectionFile {
	private static ConnectionParameter cp;
	public static ConnectionParameter readFile() throws IOException{
		
	 	FileReader fr;
	 	try {
			fr = new FileReader("dbConnectionFile.txt");
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			 cp = new ConnectionParameter(br.readLine().trim(), br.readLine().trim(),br.readLine().trim(), br.readLine().trim(), br.readLine().trim());
			 return cp;
		}catch (FileNotFoundException e) {
			ErrorLog.write(e);
		}
	 	
	 return cp;
}
}
