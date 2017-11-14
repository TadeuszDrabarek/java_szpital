package szpital;

import java.sql.SQLException;
import java.util.Scanner;

public class LS {

	DB db;
	Scanner rl;
	Lekarze L;
	Specjalnosci S;
	
	LS(DB db, Scanner rl, Lekarze L, Specjalnosci S){
		this.db=db;
		this.rl=rl;
		this.S=S;
		this.L=L;
	}
	
	public String print(){
		String res="";
		
		res+=String.format("%-5s|%-45s|%-20s\n", "ID","Lekarz(id)","Specjalno��(id)");
		res+="---------------------------------------------------------------------------------\n";
		try {
			while (db.getRs().next()){
				res+=String.format("%-5s|%-45s|%-20s\n"
						        , db.getRs().getString(1)
								, db.getRs().getString(2)+
								  '('+db.getRs().getString(3)+')'
								, db.getRs().getString(4)+
								 '('+db.getRs().getString(5)+')'
						);
			}
		} catch (SQLException ex) {
			res+="Wyst�pi� b��d !\n";
		}
			
		return res;
	}

	@Override
	public String toString(){
		String sql="select idls, lekarz,idlekarza, nazwaspecjalnosci,idspecjalnosci from v_lekarzespecjalnosci ;";
		if (db.select(sql)){
			return this.print();
		};
		return "Co� posz�o nie tak... :-(";
	}
	
	public boolean check(String idl, String ids){
		String sql=String.format(
				"select * from lekarzespecjalnosci where idlekarza=%s and idspecjalnosci=%s;",
				idl, ids);
		return db.select(sql) && (db.next());
	}
	
	public boolean check(int idls){
		String sql=String.format(
				"select * from lekarzespecjalnosci where idls=%d;",
				idls);
		return db.select(sql) && (db.next());
	}
	
	public boolean del(int idls){
		String sql=String.format(
				"delete from lekarzespecjalnosci where idls=%d; "
				,idls);
		
		return db.execute(sql) && db.commit();
	}
	
	public boolean ask_del(){
		System.out.println("Odpinanie specjalno�ci od lekarzy, lista:");
		System.out.println(this);
		do {
			System.out.println("Podaj identyfikator wpisu do usuni�cia (<0 - przerwanie):");
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
	
	public boolean add(int idlekarza, int idspecjalnosci){
		String sql=String.format(
				"insert into lekarzespecjalnosci(idlekarza, idspecjalnosci) "
				+ "values(%d,%d);"
						,idlekarza, idspecjalnosci);
		
		return db.execute(sql) && db.commit();
	}
	
	public void ask_add(){
		int idlekarza;
		int idspecjalnosci;
		
		System.out.println("Dodawanie przypisania specjalno�ci do lekarza");
		System.out.println("Lista lekarzy:");System.out.println(L);
		
		do {
			System.out.println("Podaj identyfikator lekarza  (<0-wyj�cie) :"); idlekarza=Tools.nextInt(this.rl,"Identyfikator musi by� liczb�!");
			if (idlekarza<0){
				System.out.println("Przerwanie!");
				return;
			} 
			if (L.check(idlekarza)) break;
			else
				System.out.println("Nie ma takiego identyfikatora, spr�buj ponownie...");
		} while (true);
		
		System.out.println("Lista specjalno�ci:");System.out.println(S);
		do {
			System.out.println("Podaj identyfikator specjalno�ci  (<0-wyj�cie) :"); idspecjalnosci=Tools.nextInt(this.rl,"Identyfikator musi by� liczb�!");
			if (idspecjalnosci<0){
				System.out.println("Przerwanie!");
				return;
			} 
			if (S.check(idspecjalnosci)) break;
			else
				System.out.println("Nie ma takiego identyfikatora, spr�buj ponownie...");
		} while (true);
			
		if (this.add(idlekarza, idspecjalnosci)) System.out.println("Dodano !"); else System.out.println("Wyst�pi� b��d!");
			return;					
	}
}
