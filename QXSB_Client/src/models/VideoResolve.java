package models;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class VideoResolve extends Thread{

	String videoPath="";
	String resultImagePath="C:\\a2\\result.png";
	static String imgPath=null;
	ActionListener f;
	JLabel initPic;
	JButton videoResolve;
	public VideoResolve(String videoPath,JLabel initPic,JButton videoResolve) {
		this.videoPath=videoPath;
		this.initPic=initPic;
		this.videoResolve=videoResolve;
	}
	public void fun()
	{
		JOptionPane.showMessageDialog(null,"������ɣ����ȷ���鿴���");
		try {
			initPic.setIcon(new ImageIcon(ImageIO.read(new File("C:\\a2\\result.png"))));
			videoResolve.setEnabled(true);//���ô���ť�ɵ��
//			System.out.println("C:\\result.png");
//			JLabel initPic=new JLabel(new ImageIcon(ImageIO.read(new File("C:\\result.png"))));
//			initPic.setLocation(505, 80);
			// initPic.setSize(500, 550);
//			lowVideoPanel.add(initPic);
		} catch (IOException e1) {
			System.out.println("���ͼƬ����ʧ��");
			e1.printStackTrace();
		}
	}

	public String resolve() {
		Process proc;
		try {
			System.out.println("ģ�Ϳ�ʼ������Ƶ");
			proc = Runtime.getRuntime().exec("python C:\\face\\models\\bridge.py "+videoPath);// ִ��py�ļ�
//			System.out.println("ģ�ʹ�����Ƶ����");
			//���������������ȡ���
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("ģ�ʹ�����Ƶ����2");
		fun();
		return resultImagePath;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		imgPath=resolve();
	}



}
