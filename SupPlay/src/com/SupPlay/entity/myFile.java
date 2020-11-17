package com.SupPlay.entity;
import java.io.File;


@SuppressWarnings("serial")
public class myFile extends File {

	
	private String type;
	private String path;
	private String name;
	private double duration;
	private player player;
	
	
	public myFile(String name, String path) {
		super(path);
		this.name = name;
		this.path = path;
		this.type = setType(path);
	}
	
	public String setType(String path){
		int len = path.length();
		String type = new String();
		
		for(int i = 3; i > 0; i--){
			
			type += path.charAt(len-i);
		}
		return type;
	}
	
	public void Stop(){
		if(this.player != null){
			
			this.player.Stop();
			this.player = null;
		}
		
	}
	
	public String Duration(player player){
		/*
		 * Initialisation puis destruction du player
		 * afin de pouvoir récupérer la durée de la musique
		 */
		this.player = player;
		this.player.Play();
		this.duration = this.player.getDuration();
		this.player.Stop();
		this.player = null;
		
		int min = (int) (this.duration / 60);
		int sec = (int) (this.duration % 60);
		
		return min+"m"+sec;
	}
	
	//Getters/Setters
	public double getDuration() {		
		return duration;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public player getPlayer() {
		return player;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPlayer(player player) {
		this.player = player;
	}
	
	public boolean isTerminated() {
		if(this.player != null){
			
			return this.player.isPlaying();
		}
		return false;
	}
}