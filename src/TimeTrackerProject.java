
/**
 * @file TimeTrackerProject.java
 * @brief Contient la fonction main, qui créée une pointeuse et son IHM
 */

import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.SwingUtilities;

import TimeTrackerBackEnd.TimeTracker;
import TimeTrackerGui.TimeTrackerGUI;

/**
 * @file TimeTrackerProject.java
 * @brief Contient la fonction main permettant de créer une pointeuse et son
 *        IHM.
 * @author Guillaume ELAMBERT
 * @date 2021
 *
 */
public class TimeTrackerProject
{

	/**
	 * Fonction principale du projet.
	 * 
	 * @param args Les arguments passés au programme.
	 */
	public static void main(String... args)
	{
		new Thread(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run()
			{
				try {
					ServerSocket serveur = new ServerSocket(400);
					while(serveur.accept() != null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.run();
			}
		}).start();
		
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				

				TimeTracker timeTracker = new TimeTracker();
				TimeTrackerGUI frame = new TimeTrackerGUI(timeTracker);
				frame.setVisible(true);
			}
		});
	}

}
