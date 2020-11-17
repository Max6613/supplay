package com.SupPlay.entity;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class playerWav extends player{
	private myFile file;
	private Clip clip;
	private int lastFrame;
	private boolean isPlaying;
	private AudioInputStream audioStream;
	private double duration;
	private int progress;
	
	
	public playerWav(myFile file){
		this.lastFrame = 0;
		this.isPlaying = false;
		this.file = file;
		
		try {
			this.audioStream = AudioSystem.getAudioInputStream(this.file);
			this.clip = AudioSystem.getClip();
			this.clip.open(audioStream);
			
			this.duration = this.clip.getFrameLength() / audioStream.getFormat().getFrameRate();

		} catch (LineUnavailableException e) {
			System.out.println("Erreur d'initialisation du player");
		} catch (IOException e) {
			System.out.println("Erreur d'initialisation du player");
		} catch (UnsupportedAudioFileException e) {
			System.out.println("Erreur d'initialisation du player");
		}
	}
	
	public void Play(){
		this.clip.setFramePosition(lastFrame);
		this.clip.start();
		this.isPlaying = true;
	}
	
	public void Pause(){
		this.lastFrame = this.clip.getFramePosition();
		this.clip.stop();
		this.isPlaying = false;
	}
	
	public void Stop(){
		this.lastFrame = 0;
		this.clip.stop();
		this.isPlaying = false;
	}
	
	//Getters/Setters
	public boolean isPlaying() {
		return this.isPlaying;
	}
	
	public int getProgress(){
		this.progress = this.clip.getFramePosition() * 100 / this.clip.getFrameLength();
		return this.progress;
	}
	
	@Override
	public double getDuration() {
		return duration;
	}

	@Override
	public void setVolume(float vol) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(vol);
		
	}

	@Override
	public boolean isTerminated() {
		if(this.clip != null){

			return this.clip.getFrameLength() == this.clip.getFramePosition();
		}
		return false;
	}
}
