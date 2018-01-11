package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

public class ChatGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton connectBtn, disconnectBtn, sendBtn;
	private JLabel usernameLabel, onlineUsersLabel;
	private JTextArea chatTextArea;
	private JTextArea onlineUsersArea;
	private JTextArea inputTextArea;
	private JScrollPane chatTextScroll, onlineUsersScroll, inputTextScroll;
	private ActionMap actions;
	
	public ChatGUI(){
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("User Chatter");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,10,5,10);
		
		//row 1
		usernameLabel = new JLabel();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.fill = GridBagConstraints.CENTER;
		this.add(usernameLabel, gbc);
		
		connectBtn = new JButton("Connect");
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		this.add(connectBtn, gbc);
		
		disconnectBtn = new JButton("Disconnect");
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		this.add(disconnectBtn, gbc);
		
		onlineUsersLabel = new JLabel("Online Users", SwingConstants.CENTER);
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		this.add(onlineUsersLabel, gbc);
		
		//row 2
		//chat window
		chatTextArea = new JTextArea();
		chatTextArea.setWrapStyleWord(true);
		chatTextArea.setLineWrap(true);
		DefaultCaret chatCaret = (DefaultCaret)chatTextArea.getCaret();
		chatCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatTextScroll = new JScrollPane(chatTextArea);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.6;
		this.add(chatTextScroll, gbc);
		
		//row 3
		//user's message field
		inputTextArea = new JTextArea();
		inputTextArea.setWrapStyleWord(true);
		inputTextArea.setLineWrap(true);
		DefaultCaret inputCaret = (DefaultCaret)inputTextArea.getCaret();
		inputCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//set enter to send text and shift enter to break line
		InputMap input = inputTextArea.getInputMap();
	    KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
	    KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
	    input.put(shiftEnter, "insert-break");  // input.get(enter)) = "insert-break"
	    input.put(enter, "text-submit");
	    actions = inputTextArea.getActionMap();
	    
		inputTextScroll = new JScrollPane(inputTextArea);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.2;
		this.add(inputTextScroll, gbc);
		
		//send button
		sendBtn = new JButton("Send");
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.weighty = 0.2;
		this.add(sendBtn, gbc);
		
		//online users area
		onlineUsersArea = new JTextArea();
		onlineUsersArea.setWrapStyleWord(true);
		onlineUsersArea.setLineWrap(true);
		DefaultCaret onlineUsersCaret = (DefaultCaret)onlineUsersArea.getCaret();
		onlineUsersCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		onlineUsersScroll = new JScrollPane(onlineUsersArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.8;
		this.add(onlineUsersScroll, gbc);
		
		// set default of view
		disconnectedUserView();
		
	}
	
	public void connectBtnListener(ActionListener action){
		connectBtn.addActionListener(action);
	}
	
	public void disconnectBtnListener(ActionListener action){
		disconnectBtn.addActionListener(action);
	}
	
	public void sendBtnListener(ActionListener action){
		sendBtn.addActionListener(action);
	}
	
	public void showDialog(String message){
		JOptionPane.showMessageDialog(this, message);
	}
	
	// view of connected user
	public void connectedUserView(String usernameField){
		chatTextArea.setEditable(false);
		onlineUsersArea.setEditable(false);
		inputTextArea.setEditable(true);
		connectBtn.setEnabled(false);
		sendBtn.setEnabled(true);
		disconnectBtn.setEnabled(true);
		
	}
	
	// view of disconnected user
	public void disconnectedUserView(){
		chatTextArea.setEditable(false);
		onlineUsersArea.setEditable(false);
		inputTextArea.setEditable(false);
		sendBtn.setEnabled(false);
		connectBtn.setEnabled(true);
		disconnectBtn.setEnabled(false);
	}
	
	// enter pressed event in input text area
	public void addPressedEnterEvent(AbstractAction aa){
		actions.put("text-submit", aa);
	}
	
	public void updateOnlineUsers(String[] onlineUsers){
		onlineUsersArea.setText(null);
		for (String user: onlineUsers){
			onlineUsersArea.append(user + "\n");
		}
	}
	
	public void removeOnlineUser(){
		onlineUsersArea.setText(null);
	}

	public void appendChatTextArea(String message) {
		chatTextArea.append(message);
	}

	public String getInputTextArea() {
		return inputTextArea.getText();
	}
	
	public void setInputTextArea() {
		inputTextArea.setText(null);;
	}
	
	public void setUsername(String username){
		usernameLabel.setText("Wellcome " + username);
	}
}
