package szpital;

import java.sql.SQLException;
import java.util.Scanner;

public class Grafik {
	DB db;
	Scanner rl;
	Lekarze L;
	Specjalnosci S;
	Pacjenci P;
	Wizyty W;
	
	Grafik(DB db, Scanner rl, Lekarze L, Specjalnosci S, Pacjenci P, Wizyty W){
		this.db=db;
		this.rl=rl;
		this.S=S;
		this.L=L;
		this.P=P;
		this.W=W;
	}
	
	public void print_grafik_na_dzien(int dzien){
		String sql=
				"select idls, lekarz, nazwaspecjalnosci, godziny from v_grafik_dzienny_l" 
				+ String.format(" where dzientyg=%d ",dzien)
				+ " order by 2,3,4";
		System.out.println(sql);
		if (db.select(sql)){
			System.out.println(String.format("%4s|%-40s|%-20s|%-10s","ID","Lekarz","Specjalno��","Godziny pracy"));
			System.out.println("--------------------------------------------------------------------------");
			try {
				while (db.getRs().next()){
					System.out.println(String.format("%4s|%-40s|%-20s|%-10s"
							        , db.getRs().getString(1)
									, db.getRs().getString(2)
									, db.getRs().getString(3)								
									, db.getRs().getString(4)
							));
				}
			} catch (SQLException ex) {
				System.out.println("Wyst�pi� b��d !\n");
			}
		};
	}
	
	public void grafik_na_dzien(){
		System.out.println("Grafik dzienny");
		System.out.println("Podaj dzie� tygodnia (1-niedziela, 7-sobota, 0-wyj�cie)");
		int dzien=Tools.nextInt(rl, "Dzie� musi by� liczb�", 0, 7);
		if (dzien==0) return;
		this.print_grafik_na_dzien(dzien);
		
		return;			
	}
	
	public void print_grafik_lekarza(int lekarz){
		String sql=
				"select idlekarza,lekarz,nazwaspecjalnosci,godziny,dzientyg from v_grafik_dzienny_l" 
				+ String.format(" where idlekarza=%d ",lekarz)
				+ " order by dzientyg";
		//System.out.println(sql);
		if (db.select(sql)){
			System.out.println(String.format("%4s|%-40s|%-20s|%-14s|%-3s","ID","Lekarz","Specjalno��","Godziny pracy","Dzie� tygodnia"));
			System.out.println("-----------------------------------------------------------------------------------------------");
			try {
				while (db.getRs().next()){
					System.out.println(String.format("%4s|%-40s|%-20s|%-14s|%-3s"
							        , db.getRs().getString(1)
									, db.getRs().getString(2)
									, db.getRs().getString(3)								
									, db.getRs().getString(4)
									, Tools.getDayNameDB(Integer.parseInt(db.getRs().getString(5)))
							));
				}
			} catch (SQLException ex) {
				System.out.println("Wyst�pi� b��d !\n");
			}
		};
	}
	
	public void grafik_lekarza(){
		System.out.println("Grafik lekarza, lista lekarzy");
		System.out.println(L);
		int lekarz;
		
		do{
			System.out.println("Podaj id lekarza (<0-wyj�cie):");
			lekarz=Tools.nextInt(rl, "Identyfikator musi by� liczb�");
			if (lekarz<0) {
				System.out.println("Przerwanie!");
				return;
			}
			if (L.check(lekarz)) break;
			System.out.println("Identyfikator nie istnieje, spr�buj ponownie...");
		} while (true);
		this.print_grafik_lekarza(lekarz);
		return;			
	}
	
	
	public void print_wizyty(){
		System.out.println(String.format("%-30s|%-20s|%-12s|%-7s|%15s","Lekarz","Specjalno��","Data","Godzina","identyfikator wizyty"));
		System.out.println("-----------------------------------------------------------------------------------------------");
		try {
			while (db.getRs().next()){
				System.out.println(String.format("%-30s|%-20s|%-12s|%-7s|%15s"
						        , db.getRs().getString(1)+" "+db.getRs().getString(2)
								, db.getRs().getString(3)
								, db.getRs().getString(4)								
								, db.getRs().getString(5).substring(0, 5)
								, db.getRs().getString(6)
						));
			}
		} catch (SQLException ex) {
			System.out.println("Wyst�pi� b��d !\n");
		}
	}
	
	
	
	public void print_wizyty_lekarza(int idl){
		String sql=String.format(""
				+ "select imie, nazwisko, nazwaspecjalnosci, date, godzina_od, idunique"
				+ " from v_grafik_dzienny v"
				+ " where days_from_tomorrow>0 and days_from_tomorrow<5"
				+ " and idlekarza=%d and idwizyty is null"
				+ " order by idlekarza, date, godzina_od;",idl);
		if (db.select(sql)) print_wizyty();
	}
	
	public void print_wizyty_spec(int ids){
		String sql=String.format(""
				+ "select imie, nazwisko, nazwaspecjalnosci, date, godzina_od, idunique"
				+ " from v_grafik_dzienny v"
				+ " where days_from_tomorrow>0 and days_from_tomorrow<5"
				+ " and idspecjalnosci=%d and idwizyty is null"
				+ " order by date, godzina_od, idlekarza;",ids);
		if (db.select(sql)) print_wizyty();
	}
	
	public void umowienie_wizyty(){
		System.out.println("Umawianie wizyty:");
		int idp=P.wybierz_pacjenta();
		//int idl=0;
		//int ids=0;
		String sl=""; 
		String uid="";
		
		if (idp<0) {
			System.out.println("Przerwanie !");
			return;
		} else {
			System.out.println("Czy chcesz wyszuka� konkretnego lekarza(L) czy specjalno��(S) [enter-rezygnacja] ");
			do{
				sl=rl.nextLine().toUpperCase();
				if (sl.equals("")) {
					System.out.println("Przerwanie !");
					return;
				}
				if (sl.equals("S")||sl.equals("L"))
					break;
				System.out.println("Nieprawid�owy wyb�r, wybierz L lub S...");
			} while(true);
			
				
			switch(sl){
			case "L": {
				int idl=L.wybierz();
				if (idl>0){
					print_wizyty_lekarza(idl);
				} 
				break;
			}
			case "S": {
				int ids=S.wybierz();
				if (ids>0) {
					print_wizyty_spec(ids);
				}
				break;
			}
			}
			do {		
				System.out.println("Podaj identyfikator wizyty, kt�r� chcesz um�wi� :");
				uid=rl.nextLine();
				if (uid.equals("")) {
					System.out.println("Przerwanie!");
					break;
				}
				if (W.sprawdz(uid)){
					if (W.umow_wizyte(idp, uid)) break;
				} else {
					System.out.println("Nie mo�na zarezerwowa�, ten termin jest ju� zaj�ty");
					System.out.println("Wybierz inny termin lub wci�nij ENTER aby zako�czy�...");
				}
			} while (true);
		};
		
		return;
	}

}
