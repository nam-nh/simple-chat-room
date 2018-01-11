package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class ServerReturn implements Runnable{
	private Socket socket;
	private ServerGUI view;
	private Scanner input;
	private PrintWriter out;
	private String message, disconnectedUser;
	private int userId, receivedUserId;
	private Connection conn;
	
	public ServerReturn(Socket socket, ServerGUI view){
		this.socket = socket;
		this.view = view;
	}

	@Override
	public void run() {
		try {
			//Get a connection to database
			// TODO change your own database address
			String dbURL = "jdbc:sqlserver://localhost;databaseName=ChatRoomDB;user=sa;password=namga456";
			conn = DriverManager.getConnection(dbURL);
			try{
				try{
					input = new Scanner(socket.getInputStream(), "UTF-8");
					out = new PrintWriter(new BufferedWriter(
			                new OutputStreamWriter(socket.getOutputStream(), 
			                        "UTF-8")));
					
					while(true){
						if(!input.hasNext()){
							return;
						}
						
						message = input.nextLine();
						
						// handle disconnect event from client
						if (message.equals("/disconnect")){
							removeDisconectUser(socket);
						} else {
							// handle sent message event
							view.appendChatTextArea(message + "\n");
							
							//add message to the database
							String username = message.split(":")[0].replace("[","").replace("]","");
							//get user_id
							userId = getUserId(username); 
							for (int i = 0; i < Server.getSizeConnectionArray(); i++){
								try {
									Socket tempSocket = Server.getConnection(i);
									PrintWriter tempOut = new PrintWriter(new BufferedWriter(
							                new OutputStreamWriter(tempSocket.getOutputStream(), 
							                        "UTF-8")));
									tempOut.println(message);
									tempOut.flush();
									//get received user id
									receivedUserId = getUserId(Server.getUser(i));
									if (userId != receivedUserId) {
										PreparedStatement myStmt = conn.prepareStatement("insert into ChatLog values (?,?,?,?)");
										//Set the parameters
										myStmt.setInt(1, userId);
										myStmt.setInt(2, receivedUserId);
										myStmt.setString(3, message.replace("["+ username +"]: ", ""));
										myStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
										
										//Execute SQL query
										myStmt.executeUpdate();
									} else if (Server.getSizeConnectionArray() == 1){
										//if only 1 user is online
										PreparedStatement myStmt = conn.prepareStatement("insert into ChatLog values (?,?,?,?)");
										//Set the parameters
										myStmt.setInt(1, userId);
										myStmt.setNull(2, java.sql.Types.INTEGER);
										myStmt.setString(3, message.replace("["+ username +"]: ", ""));
										myStmt.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
										
										//Execute SQL query
										myStmt.executeUpdate();
									}
								} catch (java.net.SocketException sockErr){
									removeDisconectUser(Server.getConnection(i));
									if (Server.getSizeConnectionArray() == 1){
										//if only 1 user is online
										PreparedStatement myStmt = conn.prepareStatement("insert into ChatLog values (?,?,?,?)");
										//Set the parameters
										myStmt.setInt(1, userId);
										myStmt.setNull(2, java.sql.Types.INTEGER);
										myStmt.setString(3, message.replace("["+ username +"]: ", ""));
										myStmt.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
										
										//Execute SQL query
										myStmt.executeUpdate();
									}
								}
							}
						}
					}
				}
				finally {
					socket.close();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		} catch (SQLException ex) {
			view.showDialog("Cannot connect to database!");
			System.exit(0);
		}
	}
	
	public void removeDisconectUser(Socket socket) throws IOException {
		for (int i = 0; i < Server.getSizeConnectionArray(); i++){
			if(socket == Server.getConnection(i)){
					
				// remove disconnected user
				Server.removeConnection(i);
				disconnectedUser = Server.getUser(i);
				Server.removeUser(i);
					
				//update server view
				view.setActivityTextArea(disconnectedUser + " disconnected!\n");
				view.updateOnlineUsers(Server.getCurrentUsers());
			}
		}
		
		// send current users to client
		for (int i = 0; i < Server.getSizeConnectionArray(); i++){
			Socket tempSocket = Server.getConnection(i);
			out = new PrintWriter(new BufferedWriter(
	                new OutputStreamWriter(tempSocket.getOutputStream(), 
	                        "UTF-8")));
			out.println("!@#$Online Users: " + Server.getCurrentUsers());
			out.println(disconnectedUser + " disconnected!");
			out.flush();
		}
	}
	
	public int getUserId(String username){
		try {
			PreparedStatement myStmt = conn.prepareStatement("Select user_id from Users where username = ?");
			//3. Set the parameters
			myStmt.setString(1, username);
			
			//4. Execute SQL query
			ResultSet myRs = myStmt.executeQuery();
			
			//5. Display the result set
			if(myRs.next()){
				return myRs.getInt("user_id");
			} else {
				return 0;
			}
		} catch(SQLException e) {
			view.showDialog("Cannot connect to database!");
			return 0;
		}
	}
}
