package client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel header, usernameLabel, passwordLabel, label;
	private JTextField username;
	private JPasswordField password;
	private JButton loginBtn, signupBtn;
	public LoginGUI(){
		setSize(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Login");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//row 1
		header = new JLabel("LOGIN");
		header.setFont(new Font("San-Serif", Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridy = 0;
		gbc.insets = new Insets(10,10,20,10);
		this.add(header, gbc);
		
		//row 2
		usernameLabel = new JLabel("Username: ");
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.1;
		gbc.gridy = 1;
		gbc.insets = new Insets(5,10,5,10);
		this.add(usernameLabel, gbc);
		
		username = new JTextField();
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.weightx = 0.8;
		this.add(username, gbc);
		
		//row 3
		passwordLabel = new JLabel("Password: ");
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.1;
		gbc.gridy = 2;
		this.add(passwordLabel, gbc);
		
		password = new JPasswordField();
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.gridy = 2;
		gbc.weightx = 0.8;
		this.add(password, gbc);
		
		//row 4
		loginBtn = new JButton("Login");
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy = 3;
		gbc.weightx = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20,10,5,10);
		this.add(loginBtn, gbc);
			
		//row 5
		label = new JLabel("or");
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridy = 4;
		gbc.insets = new Insets(0,10,5,10);
		this.add(label, gbc);
		
		//row 6
		signupBtn = new JButton("Sign Up");
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy = 5;
		gbc.weightx = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(signupBtn, gbc);
		
		setResizable(false);
	}
	
	// button listener
	public void addLoginBtnListener(ActionListener a){
		loginBtn.addActionListener(a);
	}
	
	public void addSignUpBtnListener(ActionListener a){
		signupBtn.addActionListener(a);
	}
	
	public void showDialog(String message){
		JOptionPane.showMessageDialog(this, message);
	}
	
	//setter & getter
	public String getUsername() {
		return username.getText();
	}

	public char[] getPassword() {
		return password.getPassword();
	}
	

}
