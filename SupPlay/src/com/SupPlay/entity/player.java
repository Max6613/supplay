package com.SupPlay.entity;

public abstract class player {

	public abstract boolean isPlaying();
	
	public abstract boolean isTerminated();

	public abstract void Pause();

	public abstract void Play();

	public abstract void Stop();

	public abstract double getDuration();
	
	public abstract int getProgress();
	
	public abstract void setVolume(float vol);

}
