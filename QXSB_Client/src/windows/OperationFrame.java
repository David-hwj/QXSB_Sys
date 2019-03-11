package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.control.Control;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import models.ImgResolve;
import models.ImgResolve0;
import models.VideoResolve;
/**
 * 操作主界面
 * @author HWJ
 *
 */
public class OperationFrame extends JFrame{

	int frameWidth=1130,frameHeight=650;
	
	JMenuBar topMenuBar;
	JMenu fileMenu,funcationChooseMenu;
	JMenuItem exitMenuItem,videoMenuItem,cameraMenuItem; 
	
	
	
	JPanel lowVideoPanel,lowCameraPanel;
	
	String getVideoPath=null;
	
//	String initPicturePath=".\\libs\\testPic.png";
	String initPicturePath="picture/icon.png";
	
	JFrame jf;
	Webcam webcam=null;
	public static int flag=0;
	
	public OperationFrame() throws HeadlessException {
		super();
		initFrame();
	}
	
	
	private void initFrame() {
		jf=this;
		//窗口属性设置
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("人脸情绪识别系统");
		this.setSize(frameWidth, frameHeight);
		this.setLocationRelativeTo(null);//居中显示
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		//顶部菜单栏设置
		topMenuBar=new JMenuBar();
		this.add(topMenuBar,BorderLayout.NORTH);
		
		fileMenu=new JMenu("系统");
		topMenuBar.add(fileMenu);
		exitMenuItem=new JMenuItem("退出");
		fileMenu.add(exitMenuItem);
		
		funcationChooseMenu = new JMenu("功能选择");
		topMenuBar.add(funcationChooseMenu);
		videoMenuItem=new JMenuItem("视频输入");
		cameraMenuItem=new JMenuItem("相机输入");
		funcationChooseMenu.add(videoMenuItem);
		funcationChooseMenu.add(cameraMenuItem);
		
		//退出按钮监听
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}	
		});
		
		//视频选择监听
		videoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jf.remove(lowCameraPanel);
				jf.add(lowVideoPanel,BorderLayout.CENTER);
//				lowVideoPanel.add(new JLabel("HHHHHH"));
				jf.setVisible(false);
				jf.setVisible(true);
				
				
				
			}	
		});
		//相机输入监听
		cameraMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jf.remove(lowVideoPanel);
				jf.add(lowCameraPanel,BorderLayout.CENTER);
//				lowVideoPanel.add(new JLabel("HHHHHH"));
				jf.setVisible(false);
				jf.setVisible(true);
			}	
		});
		
		
		
		
		//下部面板设置
		lowVideoPanel=new JPanel();
		lowVideoPanel.setBackground(Color.WHITE);
//		lowVideoPanel.setLayout(new GridBagLayout());
		lowVideoPanel.setLayout(null);
		JButton videoInput=new JButton("选择视频输入");
		JButton videoPlay=new JButton("播放视频");
		JLabel path=new JLabel("已选路径:  "+getVideoPath);
		JButton videoResolve=new JButton("开始处理");
		Control control=new Control();
		JPanel videoPlayPanel=control.getFrame();//视频播放面板
		
		
		java.net.URL imgURL=this.getClass().getClassLoader().getResource(initPicturePath);
		ImageIcon img=new ImageIcon(imgURL);
//		BufferedImage img=null;
//		try {
//			img=ImageIO.read(new File(initPicturePath));
//		} catch (IOException e1) {
//			System.out.println("初始图片加载失败");
//			e1.printStackTrace();
//		}
//		JLabel initPic=new JLabel(new ImageIcon(img));
		JLabel initPic=new JLabel(img);
