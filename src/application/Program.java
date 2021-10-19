package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import db.DB;
import db.DbException;
import model.dao.DaoFactory;
import model.dao.impl.SellerDaoJdbc;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) throws ParseException{
		Locale.setDefault(Locale.US);
		Connection conn = DB.getConnection();
		
		try {
			conn.setAutoCommit(false);
			
			Department dp = new Department(1, "Computers");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Seller seller = new Seller("Arthur", "arthur@gmail.com", sdf.parse("21/03/1980"), 3500.50, dp);
			
			SellerDaoJdbc sellerDaoJdbc = DaoFactory.createSellerDao();
			seller = sellerDaoJdbc.insert(seller);
			System.out.println("seller successfully inserted!\n");

			seller.setName("Victor");
			seller.setEmail("victor@gmail.com");
			sellerDaoJdbc.update(seller);
			System.out.println("seller successfully changed!\n");
			
			Seller seller2 = sellerDaoJdbc.findById(1);
			System.out.println(seller2.toString());
			System.out.println("seller successfully located!\n");

			conn.commit();
		}catch(SQLException e) {
			
			throw new DbException(e.getMessage());
		}catch(DbException e) {
			
			e.printStackTrace();
		}finally {
			DB.closeConnection();
		}
	}

}