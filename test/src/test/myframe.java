package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class myframe extends JFrame {

	private JPanel contentPane;
	File music = new File("C:/Users/Max/Documents/YG_IDGAF.wav");
	myFile file = new myFile("yg", "C:/Users/Max/Documents/YG_IDGAF.wav");
	playerWav player;
	
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
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public myframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 108);
		contentPane.add(panel);
		
		JButton btnLoad = new JButton("load");
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				System.out.println("load");
				player = new playerWav(file);
				
			}
		});
		panel.add(btnLoad);
		
		JButton btnPlaypause = new JButton("play/pause");
		btnPlaypause.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(player.isPlaying()){
					System.out.println("pause");
					player.Pause();
				}
				else{
					System.out.println("play");
					player.Play();
				}
			}
		});
		panel.add(btnPlaypause);
		
		JButton btnStop = new JButton("stop");
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(player != null){
					player.Stop();
				}
			}
		});
		panel.add(btnStop);
	}
}
