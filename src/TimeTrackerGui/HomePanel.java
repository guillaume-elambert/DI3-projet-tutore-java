/**
 * @file HomePanel.java
 * @brief Contient la classe permettant la création de l'écran principal de l'IHM de la pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */

package TimeTrackerGui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import TimeTrackerBackEnd.TimeTracker;

@SuppressWarnings("serial")
/**
 * @brief Classe permettant la création de l'écran principal de l'IHM de la pointeuse.
 * @author Guillaume ELAMBERT
 * @date 2021
 */
public class HomePanel extends JPanel
{

	private JLabel day;						/**< Le label contenant le jour actuel. */
	private JLabel clock;					/**< Le label contant l'heure actuelle. */
	private JPanel trueTimeContainer;		/**< Le conteneur de l'heure retenue et son label. */
	private JLabel trueTime;				/**< La label contenant l'heure retenue. */
	private JPanel champs;					/**< Le conteneur du champ de saisie et le bouton de validation. */
	private JTextField idEmployee;			/**< Le champ de saisie de l'identifiant de l'employé. */
	private JButton submitButton;			/**< Le bouton de validation. */
	private static Calendar currentDate;	/**< La date actuelle. */

	
	/**
	 * Constructeur
	 * 
	 * @param timeTracker La pointeuse dont on créé l'IHM.
	 */
	public HomePanel(TimeTracker timeTracker)
	{
		// Paramètres du conteneur
		setPreferredSize(new Dimension(450, 300));
		setLayout(null);
		setForeground(Color.WHITE);
		setBackground(new Color(0, 153, 204));

		// Jour
		day = new JLabel("1 janvier 1970");
		day.setBounds(0, 23, 450, 66);
		day.setHorizontalAlignment(SwingConstants.CENTER);
		day.setFont(new Font("Tahoma", Font.PLAIN, 40));
		day.setForeground(Color.WHITE);
		setDay();
		add(day);

		// Horloge
		clock = new JLabel("01:00:00");
		clock.setBounds(0, 113, 450, 49);
		clock.setHorizontalAlignment(SwingConstants.CENTER);
		clock.setForeground(Color.WHITE);
		clock.setFont(new Font("Tahoma", Font.PLAIN, 30));
		setClock();
		add(clock);

		/*---- Container de l'heure retenue + son label ----*/

		trueTimeContainer = new JPanel();
		trueTimeContainer.setBounds(155, 161, 139, 44);
		trueTimeContainer.setOpaque(false);
		trueTimeContainer.setLayout(null);
		add(trueTimeContainer);

		// Label Heure retenue
		JLabel lblHeureRetenue = new JLabel("Retenu :");
		lblHeureRetenue.setBounds(0, 12, 66, 19);
		lblHeureRetenue.setForeground(Color.WHITE);
		lblHeureRetenue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblHeureRetenue.setHorizontalAlignment(SwingConstants.CENTER);
		trueTimeContainer.add(lblHeureRetenue);

		// Valeur Heure retenue
		trueTime = new JLabel("01:00:00");
		trueTime.setBounds(73, 12, 66, 19);
		trueTime.setHorizontalAlignment(SwingConstants.LEFT);
		trueTime.setForeground(Color.WHITE);
		trueTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
		trueTimeContainer.add(trueTime);
		setTrueTime();

		/*---- Fin container de l'heure retenue + son label ----*/

		/*---- Container du champ + bouton ----*/
		String placeholder = "Saisissez l'identifiant du salarié";
		champs = new JPanel();
		champs.setBounds(45, 233, 360, 44);
		champs.setOpaque(false);
		champs.setLayout(null);
		add(champs);

		// Champ de saisie de l'identifiant de l'employé
		idEmployee = new JTextField();
		idEmployee.setBounds(0, 2, 248, 39);
		idEmployee.setText(placeholder);
		idEmployee.setColumns(10);
		champs.add(idEmployee);

		// Bouton de validation
		submitButton = new JButton("Valider");
		submitButton.setBounds(269, 2, 91, 40);
		submitButton.setEnabled(false);
		champs.add(submitButton);

		/*---- Fin container du champ + bouton ----*/

		// On vérifie s'il l'on a pressé le bouton de validation
		submitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{				
				timeTracker.getTcpClient().sendTimeTrackingData(
						DateFormat.getDateInstance(DateFormat.SHORT).format(currentDate.getTime()), 
						DateFormat.getTimeInstance().format(currentDate.getTime()), 
						Integer.parseInt(idEmployee.getText())
				);
			}
		});

		// On effectue des vérifications quand le contenu du champ de saisie a changé
		idEmployee.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSubmitButtonState();
			}
		});

		// On effectue des vérifications quand le contenu du champ de saisie a changé
		idEmployee.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				setSubmitButtonState();
			}
		});

		// Soit on affiche un texte par défaut si le champ de saisie est vide
		// Soit on supprime le texte par défaut si l'utilisateur est sur le champ de
		// saisie
		idEmployee.addFocusListener(new FocusListener()
		{
			@Override
			public void focusGained(FocusEvent e)
			{
				if (idEmployee.getText().equals(placeholder))
				{
					idEmployee.setText("");
					idEmployee.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				if (idEmployee.getText().isEmpty())
				{
					idEmployee.setForeground(Color.GRAY);
					idEmployee.setText(placeholder);
				}
			}
		});

		
		// On créé un timer pour actualiser le texte du jour et de l'heure
		Timer timer = new Timer(500, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setDay();
				setClock();
				setTrueTime();
			}
		});

		timer.setRepeats(true);
		timer.setCoalesce(true);
		timer.setInitialDelay(0);
		timer.start();
	}

	
	/**
	 * Fonction qui définit la valeur de la date.
	 */
	public void setDay()
	{
		day.setText(DateFormat.getDateInstance().format(Calendar.getInstance().getTime()));
	}

	
	/**
	 * Fonction qui définit la valeur de l'horloge.
	 */
	public void setClock()
	{
		clock.setText(DateFormat.getTimeInstance().format(Calendar.getInstance().getTime()));
	}

	
	/**
	 * Fonction qui définit la valeur de l'heure retenue.
	 */
	public void setTrueTime()
	{
		
		currentDate = Calendar.getInstance();
		
		// On calcul la différence avec le quart d'heure
		int mod = currentDate.get(Calendar.MINUTE) % 15;

		// Si la minute est plus proche du quart d'heure précédent, c'est lui qu'on prend, l'autre sinon
		currentDate.add(Calendar.MINUTE, (mod < 8 ? -mod : (15 - mod)));
		currentDate.set(Calendar.SECOND, 0);
		
		trueTime.setText(DateFormat.getTimeInstance().format(currentDate.getTime()));
	}

	
	/**
	 * Fonction qui vérifie que le champ de saisie contient des données au bon
	 * format (entiers).
	 * 
	 * @return true si l'identifaint de l'emplayé est bien formaté, false sinon.
	 */
	public boolean checkIdEmployee()
	{
		return (idEmployee.getText().matches("[0-9]+") && idEmployee.getText().length() > 2);
	}

	
	/**
	 * Fonction qui définit si le bouton de validation doit être actif ou non.
	 */
	public void setSubmitButtonState()
	{
		submitButton.setEnabled(checkIdEmployee());
	}
}
