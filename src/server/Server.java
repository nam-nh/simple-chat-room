package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	
	private static Scanner sc;
	private static ServerSocket serverSocket;
	private static PrintWriter out;
	private static ArrayList<Socket> ConnectionArray = new ArrayList<Socket>();
	private static ArrayList<String> CurrentUsers = new ArrayList<String>();
	private static ServerGUI view;
	
	public static void main(String[] args) {
		//create server view
		view = new ServerGUI();
		
		//create server button listener
		view.connectBtnListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//update view after click button
				view.createdServerView();
				
				//create a thread for server
				Thread server = new Thread(new Runnable(){
					public void run() {
						try {
							serverSocket = new ServerSocket(4447);
							view.setActivityTextArea("Server connected! Waiting for clients...\n");
							
							while(true){
								Socket socket = serverSocket.accept();
								
								//update online users to server view
								if(AddUserName(socket, view)){
									view.updateOnlineUsers(CurrentUsers);
									view.setActivityTextArea("Client connected from: " + socket.getLocalAddress().getHostName() + "\n");
									
									// create another thread for signal return from server
									ServerReturn chat = new ServerReturn(socket, view);
									Thread thread = new Thread(chat);
									thread.start();
								}
							}	
						} catch (IOException e1) {
							if (e1.getMessage().equals("Address already in use: JVM_Bind")){
								view.showDialog("Server is current running!");
								view.disconnectedServerView();;
							} else if(e1.getMessage().equals("socket closed")){
								System.out.println("Server disconnected!");
							} else {
								e1.printStackTrace();
							}
						}
					}	
				});
				server.start();
			}
		});
		
		//disconnect button listener
		view.disconnectBtnListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				view.setActivityTextArea("Server disconnected.\n");
				
				//send disconnect signal to all clients
				for (int i = 0; i < Server.getSizeConnectionArray(); i++){
					try {
						out = new PrintWriter(Server.getConnection(i).getOutputStream());
						out.println("!@#$%^disconnect");
						out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				view.removeOnlineUser();
				disconnectServer();
			}
			
		});
	}
	
	public static boolean AddUserName(Socket socket, ServerGUI view) {
		try {
			sc = new Scanner(socket.getInputStream(), "UTF-8");
			String userName = sc.nextLine();
			
			//check if username has already online
			if (CurrentUsers.contains(userName)){
				out = new PrintWriter(socket.getOutputStream());
				out.println("!@#$%^online");
				out.flush();
				return false;
			} else {
				ConnectionArray.add(socket);
				CurrentUsers.add(userName);
				
				// send current online users and connected user notify to clients
				view.setActivityTextArea(userName + " connected!\n");
				for (int i = 0; i < ConnectionArray.size(); i++){
					Socket tempSocket = ConnectionArray.get(i);
					if(!tempSocket.isClosed()) {
						PrintWriter out = new PrintWriter(tempSocket.getOutputStream());
						out.println("!@#$Online Users: " + CurrentUsers);
						out.println(userName + " connected!");
						out.flush();
					}
				}
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static void disconnectServer(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1){
		}
		view.disconnectedServerView();
	}
	
	public static int getSizeConnectionArray(){
		return ConnectionArray.size();
	}
	
	public static void removeConnection(int i){
		ConnectionArray.remove(i);
	}
	
	public static Socket getConnection(int i){
		return ConnectionArray.get(i);
	}
	
	public static ArrayList<String> getCurrentUsers(){
		return CurrentUsers;
	}
	
	public static String getUser(int i){
		return CurrentUsers.get(i);
	}
	
	public static void removeUser(int i){
		CurrentUsers.remove(i);
	}
}