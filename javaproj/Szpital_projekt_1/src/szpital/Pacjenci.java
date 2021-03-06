package szpital;

import java.sql.SQLException;
import java.util.Scanner;

public class Pacjenci {

	DB db;
	Scanner rl;
	int rowscount;
	
	Pacjenci(DB db, Scanner rl){
		this.db=db;
		this.rl=rl;
	}
	
	public String print(){
		String res="";
		rowscount=0;
		
		res+=String.format("%-5s|%-15s|%-20s|%-12s|%-50s\n", "ID","Imi�","Nazwisko","PESEL","Adres");
		res+="---------------------------------------------------------------------------------------------------------\n";
		try {
			while (db.getRs().next()){
				rowscount++;
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
	
	public int wybierz_pacjenta()
	{
		String pac;
		int idp=0;
		boolean exit=false;
		do {
			System.out.println("Wyszukaj pacjenta, podaj imi� i nazwisko lub PESEL [Enter=wyj�cie, % - wyszuka wszystkich]:");
			pac=rl.nextLine().toUpperCase();
			if (pac.equals("")) return -1;
			pac="%"+pac.replace(" ", "%")+"%";
			//System.out.println(pac);
			if (find(pac)>0){
				System.out.println("Podaj ID pacjenta z listy lub <0 w celu ponownego wyszukania");
				do{
					idp=Tools.nextInt(rl, "Identyfikator musi by� liczb�");
					if (idp<0) break;
					if (check(idp)) {
						exit=true;
						break;
					}
					System.out.println("Nieistniej�cy ID pacjenta !");
				} while(true);
			} else {
				System.out.println("Brak wynik�w, zmie� kryteria wyszukiwania...");
			}
		} while (!exit);
		return idp;
	}
	
	public int find(String pac){
		String sql=String.format("select b.idpacjenta, b.imie, b.nazwisko, b.pesel, b.adres from ("
				+ "	select concat(upper(PESEL),' ',upper(imie),' ',upper(nazwisko)"
				+ " ,' ',upper(imie),' ',upper(PESEL)) sklejak , idpacjenta"
				+ "	from pacjenci "
				+ ") a inner join pacjenci b on b.idpacjenta=a.idpacjenta"
				+ " where a.sklejak like '%s' order by nazwisko, imie;",pac);
		//System.out.println(sql);
		if (db.select(sql)){
			System.out.println(this.print());
			return this.rowscount;
		};
		System.out.println("Co� posz�o nie tak... :-(");
		return 0;
	}
}
