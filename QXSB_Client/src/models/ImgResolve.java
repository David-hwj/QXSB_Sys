package models;

/*public class ImgResolve {

}*/

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.github.sarxos.webcam.Webcam;

import windows.OperationFrame;


public class ImgResolve extends Thread{

	String videoPath="";
	String resultImagePath="C:\\a2\\result.png";
	static String imgPath=null;
	ActionListener f;
	JLabel initPic;
	Webcam webcam;
	
	public ImgResolve(String videoPath,JLabel initPic,Webcam webcam) {
		this.videoPath=videoPath;
		this.initPic=initPic;
		this.webcam=webcam;
		//this.flag=flag;
	}
	public void fun()
	{
		//JOptionPane.showMessageDialog(null,"处理完成，点击确定查看结果");
		try {
			initPic.setIcon(new ImageIcon(ImageIO.read(new File("C:\\a2\\result.png"))));
//			System.out.println("C:\\result.png");
//			JLabel initPic=new JLabel(new ImageIcon(ImageIO.read(new File("C:\\result.png"))));
//			initPic.setLocation(505, 80);
//			initPic.setSize(485, 600);
//			lowVideoPanel.add(initPic);
		} catch (IOException e1) {
			System.out.println("结果图片加载失败");
			e1.printStackTrace();
		}
	}
	
	public String resolve() {
		Process proc;
		while(OperationFrame.flag==0)
		{		
		
		BufferedImage bi=webcam.getImage();
		File outputfile = new File("C:\\a1\\0.png");
        try {
			ImageIO.write(bi, "png", outputfile);
			//JOptionPane.showMessageDialog(null,"图片保存完毕");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
		try {
			System.out.println("模型开始处理视频");
			proc = Runtime.getRuntime().exec("python C:\\face\\models\\birdge1.py ");// 执行py文件
			System.out.println("模型处理视频结束2");
			//用输入输出流来截取结果
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
		System.out.println("模型处理视频结束2");
		fun();
		
	}
		return resultImagePath;
	}

	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		imgPath=resolve();
	}
	
	
	
}