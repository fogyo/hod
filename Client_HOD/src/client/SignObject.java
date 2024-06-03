package client;

import java.util.Scanner;

import contract.UserTransfer;

public class SignObject {
	
	private boolean sign;
	Scanner sc = new Scanner(System.in);
	
	public boolean sign() {
		System.out.println("If u want to sign up: press 1 \n" + "If u want to log in: press 2");
		boolean correct = false;
		while(!correct) {
			String log = sc.nextLine();
			try {
				int num = Integer.parseInt(log);
				if (num == 1) {
					sign = true;
					correct = true;
				}
				else if(num == 2) {
					sign = false;
					correct = true;
				}
			} catch (NumberFormatException e) {
			}
		}	
		return sign;
	}
	
	public UserTransfer enterInfo(boolean signing) {
		System.out.println("Enter name");
		String username = sc.nextLine();
		System.out.println("Enter password");
		String password = sc.nextLine();
		UserTransfer ut = new UserTransfer(username, password, signing);
		return ut;
	}


}
