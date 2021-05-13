/**
 * @file ProjetPointeuse.java
 * @brief Contient la fonction main, qui créée une pointeuse et son IHM
 */

import javax.swing.SwingUtilities;

import PointeuseBackEnd.Pointeuse;
import PointeuseGui.PointeuseGUI;

/**
 * @author Guillaume
 *
 */
public class ProjetPointeuse {

	/**
	 * @param args
	 */
	public static void main(String... args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Pointeuse pointeuse = new Pointeuse();
				PointeuseGUI frame = new PointeuseGUI(pointeuse);
				frame.setVisible(true);
			}
		});
	}

}
