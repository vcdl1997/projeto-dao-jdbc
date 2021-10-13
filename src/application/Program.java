package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) throws ParseException{
		Locale.setDefault(Locale.US);
		
		Department dp = new Department(1, "Expedition");
		System.out.println(dp.toString());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Seller seller = new Seller(1, "Arthur", "arthur@gmail.com", sdf.parse("21/03/1980"), 3500.50, dp);
		System.out.println(seller.toString());
	}

}
