package szpital;

import java.security.spec.RSAKeyGenParameterSpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.Scanner;

public class DB {
	
	private Connection con;
	private Statement stm;
	private ResultSet rs;
	
	public ResultSet getRs() {
		return rs;
	}
	
	public boolean next(){
		try {
			return rs.next();
		} catch (Exception ex){
			return false;
		}
	}

	public static DB createDBConnection(String connstring, String username, String password){
		DB db=new DB();
		try {
			db.con = DriverManager.getConnection(connstring,username,password);
			db.con.setAutoCommit(false);
			db.stm=db.con.createStatement();
			return db;
		} catch (Exception ex){
			return null;
		}
	}
	
	public boolean select(String sqlstring){
		try {
			this.rs=this.stm.executeQuery(sqlstring);
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}
	
	public boolean execute(String sqlstring){
		try {
			this.stm.executeUpdate(sqlstring);
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}
	
	public boolean commit(){
		try {
			this.con.commit();
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}
	
	public boolean rollback(){
		try {
			this.con.rollback();
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}
	
	public boolean closeConnection(){
		try{
			this.con.close();
			return false;
		} catch (SQLException ex) {
			return false;
		}
	}
}