//		lowVideoPanel.add(videoInput,new GBC(1,0,2,1).setAnchor(GBC.EAST).setIpad(50, 10).setWeight(0, 0));
//		lowVideoPanel.add(videoPlay,new GBC(0,1,1,1).setAnchor(GBC.EAST).setIpad(50, 10).setWeight(0, 0));
//		lowVideoPanel.add(path,new GBC(1,1,2,1).setAnchor(GBC.CENTER).setIpad(200, 10).setWeight(0, 0));
//		lowVideoPanel.add(videoResolve,new GBC(3,0,1,1).setAnchor(GBC.EAST).setIpad(50, 10).setWeight(0, 0)	);
//		lowVideoPanel.add(videoPlayPanel,new GBC(0,1,4,1).setFill(GBC.CENTER).setWeight(0, 0)	);
//		lowVideoPanel.add(initPic,new GBC(4,1,4,1).setFill(GBC.CENTER).setWeight(0, 0)	);
		videoResolve.setLocation(490, 20);
		videoResolve.setSize(120, 40);
		lowVideoPanel.add(videoResolve);
		videoPlayPanel.setLocation(20, 110);
		videoPlayPanel.setSize(500, 500);
		lowVideoPanel.add(videoPlayPanel);
		initPic.setLocation(590, 50);
		initPic.setSize(500, 550);
		lowVideoPanel.add(initPic);
		
		
		this.add(lowVideoPanel, BorderLayout.CENTER);
		//相机面板
		lowCameraPanel=new JPanel();
		lowCameraPanel.setBackground(Color.WHITE);
//		lowCameraPanel.setLayout(new GridBagLayout());
		lowCameraPanel.setLayout(null);
		JButton openCamera=new JButton("打开相机");
		openCamera.setLocation(10, 10);
		openCamera.setSize(150, 30);
		lowCameraPanel.add(openCamera);
		
		JButton capture = new JButton("拍照识别并保存");
		capture.setLocation(150, 10);
		capture.setSize(150,30);
		lowCameraPanel.add(capture);
		capture.setEnabled(false);
		
		JButton v_start= new JButton("开始处理实时视频");
		v_start.setLocation(600, 10);
		v_start.setSize(150,30);
		lowCameraPanel.add(v_start);
		v_start.setEnabled(false);
		
		JButton v_end= new JButton("结束处理实时视频");
		v_end.setLocation(750, 10);
		v_end.setSize(150,30);
		lowCameraPanel.add(v_end);
		v_end.setEnabled(false);
		
		BufferedImage img2=null;
		
		try {
			img2=ImageIO.read(new File(initPicturePath));
		} 
			
			/*//filenameTemp = path+fileName+".txt";//文件路径+名称+文件类型
	        File file = new File("C:\\result.png");
	        try {
	            //如果文件不存在，则创建新的文件
	            if(!file.exists())
	            {
	                file.createNewFile();
	                img2=ImageIO.read(new File("C:\\result.png"));
	                //bool = true;
	                //System.out.println("success create file,the file is "+filenameTemp);
	                //创建文件成功后，写入内容到文件里
	                //writeFileContent(filenameTemp, filecontent);
	            }*/
			/*int width = 400;
			int height = 300;
			// 创建BufferedImage对象
			BufferedImage image = new BufferedImage(width, height,     BufferedImage.TYPE_INT_RGB);
			// 获取Graphics2D
			Graphics2D g2d = image.createGraphics();
			// 画图
			g2d.setColor(new Color(255,0,0));
			g2d.setStroke(new BasicStroke(1));
			g2d.draw
			//释放对象
			g2d.dispose();
			// 保存文件    
			ImageIO.write(image, "png", new File("c:/test.png"));*/
			
			
			
		catch (IOException e1) {
			System.out.println("初始图片加载失败");
			e1.printStackTrace();
		}
		
		
		JLabel pic=new JLabel(new ImageIcon(initPicturePath));
		pic.setLocation(540, 0);
		pic.setSize(640,640);
		lowCameraPanel.add(pic);
		
		v_end.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				flag=1;
				v_start.setEnabled(true);
			}
		});
		
		v_start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				File file=new File("C:\\a1");
				if(!file.exists()){//如果文件夹不存在
					file.mkdir();//创建文件夹
				}
				
				File file2=new File("C:\\a2");
				if(!file2.exists()){//如果文件夹不存在
					file2.mkdir();//创建文件夹
				}
				flag=0;
				ImgResolve vr=new ImgResolve("C:\\a1\\0.png",pic,webcam);
				v_start.setEnabled(false);
				vr.start();
				
				
				//JOptionPane.showMessageDialog(null,"处理结束会有提示,请点击确定继续");
				//String ResultImagePath="C:\\result.png";
