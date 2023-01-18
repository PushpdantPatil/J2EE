package com.tester;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.dal.AccountDALImpl;
import com.pojo.Account;
import com.util.DBUtil;

public class TestBankAccount {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			DBUtil.openConnection();
			Scanner sc = new Scanner(System.in);
			int ch;

			AccountDALImpl actDal = new AccountDALImpl();
            System.out.println("1:Show all Accounts\r\n"
            		+ "2:Add New Account\r\n"
            		+ "3:Update/Edit Account\r\n"
            		+ "4:Delete Account\r\n"
            		+ "5:Deposite for specific account\r\n"
            		+ "6:Withdraw for specific account\r\n"
            		+ "7:Transfer Money from one account to Other Account(use stored procedure)\r\n"
            		+ "");
			

			do {
				System.out.println("Enter choice");
				ch = sc.nextInt();

				switch (ch) 
				{
				
				case 1: List<Account> list = actDal.getallAccounts();
						list.forEach(b -> System.out.println(b));
						break;
						
				case 2: System.out.println("Enter id ,name.type and balance");
						System.out.println(actDal.addAccount(new Account(sc.nextInt(),sc.next(),sc.next(),sc.nextDouble()))+" Rows inserted");
						break;
						
						
				case 3:System.out.println("Enter id ,name.type and balance");
						System.out.println(actDal.updateAccount(new Account(sc.nextInt(),sc.next(),sc.next(),sc.nextDouble()))+" Rows updated");
						break;
				
				case 4:System.out.println("Enter Account id to delete ");
				       System.out.println(actDal.deleteAccount(sc.nextInt())+" Rows Deleted");
				       break;
				
				case 5:System.out.println("Enter id and amount to deposit");
				 	    System.out.println(actDal.deposit(sc.nextInt(), sc.nextDouble()));
				 	    break;
				case 6:System.out.println("Enter id and amount to withdraw");
				       System.out.println(actDal.withdraw(sc.nextInt(), sc.nextDouble()));
				       break;
				case 7:
					System.out.println("Enter SenderID Reciver ActID Amount");
					String status = actDal.moneyTransfer(sc.nextInt(), sc.nextInt(), sc.nextDouble());
					System.out.println(status);
					break;
				}
			} while (ch != 0);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
