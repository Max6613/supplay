package com.SupPlay.entity;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class myButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public myButton(String txt, String icon){
		super(new ImageIcon(icon));
		
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		
		setPreferredSize(new Dimension(39, 30));
		setHorizontalAlignment(SwingConstants.CENTER);
		setHorizontalTextPosition(SwingConstants.CENTER);
		
	}
}
