package PointeuseBackEnd;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

/**
 * @file PointeuseSettings.java
 * @brief Contient la classe permettant de gérer les paramètres d'une pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */

/**
 * @brief Classe permettant de gérer les paramètres d'une pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class Pointeuse {

	static String settingsFolder = ".emu-settings/";	/**< Le chemin vers le fichier où sont stockées les fichiers de configuration des pointeuses. */
	static String settingsPrefix = "emu-";			/**< Le préfixe des fichiers de configuration des pointeuses. */
	static String settingsExtension = ".json";		/**< L'extension des fichiers de configuration des pointeuses. */
	
	static String defaultName = "Default Settings";	/**< Le nom par défaut des pointeuses. */
	static String defaultHost = "localhost";		/**< Le nom par défaut du serveur TCP distant des pointeuses. */
	static String defaultPort = "400";				/**< Le port par défaut  du serveur TCP distant des pointeuses. */
	
	private String nomPointeuse;	/**< Le nom de la pointeuse. */
	private String tcpHostname;		/**< Le nom du serveur TCP distant de la pointeuse. */
	private String tcpPort;			/**< Le port du serveur TCP distant de la pointeuse. */

	/**
	 * Constructeur par défaut.
	 */
	public Pointeuse() {
		this.nomPointeuse = null;
		this.tcpHostname = null;//"localhost";
		this.tcpPort = null;//"400";
	}

	/**
	 * Constructeur de confort.
	 * 
	 * @param nomPointeuse Le nom de la pointeuse
	 * @param tcpHostname  L'adresse du serveur TCP.
	 * @param tcpPort      Le port du serveur TCP.
	 */
	public Pointeuse(String nomPointeuse, String tcpHostname, String tcpPort) {
		this.nomPointeuse = nomPointeuse;
		this.tcpHostname = tcpHostname;
		this.tcpPort = tcpPort;
	}
	
	
	/**
	 * Constructeur de confort.
	 * Créé un objet Pointeuse à partir d'un fichier de configuration JSON.
	 * 
	 * @param pathToSettingsFile Le chemin vers le fichier de configuration.
	 */
	public Pointeuse(String nomPointeuse) {
		
		try {

			// On passe le nom de la pointeuse en base64 pour éviter les caractères spéciaux
			nomPointeuse = Base64.getEncoder().encodeToString(nomPointeuse.getBytes(StandardCharsets.UTF_8.toString()));
			
			@SuppressWarnings("resource")
			FileReader file = new FileReader(settingsFolder + settingsPrefix + nomPointeuse + settingsExtension);
			
			String res = "";
			int tmp;
			while( (tmp = file.read()) != -1 ) {
				res += (char) tmp;
			}
			
			// On transforme le JSON en objet
			Gson gsonObj = new Gson();
			Pointeuse sauvegardePointeuse = gsonObj.fromJson(res, Pointeuse.class);
			
			this.nomPointeuse = sauvegardePointeuse.nomPointeuse;
			this.tcpHostname = sauvegardePointeuse.tcpHostname;
			this.tcpPort = sauvegardePointeuse.tcpPort;
			
			System.out.println("Result : \n" + gsonObj.toJson(this) + "\n\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Accesseur en lecture de nom de la pointeuse.
	 * 
	 * @return Le nom de la pointeuse.
	 */
	public String getNomPointeuse() {
		return nomPointeuse;
	}

	/**
	 * Accesseur en écriture de nom de la pointeuse.
	 * 
	 * @param nomPointeuse Le nouveau nom de la pointeuse
	 */
	public void setNomPointeuse(String nomPointeuse) {
		this.nomPointeuse = nomPointeuse;
	}

	/**
	 * Accesseur en lecture du nom du serveur TCP distant de la pointeuse.
	 * 
	 * @return Le nom du serveur TCP distant de la pointeuse.
	 */
	public String getTcpHostname() {
		return tcpHostname;
	}

	/**
	 * Accesseur en écriture du nom du serveur TCP distant de la pointeuse.
	 * 
	 * @param tcpHostname Le nom du serveur TCP distant de la pointeuse.
	 */
	public void setTcpHostname(String tcpHostname) {
		this.tcpHostname = tcpHostname;
	}

	/**
	 * Accesseur en lecture du port du serveur TCP distant de la pointeuse.
	 * 
	 * @return Le port TCP du serveur distant de la pointeuse
	 */
	public String getTcpPort() {
		return tcpPort;
	}

	/**
	 * Accesseur en écriture du port du serveur TCP distant de la pointeuse.
	 * 
	 * @param tcpPort Le port TCP du serveur distant de la pointeuse.
	 */
	public void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}
	
	
	/**
	 * Méthode qui définit les paramètres de la pointeuse comme étant ceux par défaut.
	 */
	public void setDefaultConf() {
		nomPointeuse = defaultName;
		tcpHostname = defaultHost;
		tcpPort = defaultPort;
	}
	

	/**
	 * Fonction qui copie le contenu de la pointeuse passée en paramètre dans l'objet appelant.
	 * 
	 * @param pointeuse La pointeuse dont il faut copier les paramètres.
	 */
	public void setSettings(Pointeuse pointeuse) {

		this.nomPointeuse = pointeuse.nomPointeuse;
		this.tcpHostname = pointeuse.tcpHostname;
		this.tcpPort = pointeuse.tcpPort;
		
	}
	

	/**
	 * Méthode qui définie les paramètres d'une pointeuse.
	 * Elle renvoie true si elle a réussi a créé le fichier de configuration correspondant, false sinon.
	 * 
	 * @param nomPointeuse Le nom a attribué à la pointeuse.
	 * @param tcpHostname Le nom du serveur TCP distant à attribuer à la pointeuse.
	 * @param tcpPort Le port du serveur TCP distant à attribuer à la pointeuse.
	 * @return
	 */
	public boolean setSettings(String nomPointeuse, String tcpHostname, String tcpPort) {

		this.nomPointeuse = nomPointeuse;
		this.tcpHostname = tcpHostname;
		this.tcpPort = tcpPort;

		//On essaie de créer un fichier de configuration
		try {

			// On passe le nom de la pointeuse en base64 pour éviter les caractères spéciaux
			nomPointeuse = Base64.getEncoder().encodeToString(nomPointeuse.getBytes(StandardCharsets.UTF_8.toString()));

			// On transforme l'objet en objet JSON
			Gson gsonObj = new Gson();
			String json = gsonObj.toJson(this);
			System.out.println(json);

			try {
				
				File directory = new File(settingsFolder);
			    
				//Entrée : le dossier contenant les configurations n'existe pas
				//		=> On le créé
				if (! directory.exists()){
			        directory.mkdir();
			    }
				
				// On écrit la configuration dans un fichier
				FileWriter myWriter = new FileWriter(settingsFolder + settingsPrefix + nomPointeuse + settingsExtension);
				myWriter.write(json);
				myWriter.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	
	/**
	 * Méthode qui retourne true si la pointeuse est configurée, false sinon.
	 * 
	 * @return L'état de configuration de la pointeuse.
	 */
	public boolean isConfigured() {
		return (    nomPointeuse != null 
				 && tcpHostname != null
				 && tcpPort != null
				);
	}
	
	
	/**
	 * Méthode qui renvoie la liste des noms des machines précédemment configurées.
	 * Une liste vide si aucune n'a été configurée.
	 * 
	 * @return La liste des noms des machines précédemment configurées.
	 */
	public static ArrayList<String> listSettingsFiles() {
		File root = new File(settingsFolder);
		ArrayList<String> emuNames = new ArrayList<String>();

		if (root.isDirectory()) {
	
			final Pattern p = Pattern.compile("emu-(.*)\\.json"); // careful: could also throw an exception!
			Matcher matcher;
	
			File[] listFiles = root.listFiles();
	
			// On parcourt tous les fichiers du dossier ".settings"
			for (File file : listFiles) {
				matcher = p.matcher(file.getName());
	
				// Si le nom du fichier parcouru correspond au format attendu
				// => On ajoute son nom à la liste
				if (matcher.matches()) {
					emuNames.add(new String(Base64.getDecoder().decode(matcher.group(1))));
				}
			}
		}

		return emuNames;
	}
	
	
	/**
	 * Fonction qui retourne l'objet sous forme de chaîne
	 * 
	 * @return L'objet sous forme de chaîne
	 */
	public String toString() {
		// On transforme l'objet en objet JSON
		Gson gsonObj = new Gson();
		return gsonObj.toJson(this); 
	}

}
