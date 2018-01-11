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

public class SignUpGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel header, usernameLabel, passwordLabel, cfPasswordLabel;
	private JTextField username;
	private JPasswordField password, cfPassword;
	private JButton signupBtn;
	public SignUpGUI(){
		setSize(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Login");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//row 1
		header = new JLabel("SIGN UP");
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
		cfPasswordLabel = new JLabel("Confirm Password: ");
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.1;
		gbc.gridy = 3;
		this.add(cfPasswordLabel, gbc);
		
		cfPassword = new JPasswordField();
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gbc.gridy = 3;
		gbc.weightx = 0.8;
		this.add(cfPassword, gbc);
			
		//row 5
		signupBtn = new JButton("Sign Up");
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.gridy = 4;
		gbc.weightx = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(20,10,5,10);
		this.add(signupBtn, gbc);
		
		setResizable(false);
	}
	
	public void showDialog(String message){
		JOptionPane.showMessageDialog(this, message);
	}
	
	// button listener
	public void addSignUpBtnListener(ActionListener a){
		signupBtn.addActionListener(a);
	}
	
	//setter & getter
	public String getUsername() {
		return username.getText();
	}

	public char[] getNewPassword() {
		return password.getPassword();
	}

	public char[] getCfPassword() {
		return cfPassword.getPassword();
	}

	public static void main (String[] argv) {
		new SignUpGUI();
	}
}
