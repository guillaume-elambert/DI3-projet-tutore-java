/**
 * @file TimeTrackerGUI.java
 * @brief Conteint la classe permettant de créer l'IHM d'un pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */

package TimeTrackerGui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import TimeTrackerBackEnd.TimeTracker;


@SuppressWarnings("serial")
/**
 * @brief Classe permettant la création de l'IHM de la pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class TimeTrackerGUI extends JFrame
{
	
	private TimeTracker timeTracker;	/**< La pointeuse dont on créé l'IHM. */
	private JPanel contentPane;			/**< Le conteneur de l'IHM. */
	private MenuBar menuBar;			/**< La barre de menu de l'IHM. */

	
	/**
	 * Constructeur par défaut.
	 * Créé l'IHM de la pointeuse.
	 * 
	 * @param timeTracker La pointeuse dont on créé l'IHM.
	 */
	public TimeTrackerGUI(TimeTracker timeTracker)
	{
		
		this.timeTracker = timeTracker;
		new SettingsPopup(this.timeTracker);

		// Paramètre du conteneur
		setTitle("TimeTracker - Emulateur");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(450, 300));

		// Contenu
		contentPane = new HomePanel(timeTracker);
		setContentPane(contentPane);

		// On définit la barre d'outils
		menuBar = new MenuBar(this.timeTracker);
		setJMenuBar(menuBar);

		pack();
		setLocationByPlatform(true);

	}
}