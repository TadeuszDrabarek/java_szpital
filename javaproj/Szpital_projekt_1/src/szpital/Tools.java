package szpital;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Tools {
	public static Scanner In(){
		Scanner rl=new Scanner(new FilterInputStream (System.in){
			@Override
			public void close(){};
		});
		rl.useLocale(Locale.US);
		return rl;
	}
	
	public static String getDayName(int day){
		String[] days={"Poniedziałek","Wtorek","Środa","Czwartek","Piątek","Sobota","Niedziela"};
		return days[day-1];
	}
	
	public static OutputStreamWriter openFileForWriteUTF_8(String fn) throws FileNotFoundException{
		return new OutputStreamWriter(new FileOutputStream(fn), StandardCharsets.UTF_8);	
	}
	
	public static Scanner openFileForReadUTF_8(String fn) throws FileNotFoundException{
		return new Scanner(new File(fn),"UTF-8");
	}
	
	public static String jakieDane(String wej){
		String wynik="STRING";
		try {			
			if ((wej.toLowerCase().equals("true"))||(wej.toLowerCase().equals("false")))
				wynik="BOOLEAN";
			Double.parseDouble(wej);
			wynik="DOUBLE";
			Long.parseLong(wej);
			wynik="LONG";
			Integer.parseInt(wej);
			wynik="INT";
		}
		catch (Exception exc) {	
		}
		return wynik;
	}
	
	public static int nextInt(Scanner in,String errorInfo){
		String w;
		boolean exit=false;
		do{
			w=in.nextLine();
			exit=jakieDane(w).equals("INT");
			if (!exit)
					System.out.println(errorInfo);
		} while (!exit);
		return Integer.parseInt(w);
	}
	
	public static int nextInt(Scanner in,String errorInfo, int zakres_od, int zakres_do){
		String w;
		int l=0;
		boolean exit=false;
		do{
			w=in.nextLine();
			if (jakieDane(w).equals("INT")){
				l=Integer.parseInt(w);
				if ((l>=zakres_od) && (l<=zakres_do))
					exit=true;
				else System.out.println("Liczba spoza zakresu <"+zakres_od+".."+zakres_do+">");
			}
			if (!exit)
					System.out.println(errorInfo);
		} while (!exit);
		return l;
	}
	
	public static long nextLong(Scanner in,String errorInfo){
		String w;
		boolean exit=false;
		do{
			w=in.nextLine();
			exit=jakieDane(w).equals("LONG") || jakieDane(w).equals("INT");
			if (!exit)
					System.out.println(errorInfo);
		} while (!exit);
		return Long.parseLong(w);
	}
	
	public static double nextDouble(Scanner in,String errorInfo){
		String w;
		boolean exit=false;
		do{
			w=in.nextLine();
			exit=jakieDane(w).equals("DOUBLE") || jakieDane(w).equals("INT");
			if (!exit)
					System.out.println(errorInfo);
		} while (!exit);
		return Double.parseDouble(w);
	}
}