import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

@SuppressWarnings("serial")
public class PointeuseEmulateur extends JFrame {

	private JPanel contentPane;
	private JLabel day;
	private JLabel clock;
	private JLabel trueTime;
	private JTextField idSalarie;
	private JButton submitButton;
	

	/**
	 * Lance l'application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					PointeuseEmulateur frame = new PointeuseEmulateur();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	

	/**
	 * Créé le cadre.
	 */
	public PointeuseEmulateur() {
		setResizable(false);
		setTitle("Pointeuse - Emulateur");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		
		//Conteneur
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(new Color(0, 153, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		//Jour
		day = new JLabel("1 janvier 1970");
		day.setBounds(5, 5, 434, 66);
		day.setHorizontalAlignment(SwingConstants.CENTER);
		day.setFont(new Font("Tahoma", Font.PLAIN, 40));
		day.setForeground(Color.WHITE);
		setDay();
		
		
		//Horloge
		clock = new JLabel("01:00:00");
		clock.setBounds(5, 82, 434, 49);
		clock.setHorizontalAlignment(SwingConstants.CENTER);
		clock.setForeground(Color.WHITE);
		clock.setFont(new Font("Tahoma", Font.PLAIN, 30));
		setClock();
		
		
		//Label Heure retenue
		JLabel lblHeureRetenue = new JLabel("Retenu :");
		lblHeureRetenue.setBounds(152, 136, 67, 22);
		lblHeureRetenue.setForeground(Color.WHITE);
		lblHeureRetenue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblHeureRetenue.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		//Valeur Heure retenue
		trueTime = new JLabel("01:00:00");
		trueTime.setBounds(227, 136, 73, 22);
		trueTime.setHorizontalAlignment(SwingConstants.LEFT);
		trueTime.setForeground(Color.WHITE);
		trueTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
		setTrueTime();
		
		
		//Champ de saisie de l'identifiant de l'employé
		idSalarie = new JTextField();
		idSalarie.setBounds(33, 206, 273, 43);
		String placeholder = "Saisissez l'identifiant du salarié";
		idSalarie.setText(placeholder);
		idSalarie.setColumns(10);
		
		
		//Bouton de validation
		submitButton = new JButton("Valider");
		submitButton.setBounds(327, 206, 89, 43);
		submitButton.setEnabled(false);
		contentPane.setLayout(null);
		contentPane.add(day);
		contentPane.add(clock);
		contentPane.add(lblHeureRetenue);
		contentPane.add(trueTime);
		contentPane.add(idSalarie);
		contentPane.add(submitButton);
		
		
		
		
		//On créé un timer pour actualiser le texte du jour et de l'heure
	    Timer timer = new Timer(500, new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	    	  setDay();
	    	  setClock();
	    	  setTrueTime();
	      }
	    });
	    
	    timer.setRepeats(true);
	    timer.setCoalesce(true);
	    timer.setInitialDelay(0);
	    timer.start();
	    
	    	    
	    //On effectue des vérifications quand le contenu du champ de saisie à changé
	    idSalarie.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
				setSubmitButtonState();
			}
		});
	    
	    //On effectue des vérifications quand le contenu du champ de saisie à changé
		idSalarie.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				setSubmitButtonState();
			}
		});
	    
		
	    //Soit on affiche un texte par défaut si le champ de saisie est vide
	    //Soit on supprime le texte par défaut si l'utilisateur est sur le champ de saisie
	    idSalarie.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (idSalarie.getText().equals(placeholder)) {
		        	idSalarie.setText("");
		        	idSalarie.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (idSalarie.getText().isEmpty()) {
		        	idSalarie.setForeground(Color.GRAY);
		        	idSalarie.setText(placeholder);
		        }
		    }
	    });
	}
	
	
	/**
	 * Fonction qui définit la valeur de la date.
	 */
	public void setDay() {
		day.setText(DateFormat.getDateInstance().format(new Date()));
	}
	
	
	/**
	 * Fonction qui définit la valeur de l'horloge.
	 */
	public void setClock() {
		clock.setText(DateFormat.getTimeInstance().format(new Date()));
	}
	
	
	/**
	 * Fonction qui définit la valeur de l'heure retenue.
	 */
	@SuppressWarnings("deprecation")
	public void setTrueTime() {
		//On récupère la date
		Date currentDate = new Date();
		//On stock la minute
		int currentMinute = currentDate.getMinutes();
		//On calcul la différence avec le quart d'heure
		int mod = currentMinute % 15;
		
		//Si la minute est plus proche du quart d'heure précédent, c'est lui qu'on prend, l'autre sinon
		currentMinute += (mod < 8 ? -mod : (15-mod));
		currentDate.setMinutes(currentMinute);
		currentDate.setSeconds(0);
		
		trueTime.setText(DateFormat.getTimeInstance().format(currentDate));
	}
	
	/**
	 * Fonction qui vérifie que le champ de saisie contient des données au bon format (entiers).
	 * 
	 * @return
	 */
	public boolean checkIdSalarie() {
		return (idSalarie.getText().matches("[0-9]+") && idSalarie.getText().length() > 2);
	}
	
	
	/**
	 * Fonction qui définit si le bouton de validation doit être actif ou non.
	 */
	public void setSubmitButtonState() {
		System.out.println(checkIdSalarie() ? "OK" : "KO");
		submitButton.setEnabled(checkIdSalarie());
	}
}
