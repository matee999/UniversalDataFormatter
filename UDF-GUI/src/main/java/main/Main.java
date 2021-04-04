package main;

import app.UDFApplication;

public class Main {

	public static void main(String[] args) {
		try {
			Class.forName("formatter.FormatterImpl");
			UDFApplication.start();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
