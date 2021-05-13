package PointeuseGui;
/**
 * @file PointeuseGUI.java
 * @brief Conteint la classe permettant de créer l'IHM d'un pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */


import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import PointeuseBackEnd.Pointeuse;

@SuppressWarnings("serial")
/**
 * @brief Classe permettant la création de l'IHM de la pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class PointeuseGUI extends JFrame {
	
	private Pointeuse pointeuse;
	private JPanel contentPane;

	
	/**
	 * Constructeur par défaut.
	 * Créé l'IHM de la pointeuse.
	 */
	public PointeuseGUI(Pointeuse pointeuse) {
		
		this.pointeuse = pointeuse;
		new SettingsPopup(this.pointeuse);

		System.out.println("Settings main: " + this.pointeuse.toString() +"\n\n");

		// Paramètre du conteneur
		setTitle("Pointeuse - Emulateur");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(450, 300));

		// Contenu
		contentPane = new HomePanel();
		setContentPane(contentPane);

		// On définit la barre d'outils
		MenuBar menuBar = new MenuBar(this.pointeuse);
		setJMenuBar(menuBar);

		pack();
		setLocationByPlatform(true);

	}
}