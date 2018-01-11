package client;

public class ClientMain {
	public static void main(String[] args) {
		LoginGUI loginView = new LoginGUI();
		SignUpGUI signUpView = new SignUpGUI();
		ChatGUI clientView = new ChatGUI();
		new Login(loginView, signUpView, clientView);
	}
}