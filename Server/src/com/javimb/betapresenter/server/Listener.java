package com.javimb.betapresenter.server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Listener {
	private int port;
	private ServerSocket serverSocket;
	private Socket socket;
	private DataInputStream dataInputStream;
	private Robot robot;
	private String info;
	private SimpleDateFormat dateFormat;
	
	public Listener(int port) {
		this.port = port;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		socket = null;
		info = ServerFrame.outputText.getText();
		dateFormat = new SimpleDateFormat("[dd/MM/yyyy - H:mm:ss]\n");
	}
	
	public void listen(){
		byte[] recivedData = new byte[1];
		try {
			DatagramSocket serverSocket = new DatagramSocket(port);
			info += dateFormat.format(new Date());
			info += "Listening at: " + port + "\n\n";
			ServerFrame.outputText.setText(info);
			while(true){
				DatagramPacket receivedPacket = new DatagramPacket(recivedData, recivedData.length);
				serverSocket.receive(receivedPacket);
				int code = Integer.parseInt(new String( receivedPacket.getData()));
				if(code == 0){
					serverSocket.close();
					info += dateFormat.format(new Date());
					info += "Stopped" + "\n\n";
					ServerFrame.outputText.setText(info);
					break;
				}
				info += dateFormat.format(new Date());
				info += "IP: " + receivedPacket.getAddress() + "\n";
				info += "Code: " + code + "\n\n";
				ServerFrame.outputText.setText(info);
				press(code);
			}
		} catch (SocketException e) {
			info += "Error!";
			ServerFrame.outputText.setText(info);
		} catch (IOException e) {
			info += "Error!";
			ServerFrame.outputText.setText(info);
		}
	}

	/*public void listen() {	
		try {
			serverSocket = new ServerSocket(port);
			info += dateFormat.format(new Date());
			info += "Listening at: " + port + "\n\n";
			ServerFrame.outputText.setText(info);
		} catch (IOException e) {
			info += "Error!";
			ServerFrame.outputText.setText(info);
			return;
		}
		
		int code;
		while(true){
			try {
				socket = serverSocket.accept();
				dataInputStream = new DataInputStream(socket.getInputStream());
				code = dataInputStream.readInt();
				if(code == 0){
					socket.close();
					dataInputStream.close();
					serverSocket.close();
					info += dateFormat.format(new Date());
					info += "Stopped" + "\n\n";
					ServerFrame.outputText.setText(info);
					break;
				}
				info += dateFormat.format(new Date());
				info += "IP: " + socket.getInetAddress().getHostName() + "\n";
				ServerFrame.outputText.setText(info);
				info += "Code: " + code + "\n\n";
				ServerFrame.outputText.setText(info);
				press(code);
			} catch (IOException e) {
				info += "Error!";
				ServerFrame.outputText.setText(info);
			}
			finally {
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						info += "Error!";
						ServerFrame.outputText.setText(info);
					}
				}
				
				if(dataInputStream != null){
					try {
						dataInputStream.close();
					} catch (IOException e) {
						info += "Error!";
						ServerFrame.outputText.setText(info);
					}
				}
			}
		}
	}*/

	private void press(int code){
		switch (code) {
		case 1:
			robot.keyPress(KeyEvent.VK_LEFT);
			robot.keyRelease(KeyEvent.VK_LEFT);
			break;
		case 2:
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
			break;
		}
	}
}
