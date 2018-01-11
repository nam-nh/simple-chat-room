package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
	private String username, usernamePattern, passwordPattern;
	private char[] password, cfPassword;
	private LoginGUI loginView;
	private SignUpGUI signUpView;
	private Connection conn;
	
	public Login(final LoginGUI loginView, final SignUpGUI signUpView, final ChatGUI clientView) {
		this.loginView = loginView;
		this.signUpView = signUpView;
		usernamePattern = "[A-Za-z0-9_]+{6,50}";
		passwordPattern = ".{6,50}";
		loginView.setVisible(true);
		//connect to the database
		try {
			//Get a connection to database
			// TODO change your own database address
			String dbURL = "jdbc:sqlserver://localhost;databaseName=ChatRoomDB;user=sa;password=namga456";
			conn = DriverManager.getConnection(dbURL);
		} catch (SQLException ex) {
			loginView.showDialog("Cannot connect to database!");
			System.exit(0);
		}
		
		//login button event listener
		loginView.addLoginBtnListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				username = loginView.getUsername();
				password = loginView.getPassword();
				if (checkUser(username, password)){
					//login successful
					loginView.dispose();
					signUpView.dispose();
					clientView.setUsername(username);
					clientView.setVisible(true);
					clientView.connectBtnListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							Thread thread = new Thread(new Runnable(){
								@Override
								public void run() {
									new ClientSide("localhost", 4447, clientView, username);
								}
							});
							thread.start();
						}
								
					});
				}
			}
		});
		
		//sign up option
		loginView.addSignUpBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				signUpView.setBounds(loginView.getBounds());
				signUpView.setVisible(true);
				loginView.setVisible(false);
			}
			
		});
		//sign up button listener
		signUpView.addSignUpBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				username = signUpView.getUsername();
				password = signUpView.getNewPassword();
				cfPassword = signUpView.getCfPassword();
				
				if(!username.matches(usernamePattern)){
					signUpView.showDialog("Username must be at least 6 characters with no whitespace and specific characters!");
				} else if(!String.valueOf(password).matches(passwordPattern)) {
					signUpView.showDialog("Password must be at least 6 characters!");
				}else if(String.valueOf(password) == null || String.valueOf(cfPassword) == null){
					signUpView.showDialog("Please fill all required field!");
				} else if(!String.valueOf(password).equals(String.valueOf(cfPassword))){
					signUpView.showDialog("Password and Confirm Password is not match!");
				} else {
					boolean exist = checkExistUser(username);
					if (!exist) {
						if(addNewUser(username, password)){
							signUpView.showDialog("Create account successful!");
							loginView.setBounds(signUpView.getBounds());
							signUpView.setVisible(false);
							loginView.setVisible(true);
						} else {
							signUpView.showDialog("Error!");
						}
					}
				}
				
			}
			
		});

	}
	
	public boolean checkExistUser(String username){
		try {
			PreparedStatement myStmt = conn.prepareStatement("Select username from Users where username = ?");
			myStmt.setString(1, username);
			
			ResultSet myRs = myStmt.executeQuery();
			
			if(!myRs.next()){
				return false;
			} else {
				signUpView.showDialog("This username has been taken! Please choose another.");
				return true;
			}
		} catch (SQLException e) {
			signUpView.showDialog("Cannot connect to database!");
			return false;
		}
		
		}
		
		public boolean addNewUser(String username, char[] password){
			try {
				PreparedStatement myStmt = conn.prepareStatement("insert into Users values (?,?,?)");
				//Set the parameters
				myStmt.setString(1, username);
				myStmt.setString(2, String.valueOf(password));
				myStmt.setTimestamp(3, new java.sql.Timestamp(new java.util.Date().getTime()));
				
				//Execute SQL query
				myStmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				signUpView.showDialog("Cannot connect to database!");
				return false;
			}
		}
		
		public boolean checkUser(String username, char[] password){
			try {
				PreparedStatement myStmt = conn.prepareStatement("select username, password from Users "
						+ "where username = ? and password = ?");
				//Set the parameters
				myStmt.setString(1, username);
				myStmt.setString(2, String.valueOf(password));
				
				//Execute SQL query
				ResultSet myRs = myStmt.executeQuery();
				if(!myRs.next()){
					loginView.showDialog("Wrong username or password!");
					return false;
				} else {
					return true;
				}
			} catch (SQLException e) {
				loginView.showDialog("Cannot connect to database!");
				return false;
			}
		}
		

}
