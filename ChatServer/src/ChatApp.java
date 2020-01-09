import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatApp extends Application{
	
	public DataOutputStream dos;
	public DataInputStream dis;
	public Socket s;
	public Client client = new Client("10.0.0.49", 5004);
	
	private TextArea messages = new TextArea();

	
	
	public Parent createScene() {
		messages.setPrefHeight(500);
		messages.setEditable(false);
		
		TextField input = new TextField();
		input.setPromptText("Send message here");
		input.setOnAction(e ->{
			//messages.appendText(input.getText() + "\n");
			client.send(input.getText() + "\n");
			input.clear();
		});
		
		Thread t1 = new Thread(()->{
			while(true) {
				String read = client.read();
				Platform.runLater(() ->{
				messages.setText(messages.getText() + read);
				});
			}
		});
		t1.setDaemon(true);
		t1.start();
		
		VBox root = new VBox(20, messages, input);
		return root;
	}
	

	@Override
	public void start(Stage stage){
		stage.setScene(new Scene(createScene()));
		stage.show();
	}
	
	
	
	
	public static void main(String[] args){
		launch(args);
	}

}



class Client implements Runnable{
	
	private String ip;
	private int port;
	private Socket s;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	
	public Client(String ip, int port){
		this.ip = ip;
		this.port = port;
		try {
			s = new Socket(ip, port);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void send(String message){
		try {
			dos.writeUTF(message);
		} catch (IOException e) {
			closeConnection();
		}
	}
	
	
	public String read(){
		try {
			return dis.readUTF();
		} catch (IOException e) {
			closeConnection();
		}
		return "";
	}


	@Override
	public void run() {
		while(true) {
			
		}
	}
	
	
	
}


