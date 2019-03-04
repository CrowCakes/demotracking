package com.example.demotracking.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ConnectionManager {
	
	private final static String address = "127.0.0.1";
	private final static int port = 9797;
	/*
	private final static String password = "password";
	private final static String db = "pcrental";
	*/
	
	Socket s = null;
	BufferedReader input = null;
	PrintWriter out = null;
	
	/**
	 * Connects to the server at localhost port. Returns 1 if the connection was
	 * successful, and 0 if it was not.
	 * */
	public int connect() {
		//connect to the server at localhost port
		try {
			s = new Socket(address, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		
		//initialize I/O streams
		try {
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		
		//System.out.println("Connection to server successful");
		return 1;
	}
	
	/**
	 * Disconnects from the server. Returns 1 if successful, 0 if it was not.
	 * */
	public int disconnect() {
		try {
			input.close();
			out.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		//System.out.println("Disconnection from server successful");
		return 1;
	}
	
	/**
	 * Sends the input string to the server. 
	 * Returns the server's response or an error message if the client can't use the connection.
	 * @param query
	 * @return
	 */
	public String send(String query) {
		//out.println(String.valueOf(length));
		out.println(query);
		
		StringBuilder response = new StringBuilder();
		String foo = new String();
		System.out.println("-- send --");
		try {
			while ((foo = input.readLine()) != null) {
				//System.out.println(foo);
				response.append(foo);
				response.append('\n');
			}
			System.out.println("success!");
		} catch (IOException ex) {
			foo = "Client error: " + ex;
			System.out.println(foo);
			System.out.println("Response collected:");
			System.out.println(response);
			//ex.printStackTrace();
			//return foo;
			if (response.toString().equals("")) return foo;
			else return response.toString();
		}
		return response.toString();
	}
}
