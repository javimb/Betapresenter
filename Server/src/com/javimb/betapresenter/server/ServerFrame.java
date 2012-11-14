package com.javimb.betapresenter.server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import java.awt.Toolkit;

public class ServerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField portTextField;
	private Thread listenerThread;
	private JButton startButton;
	private JButton stopButton;
	private Socket closeListenerSocket;
	private DataOutputStream closeDataOutputStream;
	private JScrollPane scrollPane;
	public static JTextArea outputText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ServerFrame.class.getResource("/images/logo.png")));
		setFont(new Font("Arial", Font.PLAIN, 12));
		setTitle("\u00DFetapresenter");
		setBounds(100, 100, 228, 286);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		portTextField = new JTextField();
		portTextField.setHorizontalAlignment(SwingConstants.CENTER);
		portTextField.setText("8888");
		portTextField.setBounds(107, 12, 55, 19);
		contentPane.add(portTextField);
		portTextField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Arial", Font.BOLD, 12));
		lblPort.setBounds(63, 15, 70, 15);
		contentPane.add(lblPort);
		
		startButton = new JButton("Start");
		startButton.setFont(new Font("Arial", Font.BOLD, 12));
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listenerThread = new Thread(new Runnable() {
					public void run()
					{
						try {
							Listener listener = new Listener(Integer.parseInt(portTextField.getText()));
							listener.listen();
						} catch (Exception e) {
							outputText.setText(outputText.getText() + "Invalid port! \n\n");
							JOptionPane.showMessageDialog(null, "Stop the server and enter a valid port", "Invalid port", 2);
						}
					}
				});
				listenerThread.start();
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
			}
		});
		startButton.setBounds(12, 43, 93, 25);
		contentPane.add(startButton);
		
		String localIp = "IP: ";
		try {
			if(System.getProperty("os.name").contains("Linux")){
				Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
				Enumeration<InetAddress> inetAddresses = Collections.list(nets).get(0).getInetAddresses();
				localIp += Collections.list(inetAddresses).get(1).getHostName();
			}
			else{
				localIp += InetAddress.getLocalHost().getHostAddress();
			}
		} catch (Exception e) {
			// TODO
		}
		JLabel hostLabel = new JLabel(localIp);
		hostLabel.setBounds(12, 83, 198, 19);
		contentPane.add(hostLabel);
		
		stopButton = new JButton("Stop");
		stopButton.setFont(new Font("Arial", Font.BOLD, 12));
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					DatagramSocket closeListenerSocket = new DatagramSocket();
					byte[] sendData = new byte[1];
					sendData = "0".getBytes();
					DatagramPacket closeListenerPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), Integer.parseInt(portTextField.getText()));
					closeListenerSocket.send(closeListenerPacket);
					closeListenerSocket.close();
					
					/*closeListenerSocket = new Socket("localhost", Integer.parseInt(portTextField.getText()));
					closeDataOutputStream = new DataOutputStream(closeListenerSocket.getOutputStream());
					closeDataOutputStream.writeInt(0);
					closeListenerSocket.close();
					closeDataOutputStream.close();*/
				} catch (Exception e) {
					//TODO
				}
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		});
		stopButton.setBounds(117, 43, 93, 25);
		stopButton.setEnabled(false);
		contentPane.add(stopButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(12, 114, 198, 122);
		contentPane.add(scrollPane);
		
		outputText = new JTextArea();
		outputText.setEditable(false);
		scrollPane.setViewportView(outputText);
	}
}
