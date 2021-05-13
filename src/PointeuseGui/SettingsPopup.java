package PointeuseGui;
/**
 * @file pointeusePopup.java
 * @brief Contient la classe permettant de créer des pop-up pour la saisie des paramètres d'une pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import PointeuseBackEnd.Pointeuse;

/**
 * @brief Classe permettant de créer des pop-up pour la saisie des paramètres d'une pointeuse.
 * @author Guillaume
 * @date 2021
 */
public class SettingsPopup {
	
	public SettingsPopup(Pointeuse pointeuse) {
		if(pointeuse.isConfigured()) {
			//pointeuse.setDefaultConf();
			popupSaisieConfigPointeuse(pointeuse);
		} else {
			popupChoixConfigPointeuse(pointeuse);
		}
		
	}
	

	
	/**
	 * Méthode qui affiche une popup contenant un menu déroulant. 
	 * Permet de séléctionner une configuration déjà existante.
	 * 
	 * @param pointeuse La pointeuse dont il faudra attribuer les paramètres saisis.
	 */
	public void popupChoixConfigPointeuse(Pointeuse pointeuse) {
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		//On récupère les noms des fichiers et on les met dans la liste déroulante
		ArrayList<String> pointeuseFileNames = Pointeuse.listSettingsFiles();
		
		
		//Entrée : il n'y a pas de fichiers de configuration
		//		=> On affiche la pop-up de saisie normale et on arrête le traitement
		if(pointeuseFileNames.isEmpty()) {
			pointeuse.setDefaultConf();
			popupSaisieConfigPointeuse(pointeuse);
			return;
		}
		
		
		JComboBox<String> comboBox = new JComboBox<String>(
				pointeuseFileNames.toArray(new String[pointeuseFileNames.size()])
		);		
		contentPane.add(comboBox);
		
		
		//Espace entre les 2 éléments
		Component horizontalStrut = Box.createHorizontalStrut(10);
		contentPane.add(horizontalStrut);
		
		
		//Bouton d'ajout d'une configuration
		JButton addBtn = new JButton();
		addBtn.setIcon(new ImageIcon(SettingsPopup.class.getResource("/assets/images/add-20px.png")));
		contentPane.add(addBtn);
		
		
		//On ajoute une action lors du clic sur le bouton pour ajouter une configuration
		//	=> On ferme la popup
		//	=> On affiche la popup pour créér un nouvelle config
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.getRootFrame().dispose();
				addBtn.setEnabled(false);
				pointeuse.setDefaultConf();
				popupSaisieConfigPointeuse(pointeuse);
			}
		});
		
		
		//Le contenu à afficher
		Object[] message = { 
				"Choisir une configuration", contentPane
		};
		
		
		//On affiche la popup pour choisir la configuration
		int dialogResult = JOptionPane.showConfirmDialog(null, message, "Settings",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(PointeuseGUI.class.getResource("/assets/images/settings-50px.png")));


		//Entrée : l'utilisateur à cliquer sur le boutton d'ajout d'une configuration
		//		=> On arrête le traitement (on ne charge pas la config depuis la pointeuse séléctionnée)
		if(!addBtn.isEnabled()) {
			return;
		}
		
		//On définit les options de la pointeuse à partir de la configuration de la pointeuse séléctionnée
		//Si rien n'a été séléctionné, utilisation des valeurs par défaut
		if(dialogResult == JOptionPane.OK_OPTION) {
			pointeuse.setSettings(new Pointeuse(comboBox.getSelectedItem().toString()));
		} else {
			pointeuse.setDefaultConf();
		}
			
		System.out.println("L'utilisateur a séléctionner : " + comboBox.getSelectedItem());		
		System.out.println("La configuration de la pointeuse : " + pointeuse.toString()+"\n\n");
		
	}

	
	
	/**
	 * Méthode qui affiche la boîte de dialogue pour la saisie des paramètres de la pointeuse
	 * 
	 * @param pointeuse La pointeuse
	 */
	public void popupSaisieConfigPointeuse(Pointeuse pointeuse) {

		String saisieNom = null;
		String saisieHost = null;
		String saisiePort = null;

		// Les champs de saisie
		JTextField champNomPointeuse = new JTextField(pointeuse.getNomPointeuse());
		JTextField champHostTCP = new JTextField(pointeuse.getTcpHostname());
		JTextField champPortTCP = new JTextField(pointeuse.getTcpPort());

		// Message de l'éventuelle erreur
		JLabel errorLabel = new JLabel("Saisie invalide !");
		errorLabel.setForeground(new Color(255, 0, 51));

		// Liste des objets à afficher par défaut
		Object[] defaultMessage = { 
				"Nom de la pointeuse :", champNomPointeuse, 
				"TCP Host :", champHostTCP,
				"TCP Port :", champPortTCP
		};

		ArrayList<Object> message = new ArrayList<Object>(Arrays.asList(defaultMessage));

		int dialogResult;
		boolean error, isFirstError = true;

		// On affiche la boîte de dialogue tant que les saisie sont mauvaises
		do {

			// On affiche la boîte de dialogue
			dialogResult = JOptionPane.showConfirmDialog(null, message.toArray(), "Settings",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
					new ImageIcon(SettingsPopup.class.getResource("/assets/images/settings-50px.png")));

			
			saisieNom = champNomPointeuse.getText();
			saisieHost = champHostTCP.getText();
			saisiePort = champPortTCP.getText();
			
			// Il y a une erreur quand
			// => L'option OK est choisie
			// => ET que l'un des champs n'est pas saisi
			error = dialogResult == JOptionPane.OK_OPTION && (saisieNom.isEmpty()
					|| saisieHost.isEmpty() || saisiePort.isEmpty());

			
			//Entrée : il y a eu une erreur
			//		ET c'est la première fois (pas déjà de msg d'erreur)
			if (error && isFirstError) {
				message.add(0, errorLabel);
				isFirstError = false;
			}

		} while (error);

		
		//Entrée : l'utilisateur a validé le formulaire popup
		//	=> On définit les paramètre de la pointeuse
		if (dialogResult == JOptionPane.OK_OPTION) {

			//Si ça n'a pas marché (impossible de créé le fichier de configuration), on réessaie
			if(!pointeuse.setSettings(saisieNom, saisiePort, saisiePort)) {
				popupSaisieConfigPointeuse(pointeuse);
				return;
			}
			
		} else {
			//On définit les paramètres de la pointeuse comme étant ceux par défaut
			pointeuse.setDefaultConf();
		}

		System.out.println("Host : " + pointeuse.getTcpHostname() + "\tPort : " + pointeuse.getTcpPort());
	}

}
