package types;

public class ConnectionParameter {
	private String serverAddress;
	private String port;
	private String dataBase;
	private String user;
	private String password;
	
	public ConnectionParameter(String servAdd, String port, String db, String user, String pw){
		this.serverAddress = servAdd;
		this.port = port;
		this.dataBase = db;
		this.user = user;
		this.password = pw;
	}
	
	public String getServerAddress(){return serverAddress;}
	public String getPort(){return port;}
	public String getDataBase(){return dataBase;}
	public String getUser(){return user;}
	public String getPassword(){return password;}
	
}
