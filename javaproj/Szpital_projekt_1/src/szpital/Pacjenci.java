package szpital;

import java.sql.SQLException;
import java.util.Scanner;

public class Pacjenci {

	DB db;
	Scanner rl;
	
	Pacjenci(DB db, Scanner rl){
		this.db=db;
		this.rl=rl;
	}
	
	public String print(){
		String res="";
		
		res+=String.format("%-5s|%-15s|%-20s|%-12s|%-50s\n", "ID","Imi�","Nazwisko","PESEL","Adres");
		res+="---------------------------------------------------------------------------------------------------------\n";
		try {
			while (db.getRs().next()){
				res+=String.format("%-5s|%-15s|%-20s|%-12s|%-50s\n"
						        , db.getRs().getString(1)
								, db.getRs().getString(2)
								, db.getRs().getString(3)
								, db.getRs().getString(4)
								, db.getRs().getString(5)
						);
			}
		} catch (SQLException ex) {
			res+="Wyst�pi� b��d !\n";
		}
			
		return res;
	}

	@Override
	public String toString(){
		String sql="select idpacjenta, imie, nazwisko, pesel, adres from pacjenci;";
		if (db.select(sql)){
			return this.print();
		};
		return "Co� posz�o nie tak... :-(";
	}
	
	public boolean check(int id){
		String sql=String.format(
				"select * from pacjenci where idpacjenta='%s' ;",
				id);
		return db.select(sql) && (db.next());
	}
					
	
	public boolean add(String imie, String nazwisko, String PESEL, String adres){
		String sql=String.format(
				"insert into pacjenci(imie, nazwisko, pesel, adres) "
				+ "values('%s','%s','%s','%s');"
						,imie, nazwisko, PESEL, adres);
		
		return db.execute(sql) && db.commit();
}
	
	public void ask_add(){
		System.out.println("Dodawanie pacjenta");
		System.out.println("Na ka�dym etapie mo�esz nacisn�� ENTER w pustej linii, aby przerwa� dodawanie");
		System.out.println("Podaj imi� pacjenta     :"); String imie=rl.nextLine();
		if (!imie.equals("")) {
			System.out.println("Podaj nazwisko pacjenta :"); String nazwisko=rl.nextLine();
			if (!nazwisko.equals("")) {
				System.out.println("Podaj PESEL pacjenta  :"); String PESEL=rl.nextLine();
				if (!PESEL.equals("")) {
					System.out.println("Podaj adres pacjenta :"); String adres=rl.nextLine();
					if (!adres.equals("")) {
						if (this.add(imie, nazwisko, PESEL, adres)) System.out.println("Dodano !"); else System.out.println("Wyst�pi� b��d!");
						return;
					}
				}							
			}
		}
		System.out.println("Przerwanie!");
		return;
				
	}
}
