package TimeTrackerBackEnd;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * @file TCPClient.java
 * @brief Contient la classe permettant de gérer le client TCP d'une pointeuse.
 * @author Angèle ROUSSEL
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class TCPClient extends Observable {
	
	public static final String defaultHost = "localhost";			/**< Le nom par défaut du serveur TCP distant des pointeuses. */
	public static final int defaultPort = 400;						/**< Le port par défaut  du serveur TCP distant des pointeuses. */
	
	@Expose private String host;
	@Expose private int port;
	private transient Socket socket;
	
	
	/**
	 * Constructeur par défaut
	 */
	public TCPClient() {
		host = defaultHost;
		port = defaultPort;
		socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(host, port));
		}
		catch (UnknownHostException e) {
			System.err.print("Connexion au serveur "+host+" impossible.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructeur de confort
	 * 
	 * @param host Le nom du serveur TCP distant.
	 * @param port Le port du serveur TCP distant.
	 */
	public TCPClient(String host, int port) {
				
		this.host = host;
		this.port = port;
		socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(host, port));
		}
		catch (UnknownHostException e) {
			System.err.print("Connexion au serveur "+host+" impossible.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Méthode qui envoie les données d'un pointage au serveur TCP distant.
	 * 
	 * @param day Le jour à envoyer.
	 * @param time L'heure à envoyer.
	 * @param idEmployee L'identifiant de l'employe à envoyer.
	 */
	public void sendTimeTrackingData(String day, String time, int idEmployee) {
		OutputStream output;
		System.out.println("Day : "+day+"\nTime : "+time+"\nId Salarie : "+idEmployee);
		try {
			output = socket.getOutputStream();
			byte[] data = day.getBytes();
			output.write(data);
			data = time.getBytes();
			output.write(data);
			data = BigInteger.valueOf(idEmployee).toByteArray();
			output.write(data);
			
			socket.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Méthode qui retourne l'état de connexion du client TCP.
	 * @return L'état de connexion du client TCP.
	 */
	public boolean getConnexionStatus() {
		boolean status = false;
		
		
		if(this.socket != null && this.socket.isConnected()) {
			try {
				status = new InetSocketAddress(host, port).getAddress().isReachable(30);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return status;
	}
	
	
	/**
	 * Accesseur en lecture du nom du serveur TCP distant.
	 * 
	 * @return 
	 */
	public String getHost() {
		return host;
	}

	
	/**
	 * Accesseur en écriture du nom du serveur TCP distant.
	 * 
	 * @param Le nouveau nom du serveur TCP distant.
	 */
	public void setHost(String host) {
		this.host = host;
			socket = new Socket();
			
		try {
			socket.connect(new InetSocketAddress(host, port));
		}
		catch (UnknownHostException e) {
			System.err.print("Connexion au serveur "+host+" impossible.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setChanged();
		notifyObservers();
	}

	
	/**
	 *  Accesseur en lecture du port du serveur TCP distant.
	 *  
	 * @return Le port du serveur TCP distant.
	 */
	public int getPort() {
		return port;
	}
	

	/**
	 * Accesseur en écriture du port du serveur TCP distant.
	 * 
	 * @param Le nouveau port du serveur TCP distant.
	 */
	public void setPort(int port) {
		this.port = port;
		socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(host, port));
		}
		catch (UnknownHostException e) {
			System.err.print("Connexion au serveur "+host+" impossible.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setChanged();
		notifyObservers();
	}


	
	/**
	 * Accesseur en lecture du client TCP.
	 * 
	 * @return Le client TCP.
	 */
	public Socket getSocket() {
		return socket;
	}

	
	/**
	 * Accesseur en écriture du client TCP.
	 * 
	 * @param Le nouveau client TCP.
	 */
	public void setSocket(Socket socket) {
		
		this.socket = socket;
		this.host = socket.getInetAddress().getHostName();
		this.port = socket.getPort();
		
		setChanged();
		notifyObservers();
	}
	
	
	/**
	 * Accesseur en écriture du nom et du port du serveur TCP distant
	 * 
	 * @param host Le nouveau nom du serveur TCP distant.
	 * @param port Le nouveau port du serveur TCP distant.
	 */
	public void setSettings(String host, int port) {
		
		this.host = host;
		this.port = port;
		socket = new Socket();
		
		try {
			socket.connect(new InetSocketAddress(host, port));
		}
		catch (UnknownHostException e) {
			System.err.print("Connexion au serveur "+host+" impossible.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		setChanged();
		notifyObservers();
	}

	
	/**
	 * Fonction qui retourne l'objet sous forme de chaîne JSON.
	 * 
	 * @return L'objet sous forme de chaîne
	 */
	public String toJson() {
		// On transforme l'objet en objet JSON
		Gson gsonObj = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gsonObj.toJson(this, TCPClient.class); 
	}
}