//				String ResultImagePath2=vr.resolve();
				
				/*BufferedImage bi=webcam.getImage();
				File outputfile = new File("C:\\0.png");
		        try {
					ImageIO.write(bi, "png", outputfile);
					JOptionPane.showMessageDialog(null,"图片保存完毕");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
		 
		        /*while(1==1)
		        {*/
		        	
		        	
		        	/*BufferedImage bi1=webcam.getImage();
					File outputfile1 = new File("C:\\0.png");
			        try {
						ImageIO.write(bi, "png", outputfile1);
						//JOptionPane.showMessageDialog(null,"图片保存完毕");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
	
				/*Process proc;
				try {
					System.out.println("模型开始处理视频");
					//String videoPath1;
					proc = Runtime.getRuntime().exec("python .\\src\\models\\birdge1.py ");// 执行py文件
					System.out.println("模型处理视频结束");
					//用输入输出流来截取结果
					BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					String line = null;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
					}
					in.close();
					proc.waitFor();
					System.out.println("模型处理视频结束2");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} */
	        	
                       //pic.setIcon(new ImageIcon(ImageIO.read(new File("C:\\result.png"))));
		        	
		        	
		    		/*try {
		    			pic.setIcon(new ImageIcon(ImageIO.read(new File("C:\\result.png"))));
						//System.out.println(ResultImagePath);
						JLabel initPic=new JLabel(new ImageIcon(ImageIO.read(new File(ResultImagePath))));
						initPic.setLocation(505, 80);
						initPic.setSize(485, 600);
						lowVideoPanel.add(initPic);
					} catch (IOException e1) {
						System.out.println("结果图片加载失败");
						e1.printStackTrace();
					}*/
		        	
	
		        //}
		                
			}
			
		});
		
		
		
		capture.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				File file=new File("C:\\a1");
				if(!file.exists()){//如果文件夹不存在
					file.mkdir();//创建文件夹
				}
				
				File file2=new File("C:\\a2");
				if(!file2.exists()){//如果文件夹不存在
					file2.mkdir();//创建文件夹
				}
				
				
				BufferedImage bi=webcam.getImage();
				
				
				Date currentTime = new Date(); 
				//改变输出格式（自己想要的格式） 
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
				//得到字符串时间 
				String tablename=dateFormat.format(currentTime);
				
				String videoPath2="C:\\a1\\"+tablename+".png";
				
				File outputfile = new File(videoPath2);
				
		        try {
					ImageIO.write(bi, "png", outputfile);
					JOptionPane.showMessageDialog(null,"已截取当前图像并保存至目录\nC:\\a1");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		        ImgResolve0 vr=new ImgResolve0(videoPath2);
				vr.start();
					
			}
			
		});
		
