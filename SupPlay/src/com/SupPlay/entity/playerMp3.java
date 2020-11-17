package com.SupPlay.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class playerMp3 extends player {

	//constante
	private final static int NOTSTARTED = 0;
	private final static int PLAYING = 1;
	private final static int PAUSED = 2;
	private final static int FINISHED = 3;
	
	private Player player;
	private Object playerLock = new Object();
	private int playerStatus = NOTSTARTED;
	
	private FileInputStream inputStream;
	private myFile file;
	private double duration = 0.0;
	boolean isTerminated;
	private int progress;
	
	
	public playerMp3(myFile file) throws JavaLayerException{
		this.file = file;
		try {
			
			this.inputStream = new FileInputStream(this.file);
			this.player = new Player(this.inputStream);
			this.progress = 0;
			
			//duree musique
			Bitstream bitStream = new Bitstream(this.inputStream);
			Header h = bitStream.readFrame();
			long tn = this.inputStream.getChannel().size();
			this.duration = h.total_ms((int) tn/1000);
			
		} catch (FileNotFoundException e) {
			System.out.println("Erreur lors de l'ouverture du fichier");
		} catch (Exception e) {
			System.out.println("Erreur de lecture des données de durée");
		}
		
		
	}

	@Override
	public void Play(){
		this.isTerminated = false;
		synchronized (playerLock) {
			switch(playerStatus){
			case NOTSTARTED:
				Runnable run = new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playInternal();
					}
				};
				Thread thr = new Thread(run);
				thr.setDaemon(true);
				thr.setPriority(Thread.MAX_PRIORITY);
				playerStatus = PLAYING;
				thr.start();
				break;
				
			case PAUSED:
				synchronized (playerLock) {
					if(playerStatus == PAUSED){
						playerStatus = PLAYING;
						playerLock.notifyAll();
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void Pause(){
		synchronized (playerLock) {
			if(playerStatus == PLAYING){
				
				playerStatus = PAUSED;
			}
		}
	}

	@Override
	public void Stop(){
		synchronized (playerLock) {
			playerStatus = FINISHED;
			playerLock.notifyAll();
		}
		this.close();
	}
	
	public void playInternal(){
		while(playerStatus != FINISHED){
			try{
				if(!player.play(PLAYING)){
					
					break;
				}
			}catch(JavaLayerException exc){
				System.out.println("Erreur lors de la lecture");
			}
			
			synchronized (playerLock) {
				while(playerStatus == PAUSED){
					try{
						playerLock.wait();
					}catch(InterruptedException exc1){
						System.out.println("Player arreté");
						break;
					}
				}
			}
		}
		close();
		this.isTerminated = true; //playerStatus = finished
	}
	
	public void close(){
		synchronized (playerLock) {
			playerStatus = FINISHED;
		}
		try{
			player.close();
		}catch(Exception exc){
			System.out.println("Player arreté");
		}
	}
	
	@Override
	public boolean isPlaying() {
		//return this.playerStatus == 1;
		if(this.playerStatus == 1){
			
			return true;
		}
		return false;
	}
	
	/*
	 * Getters
	 * Setters
	 */
	public double getDuration() {
		return duration;
	}

	public void setVolume(float vol) {
		/*
		 * Formule pour passer d'un ensemble [-80; 0] (réglage son pour .wav)
		 * à un ensemble [0.0; 1.0] (réglage son pour .mp3)
		 */
		vol = (vol / 80) + 1;

		Mixer.Info [] mixers = AudioSystem.getMixerInfo();

		for (Mixer.Info mixerInfo : mixers){
			
		    Mixer mixer = AudioSystem.getMixer(mixerInfo);
		    if (mixer.isLineSupported(Port.Info.SPEAKER)){
		    	
				try {
					Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
					port.open();
					if (port.isControlSupported(FloatControl.Type.VOLUME)){
						
			    		FloatControl volume = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
			    		volume.setValue(vol);
			    	}
				} catch (LineUnavailableException e) {
					System.out.println("Erreur réglage du son");
				}
		    	
		    	
		    	
		    }
		}
	}

	@Override
	public boolean isTerminated() {
		try{
			if(this.inputStream.available() == 0){
				
				return true;
			}
			return false;
		}catch(IOException e){
			System.out.println("Erreur lors de la lecture");
		}
		return false;
	}
	
	@Override
	public int getProgress(){
		
		return this.progress;
	}
}
