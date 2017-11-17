package szpital;

import java.sql.SQLException;
import java.util.Scanner;

public class Check {
	
	public static boolean date_exists(DB db, Scanner rl, String date){
		String sql=String.format("select count(*) from daty where date ='%s';", date);
		if (db.select(sql)){
			try {
				if (db.getRs().next()) {
					return (db.getRs().getInt(1)>0);
				}
			} catch (SQLException ex) {
				System.out.println("Wyst�pi� b��d !\n"+ex.getMessage());
				return false;
			}				
		} 
		return false;
	}
	
	public static boolean time_exists(DB db, Scanner rl, String time){
		String sql=String.format("select count(*) from czasy where godzina_od ='%s';", time);
		if (db.select(sql)){
			try {
				if (db.getRs().next()) {
					return (db.getRs().getInt(1)>0);
				}
			} catch (SQLException ex) {
				System.out.println("Wyst�pi� b��d !\n"+ex.getMessage());
				return false;
			}				
		} 
		return false;
	}
}
