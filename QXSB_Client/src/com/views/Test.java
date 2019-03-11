package com.views;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.control.Control;

public class Test{
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JFrame jf=new JFrame();
		jf.setBounds(100, 100, 700, 700);
    	jf.setLayout(null);
		Control a = new Control();
		JPanel jp=a.getFrame();
		jp.setSize(80, 100);
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		
	}

}
