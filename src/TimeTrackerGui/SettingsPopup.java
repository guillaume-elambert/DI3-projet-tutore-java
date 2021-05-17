package TimeTrackerGui;
/**
 * @file SettingsPopup.java
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import TimeTrackerBackEnd.TimeTracker;

/**
 * @brief Classe permettant de créer des pop-up pour la saisie des paramètres d'une pointeuse.
 * @author Guillaume
 * @date 2021
 */
public class SettingsPopup {
	
	public SettingsPopup(TimeTracker timeTracker) {
		if(timeTracker.isConfigured()) {
			configInputPopup(timeTracker);
		} else {
			configChoicePopup(timeTracker);
		}
		
	}
	

	
	/**
	 * Méthode qui affiche une popup contenant un menu déroulant. 
	 * Permet de séléctionner une configuration déjà existante.
	 * 
	 * @param timeTracker La pointeuse dont il faudra attribuer les paramètres saisis.
	 */
	public void configChoicePopup(TimeTracker timeTracker) {

		JPanel contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		//On récupère les noms des fichiers et on les met dans la liste déroulante
		ArrayList<String> timeTrackerFileNames = TimeTracker.listSettingsFiles();
		
		
		//Entrée : il n'y a pas de fichiers de configuration
		//		=> On affiche la pop-up de saisie normale et on arrête le traitement
		if(timeTrackerFileNames.isEmpty()) {
			timeTracker.setDefaultConf();
			configInputPopup(timeTracker);
			return;
		}
		
		
		JComboBox<String> comboBox = new JComboBox<String>(
				timeTrackerFileNames.toArray(new String[timeTrackerFileNames.size()])
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
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.getRootFrame().dispose();
				addBtn.setEnabled(false);
				timeTracker.setDefaultConf();
				configInputPopup(timeTracker);
			}
		});
		
		
		//Le contenu à afficher
		Object[] message = { 
				"Choisir ou ajouter une configuration",
				Box.createVerticalStrut(10),
				contentPane,
				Box.createVerticalStrut(10)
		};
		
		
		//On affiche la popup pour choisir la configuration
		int dialogResult = JOptionPane.showConfirmDialog(null, message, "Settings",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(TimeTrackerGUI.class.getResource("/assets/images/settings-50px.png")));


		//Entrée : l'utilisateur à cliquer sur le boutton d'ajout d'une configuration
		//		=> On arrête le traitement (on ne charge pas la config depuis la pointeuse séléctionnée)
		if(!addBtn.isEnabled()) {
			return;
		}
		
		//On définit les options de la pointeuse à partir de la configuration de la pointeuse séléctionnée
		//Si rien n'a été séléctionné, utilisation des valeurs par défaut
		if(dialogResult == JOptionPane.OK_OPTION) {
			timeTracker.copySettings(new TimeTracker(comboBox.getSelectedItem().toString()));
		} else {
			timeTracker.setDefaultConf();
		}
		
	}

	
	
	/**
	 * Méthode qui affiche la boîte de dialogue pour la saisie des paramètres de la pointeuse
	 * 
	 * @param timeTracker La pointeuse
	 */
	public void configInputPopup(TimeTracker timeTracker) {

		String timeTrackerName = null;
		String tcpHost = null;
		int tcpPort = timeTracker.getTcpClient().getPort();
		
		// Les champs de saisie
		JTextField timeTrackerNameInput = new JTextField(timeTracker.getTimeTrackerName());
		JTextField tcpHostInput = new JTextField(timeTracker.getTcpClient().getHost());
		JSpinner tcpPortSpinner = new JSpinner();
		tcpPortSpinner.setModel(new SpinnerNumberModel(timeTracker.getTcpClient().getPort(), 1, null, 1));

		// Message de l'éventuelle erreur
		JLabel errorLabel = new JLabel("Saisie invalide !");
		errorLabel.setForeground(new Color(255, 0, 51));

		// Liste des objets à afficher par défaut
		Object[] defaultMessage = { 
				"Nom de la pointeuse :", timeTrackerNameInput, 
				Box.createVerticalStrut(10),
				"TCP Host :", tcpHostInput,
				Box.createVerticalStrut(10),
				"TCP Port :", tcpPortSpinner,
				Box.createVerticalStrut(10),
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

			
			timeTrackerName = timeTrackerNameInput.getText();
			tcpHost = tcpHostInput.getText();
			tcpPort = ((SpinnerNumberModel) tcpPortSpinner.getModel()).getNumber().intValue();
			
			// Il y a une erreur quand
			// => L'option OK est choisie
			// => ET que l'un des champs n'est pas saisi
			error = dialogResult == JOptionPane.OK_OPTION && (timeTrackerName.isEmpty()
					|| tcpHost.isEmpty() || tcpPort <= 0);

			
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
			if(!timeTracker.setSettings(timeTrackerName, tcpHost, tcpPort)) {
				configInputPopup(timeTracker);
				return;
			}
			
		} else {
			//On définit les paramètres de la pointeuse comme étant ceux par défaut
			timeTracker.setDefaultConf();
		}

	}

}
