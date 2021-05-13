package PointeuseGui;
/**
 * @file MenuBar.java
 * @brief Contient la classe permettant la création d'une barre d'options dans l'IHM de la pointeuse. 
 * @author Guillaume ELAMBERT
 * @date 2021
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import PointeuseBackEnd.Pointeuse;

@SuppressWarnings("serial")
/**
 * @brief Classe permettant la création d'une barre d'options dans l'IHM de la pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class MenuBar extends JMenuBar {

	private JMenu menu;
	private JMenuItem settingsMenuItem;

	/**
	 * Create the panel.
	 */
	public MenuBar(Pointeuse pointeuse) {
		menu = new JMenu("Manage");
		settingsMenuItem = new JMenuItem("Settings",
				new ImageIcon(MenuBar.class.getResource("/assets/images/settings-20px.png")));

		menu.add(settingsMenuItem);
		add(menu);

		settingsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsPopup(pointeuse);
			}
		});
	}

}