//		//面板按钮监听
//		videoInput.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser jf = new JFileChooser();
//		        jf.setSelectedFile(new File("C:\\Users"));
//		        int value = jf.showSaveDialog(null);
//		        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		        jf.setFileHidingEnabled(false);
//		        if (value == JFileChooser.APPROVE_OPTION) {
//		            getVideoPath = jf.getSelectedFile().toString();
//		            path.setText("已选路径:  "+getVideoPath);
//		            System.out.println(getVideoPath);       
//		        } else {
//		        	System.out.println("你取消了选择文件");
//		        }
//				
//			}	
//		});
//		
		videoResolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String format = "";
				int path_size=Control.videoPath.length();
				format=format+Control.videoPath.charAt(path_size-3);
				format=format+Control.videoPath.charAt(path_size-2);
				format=format+Control.videoPath.charAt(path_size-1);
				//System.out.println(format);
				if(
						format.equals("Mp4")||
						format.equals("mP4")||
						format.equals("MP4")||
						format.equals("mp4")||
						format.equals("avi")||
						format.equals("avI")||
						format.equals("aVi")||
						format.equals("aVI")||
						format.equals("Avi")||
						format.equals("AvI")||
						format.equals("AVi")||
						format.equals("AVI")
						
						)
				{
					
				}
				else
				{
                    JOptionPane.showMessageDialog(null,"视频格式不是AVI或MP4，无法处理");
					
					return;
				}
				
				
			
				double value = 0;
				FileChannel fc= null; 
				String size = "";
				try {
				@SuppressWarnings("resource")
				FileInputStream fis = new FileInputStream(Control.videoPath);
				fc= fis.getChannel();
				BigDecimal fileSize = new BigDecimal(fc.size());
				size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP)+"";
				value = Double.valueOf(size.toString());
				//System.out.println(value);
				} catch (FileNotFoundException e4) {
				e4.printStackTrace();
				} catch (IOException e4) {
				e4.printStackTrace();
				} finally { 
				if (null!=fc){ 
				try{ 
				fc.close(); 
				}catch(IOException e4){ 
				e4.printStackTrace(); 
				} 
				} 
				}
				
				
				if(value>100.0)
				{
					JOptionPane.showMessageDialog(null,"视频大小超过100MB，无法处理");
					
					return;
				}
				
				
				
				
				
				
				
				
				
				
				VideoResolve vr=new VideoResolve(Control.videoPath,initPic,videoResolve);
				JOptionPane.showMessageDialog(null,"处理结束会有提示,请点击确定继续");
				videoResolve.setEnabled(false);//设置开始处理按钮不可点击
				
				String ResultImagePath="C:\\result.png";
//				String ResultImagePath2=vr.resolve();
				vr.start();
				
				
				/*String s = "0";
				FileWriter fw;
				try {
					fw = new FileWriter("C:\\\\data.txt");
					fw.write(s,0,s.length());
					fw.flush();
					
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				
				
				
				String filePath="C:\\data.txt";
				
				//String s="0";
				int i=-1;
				    while(i!=1)
				   {
					   try {
				           String encoding="UTF-8";
				           File file=new File(filePath);
				           if(file.isFile() && file.exists()){ //判断文件是否存在
				               InputStreamReader read = new InputStreamReader(
				               new FileInputStream(file),encoding);//考虑到编码格式
				               BufferedReader bufferedReader = new BufferedReader(read);
				               String lineTxt = null;
				               while((lineTxt = bufferedReader.readLine()) != null){
				                   //System.out.println(lineTxt);
				                  s=lineTxt;
				                  i=Integer.parseInt(s);
				                  System.out.println(i);
				               }
				               read.close();
				   }else{
				       System.out.println("找不到指定的文件");
				   }
				   } catch (Exception e2) {
				       System.out.println("读取文件内容出错");
				       e2.printStackTrace();
				       //e2.printStackTrace();
				   }
				   }*/
	    
			}	
			
			public void fun()
			{
				//JOptionPane.showMessageDialog(null,"处理完成，点击确定查看结果");
				try {
					initPic.setIcon(new ImageIcon(ImageIO.read(new File("C:\\result.png"))));
//					System.out.println("C:\\result.png");
//					JLabel initPic=new JLabel(new ImageIcon(ImageIO.read(new File("C:\\result.png"))));
//					initPic.setLocation(505, 80);
//					initPic.setSize(485, 600);
//					lowVideoPanel.add(initPic);
				} catch (IOException e1) {
					System.out.println("结果图片加载失败");
					e1.printStackTrace();
				}
			}
		});
		
		openCamera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				webcam = Webcam.getDefault();
				webcam.setViewSize(WebcamResolution.VGA.getSize());
				WebcamPanel panel = new WebcamPanel(webcam);
				panel.setFPSDisplayed(true);
				panel.setDisplayDebugInfo(true);
				panel.setImageSizeDisplayed(true);
				panel.setMirrored(true);
				panel.setLocation(20, 140);
				panel.setSize(500, 375);
				lowCameraPanel.add(panel);
				jf.setVisible(false);
				jf.setVisible(true);
				
				capture.setEnabled(true);
				v_start.setEnabled(true);
				v_end.setEnabled(true);
			}
		});
		
		
		
		
	}


	public static void main(String[] args) {
		OperationFrame of=new OperationFrame();
		of.setVisible(true);
	}

}
