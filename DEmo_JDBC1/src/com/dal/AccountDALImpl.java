package com.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.pojo.Account;
import com.util.DBUtil;

public class AccountDALImpl implements BankDAL {

	//
	private Connection con;
	private Statement stmt;
	private ResultSet rset;
	private PreparedStatement p1, p2, p3;

	private CallableStatement cstmt,wi;

	public AccountDALImpl() throws SQLException {
		con = DBUtil.getCon();
		stmt = con.createStatement();
		cstmt = con.prepareCall("{call transfer_funds(?,?,?,?,?)}");
		// out parameter

		cstmt.registerOutParameter(4, Types.DOUBLE);// parameter 4 data type is double JVM send this info to DB
		cstmt.registerOutParameter(5, Types.DOUBLE);// parameter 5

		p1 = con.prepareStatement("insert into accounts values(?,?,?,?)");
		p2 = con.prepareStatement("update accounts set name=? ,type=? ,bal=? where id=?");
		p3 = con.prepareStatement("delete from  accounts  where id=?");

		wi = con.prepareCall("{call withdraw(?,?,?)}");
		
	}

	@Override
	public String moneyTransfer(int sId, int rId, double amount) throws SQLException {

		cstmt.setInt(1, sId);// 1st IN parameter
		cstmt.setInt(2, rId);// 2nd IN parameter
		cstmt.setDouble(3, amount);// 3rd IN parameter
		// execute Stored Procedure

		cstmt.execute();
		return "Money Transfer : Sender Balance=" + cstmt.getDouble(4) + "   Reciver Balance:" + cstmt.getDouble(5);
	}

	public List<Account> getallAccounts() {
		// select * from book;
		try {
			List<Account> acc = new ArrayList<Account>();

			// stmt.executeQuery: read data: select query
			rset = stmt.executeQuery("select * from accounts");
			while (rset.next()) {
				acc.add(new Account(rset.getInt("id"), rset.getString("name"), rset.getString("type"),
						rset.getDouble("bal")));
			}
			return acc;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public String deposit(int sid, double amt) {

		try {

			PreparedStatement d1;
			d1 = con.prepareStatement("update accounts set bal=bal+ ? where id=?");

			d1.setDouble(1, amt);
			d1.setInt(2, sid);
			int i = d1.executeUpdate();
			AccountDALImpl actDal = new AccountDALImpl();

			List<Account> dd = actDal.getallAccounts();
			for (Account a : dd) {
				if (a.getId() == sid) {
					return "New Balance = " + a.getBalance();
				}
			}
			return null;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int addAccount(Account obj) {
		try {
			// add 1st parameter to pstmt1 :bookid
			p1.setInt(1, obj.getId());
			// add 2 parameter to pstmt1 :title
			p1.setString(2, obj.getName());

			p1.setString(3, obj.getType());

			p1.setDouble(4, obj.getBalance());

			int i = p1.executeUpdate();// Write: Insert ,Update Delete
			System.out.println("---inserted obj:" + obj);
			return i;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	public int updateAccount(Account obj) {

		try {
			// add 1st parameter to pstmt1 :
			p2.setString(1, obj.getName());
			// add 2 parameter to pstmt1 :
			p2.setString(2, obj.getType());

			p2.setDouble(3, obj.getBalance());
			p2.setInt(4, obj.getId());

			int i = p2.executeUpdate();
			System.out.println("---updated obj:" + obj);
			return i;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public int deleteAccount(int id)
	{
		try {
			
			p3.setInt(1, id);
             int i= p3.executeUpdate();
              return i;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public String withdraw(int did,double amt)
	{
		try 
		{
			wi.setInt(1, did);
			wi.setDouble(2, amt);
			wi.registerOutParameter(3,Types.DOUBLE);
			
			wi.execute();
			return "Balance after withdrawl =" + wi.getDouble(3);
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

}
