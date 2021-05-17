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
 * @brief Contient la fonction main permettant de créer une pointeuse et son IHM.
 * @author Guillaume ELAMBERT
 * @date 2021
 *
 */
public class TimeTrackerProject {

	/**
	 * @param args
	 */
	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				
				try {
					new ServerSocket(400);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				TimeTracker timeTracker = new TimeTracker();
				TimeTrackerGUI frame = new TimeTrackerGUI(timeTracker);
				frame.setVisible(true);
			}
		});
	}

}
