package szpital;

import java.util.Scanner;

public class App {
	Scanner rl;
	DB db;
	
	private String menu(String[] options){
		System.out.println("Dost�pne opcje:");
		for (String option : options){
			System.out.println(option);
		}	
		System.out.println("Wybierz :");
		return rl.nextLine();
		
	}
		
	App(){
		rl=Tools.In();
		
		db=DB.createDBConnection("jdbc:mysql://localhost:3306/szpital?useSSL=false"
				               , "root"
				               , "root");
		System.out.print("��czenie z baz� danych...");
		if (db==null) {
			System.out.println("B��d, wyj�cie z programu");
			return;
		}
		System.out.println("OK");
		
		Lekarze L=new Lekarze(db,rl);
		Pacjenci P=new Pacjenci(db,rl);
		Specjalnosci S=new Specjalnosci(db,rl);
		LS H=new LS(db,rl,L,S);
		Wizyty W=new Wizyty(db,rl);
		Grafik G=new Grafik(db,rl,L,S,P,W);
		
		String menulevel="M";
		String[] mainoptions={"EXIT, Q, ! -Wyj�cie","L-lekarze","P-Pacjenci", "S-Specjalno�ci", "H-Definiowanie specjalno�ci lekarzy","G-Grafik i wizyty"};
		String[] lekarzeoptions={"LL-Lista lekarzy","DL-Dodawanie lekarza","UL-Usuwanie lekarza","M-Powr�t do menu g�ownego"};
		String[] pacjencioptions={"LP-Lista pacjent�w","DP-Dodawanie pacjenta","M-Powr�t do menu g�ownego"};
		String[] specjalnoscioptions={"LS-S�ownik specjalno�ci","DS-Dodawanie specjalno�ci","US-Usuwanie specjalno�ci","M-Powr�t do menu g�ownego"};
		String[] lsoptions={"LH-Lista specjalno�ci lekarzy","DH-Dopisanie specjalno�ci do lekarza","UH-Usuwanie spelno�ci lekarza","M-Powr�t do menu g�ownego"};
		String[] grafikoptions={"GD-Grafik dzienny", "GL-grafik lekarza","UM-Um�wienie wizyty","WD-Lista wizyt w dniu","WL-Lista wizyt lekarza","WP-Lista wizyt pacjenta", "WW-Wizyty wszystkie","M-Powr�t do menu g�ownego"};
		boolean exit=false;
		String w;
		String[] m={};
		
		do {
			
			switch (menulevel){
			case "M":m=mainoptions;break;
			case "L":m=lekarzeoptions;break;
			case "P":m=pacjencioptions;break;
			case "S":m=specjalnoscioptions;break;
			case "H":m=lsoptions;break;
			case "G":m=grafikoptions;break;
			}; 
			w=this.menu(m).toUpperCase();
			
			switch(w){
			case "EXIT":   
			case "!": 
			case "Q": exit=true;break;
			case "M": menulevel="M"; break;
			case "L": menulevel="L"; break;
			case "P": menulevel="P"; break;
			case "S": menulevel="S"; break;
			case "H": menulevel="H"; break;
			case "G": menulevel="G"; break;
			
			case "DL": L.ask_add(); break;
			case "UL": L.ask_del(); break;
			case "LL": System.out.println(L);break;
			
			case "DP": P.ask_add(); break;			
			case "LP": System.out.println(P);break;
			
			case "DS": S.ask_add(); break;
			case "US": S.ask_del(); break;
			case "LS": System.out.println(S);break;
			
			case "DH": H.ask_add(); break;
			case "UH": H.ask_del(); break;
			case "LH": System.out.println(H);break;
			
			case "GD": G.grafik_na_dzien();break;
			case "GL": G.grafik_lekarza();break;
			case "UM": G.umowienie_wizyty(); break;
			
			case "WW": W.wizyty_wzystkie_um();break;
			case "WL":{
				int idl=L.wybierz();
				if (idl>0) W.wizyty_lekarza_um(idl);
			}
			case "WP":{
				int idp=P.wybierz_pacjenta();
				if (idp>0) W.wizyty_pacjenta_um(idp);
			}
			
			default: System.out.println("Komenda nierozpoznana, spr�buj ponownie!"); break;
			}
		} while (!exit);
		
		db.closeConnection();
		rl.close();
		
		System.out.println("Koniec programu");
		
	}

	public static void main(String[] args) {
		new App();			
	}

}
