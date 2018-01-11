package server;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton connectBtn, disconnectBtn;
	private JLabel onlineUsersLabel;
	private JTextArea chatTextArea;
	private JTextArea onlineUsersArea;
	private JTextArea activityTextArea;
	private JScrollPane chatTextScroll, onlineUsersScroll, activityTextScroll;
	
	public ServerGUI(){
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//add event listener when close windows
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				int confirm = JOptionPane.showOptionDialog(getContentPane(),
                        "Are You Sure to Close The Server?",
                        "Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == JOptionPane.OK_OPTION) {
					try{
						Server.disconnectServer();
					} finally{
						System.exit(0);
					}
                }
			}
		});
		
		setTitle("Chat Server");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,10,5,10);
		
		//column 1
		connectBtn = new JButton("Create The Chat Room");
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		this.add(connectBtn, gbc);
		
		disconnectBtn = new JButton("Close The Chat Room");
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		this.add(disconnectBtn, gbc);
		
		onlineUsersLabel = new JLabel("Online Users", SwingConstants.CENTER);
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		this.add(onlineUsersLabel, gbc);
		
		//column 2
		//chat log
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
		chatTextScroll.setBorder(BorderFactory.createTitledBorder("Chat Log"));
		this.add(chatTextScroll, gbc);
		
		//column 3
		//activity log
		activityTextArea = new JTextArea();
		activityTextArea.setWrapStyleWord(true);
		activityTextArea.setLineWrap(true);
		DefaultCaret activityCaret = (DefaultCaret)activityTextArea.getCaret();
		activityCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		activityTextScroll = new JScrollPane(activityTextArea);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.2;
		activityTextScroll.setBorder(BorderFactory.createTitledBorder("Activity Log"));
		this.add(activityTextScroll, gbc);
		
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
		
		this.setVisible(true);
		
		// set default of view
		disconnectedServerView();
		
	}
	
	//listener of create server button
	public void connectBtnListener(ActionListener action){
		connectBtn.addActionListener(action);
	}
	//listener of disconnect server button	
	public void disconnectBtnListener(ActionListener action){
		disconnectBtn.addActionListener(action);
	}
	
	public void showDialog(String message){
		JOptionPane.showMessageDialog(this, message);
	}
	
	// view of connected user
	public void createdServerView(){
		chatTextArea.setEditable(false);
		onlineUsersArea.setEditable(false);
		activityTextArea.setEditable(true);
		connectBtn.setEnabled(false);
		disconnectBtn.setEnabled(true);
		
	}
	
	// view of disconnected user
	public void disconnectedServerView(){
		chatTextArea.setEditable(false);
		onlineUsersArea.setEditable(false);
		activityTextArea.setEditable(false);
		connectBtn.setEnabled(true);
		disconnectBtn.setEnabled(false);
	}
	
	//update online user from list
	public void updateOnlineUsers(ArrayList<String> onlineUsers){
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
	
	public void setActivityTextArea(String activity) {
		activityTextArea.append(activity);;
	}
}
