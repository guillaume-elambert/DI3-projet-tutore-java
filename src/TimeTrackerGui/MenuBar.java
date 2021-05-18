/**
 * @file MenuBar.java
 * @brief Contient la classe permettant la création d'une barre d'options dans l'IHM de la pointeuse. 
 * @author Guillaume ELAMBERT
 * @date 2021
 */

package TimeTrackerGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import TimeTrackerBackEnd.TimeTracker;


@SuppressWarnings("serial")
/**
 * @brief Classe permettant la création d'une barre d'options dans l'IHM de la pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class MenuBar extends JMenuBar
{

	private JMenu menu;					/**< Le menu déroulant d'options. */
	private JMenuItem settingsMenuItem;	/**< Le bouton d'options. */
	private JPanel iconPanel;			/**< Conteneur de l'image. */
	private JLabel connexionStatus;		/**< Label qui contiendra l'icône de l'état de connexion avec le serveur TCP distant. */
	
	static final String connectedStatusImg = "connection-30px.png";			/**< Image de l'état de connexion OK. */
	static final String notConnectedStatusImg = "no-connection-30px.png";	/**< Image de l'état de connexion KO. */

	/**
	 * Créé la barre de menu.
	 * 
	 * @param timeTracker La pointeuse dont on créé l'IHM.
	 */
	public MenuBar(TimeTracker timeTracker)
	{
		menu = new JMenu("Manage");
		settingsMenuItem = new JMenuItem("Settings",
				new ImageIcon(MenuBar.class.getResource("/assets/images/settings-20px.png")));
		settingsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, InputEvent.CTRL_MASK));

		menu.add(settingsMenuItem);
		add(menu);
		
		
		//Panel qui contient l'icône de l'état de connexion
		iconPanel = new JPanel();
		iconPanel.setBorder(null);
		iconPanel.setBackground(new Color(240,240,240));
		iconPanel.setLayout(new BorderLayout(0, 0));
		add(iconPanel);
		
		//Icône de l'état de connexion
		connexionStatus = new JLabel("");
		iconPanel.add(connexionStatus, BorderLayout.EAST);

		//Quand clic => ouvrir popup paramètres
		settingsMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new SettingsPopup(timeTracker);
			}
		});
		
		setConnectionStatus(timeTracker.getTcpClient().getConnexionStatus());
		
		// On créé un timer pour actualiser l'icône d'état de connexion
		Timer timer = new Timer(60000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setConnectionStatus(timeTracker.getTcpClient().getConnexionStatus());
			}
		});

		timer.setRepeats(true);
		timer.setCoalesce(true);
		timer.start();
		
		Observer tcpClientObserver = new Observer()
		{			
			@Override
			public void update(Observable obj, Object arg)
			{
				setConnectionStatus(timeTracker.getTcpClient().getConnexionStatus());
			}
		};
		
		timeTracker.getTcpClient().addObserver(tcpClientObserver);
	}
	
	
	/**
	 * Méthode qui change l'icône d'état de connexion de la pointeuse avec son serveur TCP distant. 
	 * Si isConnected = true, l'icône sera celle qui indique que la pointeuse  est connectée et inversement.
	 * 
	 * @param isConnected L'état de connexion de la pointeuse.
	 */
	public void setConnectionStatus(boolean isConnected)
	{
		connexionStatus.setIcon(new ImageIcon(MenuBar.class.getResource("/assets/images/"+ (isConnected ? connectedStatusImg : notConnectedStatusImg) )));
	}

}
