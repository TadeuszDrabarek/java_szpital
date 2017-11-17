package szpital;

import java.sql.SQLException;
import java.util.Scanner;

public class Specjalnosci {

	DB db;
	Scanner rl;
	
	Specjalnosci(DB db, Scanner rl){
		this.db=db;
		this.rl=rl;
	}
	
	public String print(){
		String res="";
		
		res+=String.format("%-5s|%-36s\n", "ID","Nazwa specjalnoœci");
		res+="------------------------------------------\n";
		try {
			while (db.getRs().next()){
				res+=String.format("%-5s|%-36s\n"
						        , db.getRs().getString(1)
								, db.getRs().getString(2)							
						);
			}
		} catch (SQLException ex) {
			res+="Wyst¹pi³ b³¹d !\n";
		}
			
		return res;
	}

	@Override
	public String toString(){
		String sql="select idspecjalnosci, nazwaspecjalnosci from specjalnosci where isvalid=1;";
		if (db.select(sql)){
			return this.print();
		};
		return "Coœ posz³o nie tak... :-(";
	}
	
	public boolean check(int id){
		String sql=String.format(
				"select * from specjalnosci where idspecjalnosci='%s' and isvalid=1;",
				id);
		return db.select(sql) && (db.next());
	}
	
	public boolean del(int id){
		String sql=String.format(
				"update specjalnosci set isvalid=0 where idspecjalnosci=%d; "
				,id);
		
		return db.execute(sql) && db.commit();
	}
	
	public boolean ask_del(){
		System.out.println("Usuwanie specjalnosci, lista spacjalnosci:");
		System.out.println(this);
		do {
			System.out.println("Podaj identyfikator specjalnoœci do usuniêcia (<0 - przerwanie):");
			int id=Tools.nextInt(this.rl, "Identyfikator musi byc liczb¹!");
			if (id<0){
				System.out.println("Przerwanie!");
				break;
			}
			if (check(id)) {
				//System.out.println("Znaleziono taki ID!");
				boolean res=del(id);
				if (res)
					System.out.println("Usuniêto!");
				else
					System.out.println("Wyst¹pi b³¹d!");
				return res;
			}
			System.out.println("Nie znaleziono takiego ID, spróbuj ponownie...");
		} while (true);
		return true;
	}
	
	public boolean add(String nazwaspecjalnosci){
		String sql=String.format(
				"insert into specjalnosci(nazwaspecjalnosci, isvalid) "
				+ "values('%s',1);"
						,nazwaspecjalnosci);
		
		return db.execute(sql) && db.commit();
}
	
	public void ask_add(){
		System.out.println("Dodawanie Specjalnoœci");
		System.out.println("Na ka¿dym etapie mo¿esz nacisn¹æ ENTER w pustej linii, aby przerwaæ dodawanie");
		System.out.println("Podaj nazwê secjalnoœci :"); String nazwaspecjalnosci=rl.nextLine();
		if (!nazwaspecjalnosci.equals("")) {
			if (this.add(nazwaspecjalnosci)) System.out.println("Dodano !"); else System.out.println("Wyst¹pi³ b³¹d!");
			return;		
		}
		System.out.println("Przerwanie!");
		return;
				
	}
}
