package TimeTrackerBackEnd;

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
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * @file TimeTracker.java
 * @brief Contient la classe permettant de gérer les paramètres d'une pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */

/**
 * @brief Classe permettant de gérer les paramètres d'une pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class TimeTracker {

	public static final String settingsFolder = ".emu-settings/";	/**< Le chemin vers le fichier où sont stockées les fichiers de configuration des pointeuses. */
	public static final String settingsPrefix = "emu-";			/**< Le préfixe des fichiers de configuration des pointeuses. */
	public static final String settingsExtension = ".json";		/**< L'extension des fichiers de configuration des pointeuses. */
	
	public static final String defaultName = "Default Settings";	/**< Le nom par défaut des pointeuses. */
	
	@Expose private String timeTrackerName;	/**< Le nom de la pointeuse. */
	@Expose private TCPClient tcpClient;	/**< Client TCP permettant lacommunication avec le serveur TCP distant de la pointeuse. */

	
	/**
	 * Constructeur par défaut.
	 */
	public TimeTracker() {
		this.timeTrackerName = null;
		this.tcpClient = new TCPClient();
	}

	
	/**
	 * Constructeur de confort.
	 * 
	 * @param timeTrackerName Le nom de la pointeuse.
	 * @param tcpHost L'adresse du serveur TCP.
	 * @param tcpPort Le port du serveur TCP.
	 */
	public TimeTracker(String timeTrackerName, String tcpHost, int tcpPort) {
		this.timeTrackerName = timeTrackerName;
		this.tcpClient = new TCPClient(tcpHost, tcpPort);
	}
	
	
	/**
	 * Constructeur de confort.
	 * Créé un objet TimeTracker à partir d'un fichier de configuration JSON.
	 * 
	 * @param timeTrackerName Le nom de la pointeuse dont il faut utiliser le fichier de configuration.
	 */
	public TimeTracker(String timeTrackerName) {
		
		try {

			// On passe le nom de la pointeuse en base64 pour éviter les caractères spéciaux
			timeTrackerName = Base64.getEncoder().encodeToString(timeTrackerName.getBytes(StandardCharsets.UTF_8.toString()));
			
			@SuppressWarnings("resource")
			FileReader file = new FileReader(settingsFolder + settingsPrefix + timeTrackerName + settingsExtension);
			
			String res = "";
			int tmp;
			while( (tmp = file.read()) != -1 ) {
				res += (char) tmp;
			}
			
			// On transforme le JSON en objet
			Gson gsonObj = new Gson();
			TimeTracker timeTrackerBackup = gsonObj.fromJson(res, TimeTracker.class);
			
			this.timeTrackerName = timeTrackerBackup.timeTrackerName;
			this.tcpClient = new TCPClient(timeTrackerBackup.tcpClient.getHost(), timeTrackerBackup.tcpClient.getPort());
			
			
		} catch (IOException e) {
			
			this.timeTrackerName = defaultName;
			this.tcpClient = new TCPClient();
			
			e.printStackTrace();
		}
	}
	

	/**
	 * Fonction qui copie les paramètres de la pointeuse passée en paramètre dans l'objet appelant.
	 * 
	 * @param timeTrackerName La pointeuse dont il faut copier les paramètres.
	 */
	public void copySettings(TimeTracker timeTrackerName) {

		this.timeTrackerName = timeTrackerName.timeTrackerName;
		this.tcpClient = new TCPClient(timeTrackerName.tcpClient.getHost(), timeTrackerName.tcpClient.getPort());
		
	}
	

	/**
	 * Méthode qui définie les paramètres d'une pointeuse.
	 * Elle renvoie true si elle a réussi a créé le fichier de configuration correspondant, false sinon.
	 * 
	 * @param timeTrackerName Le nom a attribué à la pointeuse.
	 * @param tcpHost Le nom du serveur TCP distant à attribuer à la pointeuse.
	 * @param tcpPort Le port du serveur TCP distant à attribuer à la pointeuse.
	 * @return L'état de création du fichier de configuration
	 */
	public boolean setSettings(String timeTrackerName, String tcpHost, int tcpPort) {
		this.timeTrackerName = timeTrackerName;
		this.tcpClient.setSettings(tcpHost, tcpPort);

		//On essaie de créer un fichier de configuration
		try {

			// On passe le nom de la pointeuse en base64 pour éviter les caractères spéciaux
			timeTrackerName = Base64.getEncoder().encodeToString(timeTrackerName.getBytes(StandardCharsets.UTF_8.toString()));

			// On transforme l'objet en objet JSON
			String json = toJson();

			try {
				
				File directory = new File(settingsFolder);
			    
				//Entrée : le dossier contenant les configurations n'existe pas
				//		=> On le créé
				if (! directory.exists()){
			        directory.mkdir();
			    }
				
				// On écrit la configuration dans un fichier
				FileWriter myWriter = new FileWriter(settingsFolder + settingsPrefix + timeTrackerName + settingsExtension);
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
		
		return (    timeTrackerName != null 
				 && this.tcpClient.getHost() != null
				 && this.tcpClient.getPort() > 0
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
	 * Méthode qui définit les paramètres de la pointeuse comme étant ceux par défaut.
	 */
	public void setDefaultConf() {
		timeTrackerName = defaultName;
		this.tcpClient.setSettings(TCPClient.defaultHost, TCPClient.defaultPort);// = new TCPClient();
	}
	

	/**
	 * Accesseur en lecture de nom de la pointeuse.
	 * 
	 * @return Le nom de la pointeuse.
	 */
	public String getTimeTrackerName() {
		return timeTrackerName;
	}

	
	/**
	 * Accesseur en écriture de nom de la pointeuse.
	 * 
	 * @param timeTrackerName Le nouveau nom de la pointeuse
	 */
	public void setTimeTrackerName(String timeTrackerName) {
		this.timeTrackerName = timeTrackerName;
	}

	
	/**
	 * Accesseur en lecture du client TCP de la pointeuse.
	 * 
	 * @return Le client TCP de la pointeuse
	 */
	public TCPClient getTcpClient() {
		return tcpClient;
	}
	

	/**
	 * Accesseur en écriture du client TCP de la pointeuse.
	 * 
	 * @param tcpClient Le nouveau client TCP de la pointeuse
	 */
	public void setTcpClient(TCPClient tcpClient) {
		this.tcpClient = tcpClient;
	}
	
	
	/**
	 * Fonction qui retourne l'objet sous forme de chaîne JSON.
	 * 
	 * @return L'objet sous forme de chaîne
	 */
	public String toJson() {
		// On transforme l'objet en objet JSON
		Gson gsonObj = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gsonObj.toJson(this, TimeTracker.class); 
	}

}
