package windows;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class Test {

	public static void main(String[] args) {
		Webcam webcam = Webcam.getDefault();
		
		
		
		webcam.setViewSize(WebcamResolution.VGA.getSize());
 
		WebcamPanel panel = new WebcamPanel(webcam);
 
		panel.setFPSDisplayed(true);
 
		panel.setDisplayDebugInfo(true);
 
		panel.setImageSizeDisplayed(true);
 
		panel.setMirrored(true);
 
		JFrame window = new JFrame("Test webcam panel");
		window.setSize(1000, 800);
		window.setLayout(new FlowLayout());
		window.setLocationRelativeTo(null);//æ”÷–œ‘ æ
 
		//window.add(panel);
 
		window.setResizable(false);
 
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton capture=new JButton("≈ƒ’’");
//		window.add(capture);
		
		JPanel tmp=new JPanel();
		tmp.setBackground(Color.BLUE);
		tmp.setLayout(new FlowLayout());
		tmp.add(panel);
		
		JPanel tmp2=new JPanel();
		tmp2.setBackground(Color.red);
		tmp2.setLayout(new FlowLayout());
		tmp2.add(tmp);
		
		window.add(tmp2);
		
		/*
		//window.pack();
		window.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				BufferedImage bi=webcam.getImage();
				File outputfile = new File("saved.png");
		        try {
					ImageIO.write(bi, "png", outputfile);
					JOptionPane.showMessageDialog(null,"Õº∆¨±£¥ÊÕÍ±œ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		capture.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				BufferedImage bi=webcam.getImage();
				File outputfile = new File("C:\\Users\\HWJ\\Desktop\\saved.png");
		        try {
					ImageIO.write(bi, "png", outputfile);
					JOptionPane.showMessageDialog(null,"Õº∆¨±£¥ÊÕÍ±œ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}
			
		});
 */
		window.setVisible(true);

	}

}
