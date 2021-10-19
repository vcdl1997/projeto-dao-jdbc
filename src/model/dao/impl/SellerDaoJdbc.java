package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.Dao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJdbc implements Dao<Seller>{
	
	private Connection conn;
	
	public SellerDaoJdbc(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = this.conn.prepareStatement(""
					+ "UPDATE seller\r\n"
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?\r\n"
					+ "WHERE Id = ?"
			);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected == 0) throw new DbException("Error trying to update record");
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = this.conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			ps.setInt(1, id);			
			
			int rowsAffected = ps.executeUpdate();
			if(rowsAffected == 0) throw new DbException("Error trying to delete record");
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = this.conn.prepareStatement(""
				+ "SELECT s.*, d.Name as DepartmentName\r\n"
				+ "FROM seller as s\r\n"
				+ "INNER JOIN department as d ON d.Id = s.DepartmentId\r\n"
				+ "where s.Id = ?",
				ResultSet.TYPE_SCROLL_SENSITIVE, 
                ResultSet.CONCUR_UPDATABLE
            );
			ps.setInt(1, id);
			rs = ps.executeQuery();
			rs.first();
			
			return new Seller(
				rs.getInt("Id"),
				rs.getString("Name"),
				rs.getString("Email"),
				rs.getDate("BirthDate"),
				rs.getDouble("BaseSalary"),
				new Department(rs.getInt("DepartmentId"), rs.getString("DepartmentName"))
			);
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
	}

	@Override
	public List<Seller> findAll() {
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = this.conn.createStatement();
			rs = st.executeQuery(""
				+ "SELECT s.*, d.Name as DepartmentName "
				+ "FROM seller as s "
				+ "INNER JOIN department as d ON d.Id = s.DepartmentId"
			);
			
			List<Seller> listSeller = new ArrayList<>();
			
			while(rs.next()) {
				listSeller.add(
					new Seller(
						rs.getInt("Id"),
						rs.getString("Name"),
						rs.getString("Email"),
						rs.getDate("BirthDate"),
						rs.getDouble("BaseSalary"),
						new Department(rs.getInt("DepartmentId"), rs.getString("DepartmentName"))
					)
				);
			}
			
			return listSeller;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller insert(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = this.conn.prepareStatement(""
				+ "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId)\r\n"
				+ "VALUES (?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS
			);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected == 0) throw new DbException("Error trying to insert record");
			
			ResultSet rs = ps.getGeneratedKeys();
			
			while(rs.next()) {
				obj.setId(rs.getInt(1));
				return obj;
			}	
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
		
		return null;
	}

}
