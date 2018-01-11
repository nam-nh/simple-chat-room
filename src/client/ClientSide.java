package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.AbstractAction;

public class ClientSide {
	private String username, signal, message;
	private Socket socket;
	private volatile Scanner sc;
	private volatile PrintWriter out;
	private String[] onlineUsers;
	private ChatGUI clientView;
	
	public ClientSide(final String host, final int port, final ChatGUI clientView, final String username) {
		this.clientView = clientView;
		this.username = username;

		// event of disconnect button on client view
		clientView.disconnectBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				disconnect();
			}
					
		});
		// event of send button on client view
		clientView.sendBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				message = clientView.getInputTextArea();
				sendMessage("[" + username + "]" + ": " + message);
				clientView.setInputTextArea();
			}
		});
		
		//event of press enter input text area on client view
		clientView.addPressedEnterEvent(new AbstractAction(){
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				message = clientView.getInputTextArea();
				sendMessage("[" + username + "]" + ": " + message);
				clientView.setInputTextArea();
						
			}
		});
		try {
			//create a socket
			socket = new Socket(host, port);
			out = new PrintWriter(new BufferedWriter(
	                new OutputStreamWriter(socket.getOutputStream(), 
	                        "UTF-8")));
			clientView.appendChatTextArea("You are connect to: " + host + "\n");
			
			//set username
			sendMessage(username);
			
			sc = new Scanner(socket.getInputStream(), "UTF-8");
			out.flush();
			
			//check input stream
			checkStream();
		} catch (IOException e) {
			
			//event when server is current offline
			System.out.println("Server is current offline.");
			clientView.showDialog("Sorry! The server is current offline!");
			clientView.disconnectedUserView();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Exception 2");
			}
		}
		
	}
	
	//check input stream
	public void checkStream(){
		while(true){
			if(sc.hasNext()){
				signal = sc.nextLine();
				
				if (signal.contains("!@#$Online Users")){
					//set view of connected client
					clientView.connectedUserView(username);
					
					//update online users to client
					String temp = signal.replace("!@#$Online Users: [", "").replace("]","");
					onlineUsers = temp.split(", ");
					clientView.updateOnlineUsers(onlineUsers);
				} else if (signal.equals("!@#$%^online")) {
					
					//event when username is already online
					clientView.showDialog("This account has already online in another computer!");
					clientView.disconnectedUserView();
				} else if (signal.equals("!@#$%^disconnect")){
					
					//server is disconnected
					clientView.showDialog("Disconnected from server!");
					disconnect();
				} else if (signal.length() > 0){
					
					//append messages in chat view
					clientView.appendChatTextArea(signal + "\n");
				}
			}
		}
	}
	
	//send message to server
	public void sendMessage(String message){
		//handle null message
		if (message.trim().length() != username.length() + 3)
		out.println(message);
		out.flush();
	}
	
	//disconnect by client
	public void disconnect(){
		//send a disconnect signal
		String disconect = "/disconnect";
		out.println(disconect);
		out.flush();
		
		//update client view after disconnected
		clientView.removeOnlineUser();
		clientView.appendChatTextArea("You are disconnected!\n");
		clientView.disconnectedUserView();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception 5");
		}
	}

}
