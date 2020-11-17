package com.SupPlay.soft;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import java.io.File;

import com.SupPlay.entity.myButton;
import com.SupPlay.entity.myFile;
import com.SupPlay.entity.playerMp3;
import com.SupPlay.entity.playerWav;

import javazoom.jl.decoder.JavaLayerException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.ScrollPaneConstants;


@SuppressWarnings("serial")
public class myframe extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JSlider slider;

	private int rowSelected = 0;
	private myFile[] musics = new myFile[0];

	myButton btnPlaypause;
	myButton btnStop;
	JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					myframe frame = new myframe();
					frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("Impossible de lancer la fenetre");
				}
			}
		});
	}

	
	/**
	 * Create the frame.
	 */
	public myframe() {
		setTitle("SupPlay");
		setResizable(false);
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 520);

		/*
		 * LOOK AND FEEL
		 */
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch(InstantiationException e){
		}catch(ClassNotFoundException e){
		}catch(UnsupportedLookAndFeelException e){
		}catch(IllegalAccessException e){
		}

		/*
		 * MENU
		 */
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(25, 25, 112));
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openingWindow();
			}
		});
		mnFile.add(mntmOpen);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem menuItem = new JMenuItem("?");
		mnHelp.add(menuItem);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/*
		 * SCROLLPANEL
		 */
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBounds(10, 11, 570, 395);
		contentPane.add(panel);

		/*
		 * TABLEAU DE MUSIQUE
		 */
		Object[][] datas = new Object[30][3];
		String[] header = {"Name", "Duration", "Type"};
		table = new JTable(datas, header);
		table.setBorder(new TitledBorder(null, "", TitledBorder.CENTER, TitledBorder.BELOW_TOP, null, null));
		table.setFont(new Font("Modern No. 20", Font.PLAIN, 16));
		table.setBackground(SystemColor.inactiveCaption);
		table.setShowHorizontalLines(false);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				rowSelected = table.getSelectedRow();
			}
		});
		table.setAlignmentY(Component.TOP_ALIGNMENT);
		table.setAlignmentX(Component.LEFT_ALIGNMENT);
		table.setPreferredSize(new Dimension(570, 360));
		table.getColumnModel().getColumn(0).setPreferredWidth(460);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//TODO scroll
		
		panel.add(table.getTableHeader(), BorderLayout.NORTH);
		panel.add(table, BorderLayout.CENTER);

		
		/*
		 * PANEL DE CONTROLES
		 */
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaption);
		panel_1.setBounds(10, 418, 570, 40);
		panel_1.setAlignmentX(CENTER_ALIGNMENT);
		contentPane.add(panel_1);

		/* 
		 *BOUTON PLAY/PAUSE
		 */
		btnPlaypause = new myButton("Play/Pause", "src/icons/play_pause.png");
		
		btnPlaypause.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				playlist();
				play();
			}
		});
		panel_1.add(btnPlaypause);

		/* 
		 *BOUTON STOP
		 */
		btnStop = new myButton("Stop", "src/icons/stop.png");
		
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				stop();
			}
		});
		panel_1.add(btnStop);

		/*
		 *BOUTON PRECEDENT
		 */

		myButton btnPrevious = new myButton("Previous", "src/icons/previous.png");
		
		btnPrevious.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				previous();
			}
		});
		panel_1.add(btnPrevious);

		/*
		 * BARRE DE PROGRESSION
		 */
		progressBar = new JProgressBar();
		progressBar.setBackground(SystemColor.control);
		progressBar.setPreferredSize(new Dimension(260, 20));
		//TODO
		panel_1.add(progressBar);

		/* 
		 *SLIDER SON
		 */
		slider = new JSlider();
		slider.setBorder(null);
		slider.setOpaque(false);
		
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(musics != null){
					if(table.getValueAt(rowSelected, 0) != null){
						System.out.println("volume changement "+slider.getValue());
						musics[rowSelected].getPlayer().setVolume(slider.getValue());
					}
				}
			}
		});
		
		slider.setPreferredSize(new Dimension(80, 30));
		slider.setMinimum(-80);
		slider.setMaximum(0);
		slider.setValue(0);
		panel_1.add(slider);

		/*
		 * BOUTON NEXT
		 */
		myButton btnNext = new myButton("Next", "src/icons/next.png");
		
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				next();
			}
		});
		panel_1.add(btnNext);
		
		/*
		 * TABLE
		 */
	}

	/*
	 * _____________________METHODES____________________________________________________________________________________________________
	 */
	public void openingWindow(){
		/*
		 * Fenetre ouverture de fichiers
		 */
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setDialogTitle("Open music");
		fc.setAcceptAllFileFilterUsed(false);

		/*
		 * Filtres types de fichiers
		 */
		FileNameExtensionFilter music_filter = new FileNameExtensionFilter("mp3 file", "mp3");
		fc.addChoosableFileFilter(music_filter);
		FileNameExtensionFilter music_filter2 = new FileNameExtensionFilter("WAV file", "wav");
		fc.addChoosableFileFilter(music_filter2);

		/*
		 * Récupération des fichiers sélectionnés
		 */
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			
			File[] files = fc.getSelectedFiles();
			myFile[] musics_open = new myFile[files.length];

			for(int i = 0; i < files.length; i++){
				musics_open[i] = new myFile(files[i].getName(), files[i].getAbsolutePath());
			}

			//copie temporaire des musiques precedemment ouvertes
			myFile[] tmp = (myFile[]) musics.clone();

			//nouvelle liste de "myFile" vide
			musics = new myFile[tmp.length + musics_open.length];

			//ajout des precedentes et nouvelles musiques dans tableau
			for(int i = 0; i < musics.length; i++){
				if(i < tmp.length){
					musics[i] = tmp[i];
				}
				else{
					musics[i] = musics_open[i-tmp.length];
				}
			}

			//Ajout dans tableau d'affichage
			try {
				table = AddToTable(table, musics_open);
			} catch (JavaLayerException e) {
				System.out.println("Erreur ajout de musique à la table");
			}
		}
	}

	public void play(){
		/*
		 * Par défaut, si aucune ligne sélectionnée, play la premiere musique
		 * Si musique a la ligne sélectionnée
		 */
		if(table.getValueAt(rowSelected, 0) != null){
			/*
			 * Arret si musique en lecture, pour toutes 
			 * les musiques sauf celle sélectionnée
			 */ 
			for(int i = 0; i < musics.length; i++){
				if(i != rowSelected){
					if(musics[i].getPlayer() != null){ 
						if(musics[i].getPlayer().isPlaying()){
							musics[i].Stop();
						}
					}
				}
			}

			myFile music = musics[rowSelected];	//musique selectionnée

			/*
			 * Si pas de player déja instancié
			 * instanciation d'un player correspondant au type de la musique
			 */
			if(music.getPlayer() == null){	
				if(music.getType().equals("wav")){
					//playerWav
					music.setPlayer(new playerWav(music));
				}
				else{
					//playerMp3
					try {
						music.setPlayer(new playerMp3(music));
					} catch (JavaLayerException e) {
						System.out.println("Erreur d'initialisation du player");
					}
				}
				//initialisation son (volume maximum)
				music.getPlayer().setVolume(slider.getValue());
			}

			/*
			 * Player instancié
			 * Play ou pause en fonction de l'etat de la musique sélectionné
			 */
			if(music.getPlayer().isPlaying()){
				music.getPlayer().Pause();
				//Modifications bouton
				btnPlaypause.setText("Play");
				btnPlaypause.setIcon(new ImageIcon("src/icons/play.png"));
			}
			else{
				music.getPlayer().Play();
				//modifications bouton
				btnPlaypause.setText("Pause");
				btnPlaypause.setIcon(new ImageIcon("src/icons/pause.png"));
			}
		}
	}


	public void stop(){
		try{
			musics[rowSelected].Stop();
			btnPlaypause.setIcon(new ImageIcon("src/icons/play_pause.png"));
		}catch(Exception e){
			System.out.println("Aucune musique sélectionnée.");
		}
	}


	public JTable AddToTable(JTable table, myFile[] musics) throws JavaLayerException{
		//parcour de musics
		for(int i = 0; i < musics.length; i++){
			//parcour du contenu de table
			for(int x = 0; x < 30; x++){
				if(table.getValueAt(x, 0) == null){
					table.setValueAt(musics[i].getName(), x, 0);
					//table.setValueAt(musics[i].getDuration(), x, 1);
					table.setValueAt(musics[i].getType(), x, 2);

					if(musics[i].getType().equals("wav")){
						table.setValueAt(musics[i].Duration(new playerWav(musics[i])), x, 1);
					}
					else{
						table.setValueAt(musics[i].Duration(new playerMp3(musics[i])), x, 1);
					}
					break;
				}
			}
		}
		return table;
	}


	public void next(){
		if(rowSelected++ < musics.length){ //si il y a une musique suivante
			play();
		}
		else{
			rowSelected--;
		}
	}


	public void previous(){
		if(rowSelected-- >= 0){ //si il y a une musique suivante
			play();
		}
		else{
			rowSelected++;
		}
	}

	/*
	 * Permet de savoir quand la musique est terminé
	 * et recupere la progression pour la progressbar
	 */
	public void playlist(){
		Runnable run = new Runnable() {

			@Override
			public void run() {
				while(true){
					if(table.getValueAt(rowSelected, 0) != null){ 					//éviter out of range
						if(musics[rowSelected].getPlayer() != null){				//player instancié
							if (musics[rowSelected].getPlayer().isTerminated()){	//musique terminée
								musics[rowSelected].Stop();
								if(table.getValueAt(rowSelected++, 0) != null){ 	//si il y a une musique suivante
									play();
								}
								else{
									rowSelected--;
									btnPlaypause.setIcon(new ImageIcon("src/icons/play_pause.png"));
								}
							}
							
							progressBar.setValue(musics[rowSelected].getPlayer().getProgress());
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						System.out.println("Erreur pause sur thread playlist");
					}
				}
			}
		};
		Thread thr = new Thread(run);
		thr.setDaemon(true);
		thr.setPriority(Thread.NORM_PRIORITY);
		thr.start();
	}
}
