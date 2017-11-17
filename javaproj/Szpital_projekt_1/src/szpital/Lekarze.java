package szpital;

import java.sql.SQLException;
import java.util.Scanner;

public class Lekarze {
	
	DB db;
	Scanner rl;
	
	Lekarze(DB db, Scanner rl){
		this.db=db;
		this.rl=rl;
	}
	
	public String print(){
		String res="";
		
		res+=String.format("%-5s|%-15s|%-20s\n", "ID","Imi�","Nazwisko");
		res+="------------------------------------------\n";
		try {
			while (db.getRs().next()){
				res+=String.format("%-5s|%-15s|%-20s\n"
						        , db.getRs().getString(1)
								, db.getRs().getString(2)
								, db.getRs().getString(3)
						);
			}
		} catch (SQLException ex) {
			res+="Wyst�pi� b��d !\n";
		}
			
		return res;
	}

	@Override
	public String toString(){
		String sql="select idlekarza, imie, nazwisko from lekarze where isvalid=1;";
		if (db.select(sql)){
			return this.print();
		};
		return "Co� posz�o nie tak... :-(";
	}
	
	public boolean check(int id){
		String sql=String.format(
				"select * from Lekarze where idlekarza='%s' and isvalid=1;",
				id);
		return db.select(sql) && (db.next());
	}
	
	public boolean del(int id){
		String sql=String.format(
				"update Lekarze set isvalid=0 where idlekarza=%d; "
				,id);
		
		return db.execute(sql) && db.commit();
	}
	
	public int wybierz(){
		System.out.println(this);
		do {
			System.out.println("Podaj identyfikator lekarza  (<0 - przerwanie):");
			int id=Tools.nextInt(this.rl, "Identyfikator musi byc liczb�!");
			if (id<0){
				System.out.println("Przerwanie!");
				break;
			}
			if (check(id)) {				
				return id;
			}
			System.out.println("Nie znaleziono takiego ID, spr�buj ponownie...");
		} while (true);
		return -1;
	}
	
	public boolean ask_del(){
		System.out.println("Usuwanie lekarza, lista lekarzy:");
		System.out.println(this);
		do {
			System.out.println("Podaj identyfikator lekarza do usuni�cia (<0 - przerwanie):");
			int id=Tools.nextInt(this.rl, "Identyfikator musi byc liczb�!");
			if (id<0){
				System.out.println("Przerwanie!");
				break;
			}
			if (check(id)) {
				//System.out.println("Znaleziono taki ID!");
				boolean res=del(id);
				if (res)
					System.out.println("Usuni�to!");
				else
					System.out.println("Wyst�pi b��d!");
				return res;
			}
			System.out.println("Nie znaleziono takiego ID, spr�buj ponownie...");
		} while (true);
		return true;
	}
	
	public boolean add(String imie, String nazwisko){
		String sql=String.format(
				"insert into Lekarze(imie, nazwisko, isvalid) "
				+ "values('%s','%s',1);"
						,imie, nazwisko);
		
		return db.execute(sql) && db.commit();
}
	
	public void ask_add(){
		System.out.println("Dodawanie lekarza");
		System.out.println("Na ka�dym etapie mo�esz nacisn�� ENTER w pustej linii, aby przerwa� dodawanie");
		System.out.println("Podaj imi� lekarze     :"); String imie=rl.nextLine();
		if (!imie.equals("")) {
			System.out.println("Podaj nazwisko lekarza :"); String nazwisko=rl.nextLine();
			if (!nazwisko.equals("")) {
				if (this.add(imie, nazwisko)) System.out.println("Dodano !"); else System.out.println("Wyst�pi� b��d!");
				return;
			}
		}
		System.out.println("Przerwanie!");
		return;
				
	}
}
