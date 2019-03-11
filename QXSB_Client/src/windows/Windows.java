package windows;
import java.sql.*;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class Windows extends JFrame {
	JFrame thisJF ;
	public Windows() {	
		thisJF=this;
		// 设置标题
		setTitle("请登陆");
		// 绝对布局
		setLayout(null);
		// 定义一个容器
		Container c = getContentPane();
		// 创建"用户名:"标签
		JLabel jl1 = new JLabel("用户名：");
		// 创建文本框
		final JTextField jtf1 = new JTextField();
		// 创建"密码:"标签
		JLabel jl2 = new JLabel("密码:");
		// 创建密码框
		final JPasswordField jpf1 = new JPasswordField();
		// 设置密码字符为*
		jpf1.setEchoChar('*');
		// 创建"登录"按钮
		JButton jb1 = new JButton("登录");
		// 创建"注册"按钮
		JButton jb2 = new JButton("注册");
		JButton jb3 = new JButton("忘记密码");
		jb1.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
				      Class.forName("com.mysql.cj.jdbc.Driver");     //加载MYSQL JDBC驱动程序   
				      //Class.forName("org.gjt.mm.mysql.Driver");
				     System.out.println("Success loading Mysql Driver!");
				    }
				    catch (Exception E) {
				      System.out.print("Error loading Mysql Driver!");
				      E.printStackTrace();
				    }
				    try {
				      Connection connect = DriverManager.getConnection(
				          "jdbc:mysql://47.106.20.56:3306/qxsb_user?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true","root","@SkyrimOf3");
				           //连接URL*******				 
				      System.out.println("Success connect Mysql server!");
				      String namedata=jtf1.getText();
						char[] pd=jpf1.getPassword();
						String pdata = new String(pd);
						String ncheck="select * from user where name= ?";
						PreparedStatement preStatement =connect.prepareStatement(ncheck);
						preStatement.setString(1,namedata);
						ResultSet rs = preStatement.executeQuery();
						if  (rs.next()) {
							String pcheck="select * from user where  password= ?";
							PreparedStatement preStatement1 =connect.prepareStatement(pcheck);
							preStatement1.setString(1,pdata);
							ResultSet rs1 = preStatement1.executeQuery();
							if(rs1.next()) {
							JOptionPane.showMessageDialog(null, "登陆成功！");
							OperationFrame of=new OperationFrame();
							of.setVisible(true);
							thisJF.dispose();
							}
							else {
								JOptionPane.showMessageDialog(null, "密码错误！");
								jtf1.setText("");
								jpf1.setText("");
							}
						} else  {
							JOptionPane.showMessageDialog(null, "该用户不存在！");
							// 清零
							jtf1.setText("");
							jpf1.setText("");
						}
				    }
				    catch (Exception E) {
				      System.out.print("get data error!");
				      E.printStackTrace();
				    }
			}
		});
		// 实现"注册"按钮功能
		jb2.addActionListener(new ActionListener() {
 
			@Override
			public void actionPerformed(ActionEvent arg0) {
                 new OtherWindows();
				 thisJF.dispose();
			}
		});
		jb3.addActionListener(new ActionListener() {
			 
			@Override
			public void actionPerformed(ActionEvent arg0) {
                 new Rpassword();
				 thisJF.dispose();
			}
		});
		// 将各组件添加到容器中
		c.add(jl1);
		c.add(jtf1);
		c.add(jl2);
		c.add(jpf1);
		c.add(jb1);
		c.add(jb2);
		c.add(jb3);
		// 设置各组件的位置以及大小
		jl1.setBounds(60, 160, 90, 30);
		jtf1.setBounds(110, 160, 210, 30);
		jl2.setBounds(75, 200, 90, 30);
		jpf1.setBounds(110, 200, 210, 30);
		jb1.setBounds(90, 240, 60, 40);
		jb2.setBounds(160, 240, 60, 40);
		jb3.setBounds(230, 240, 100, 40);
		// 设置窗体大小、关闭方式、不可拉伸
		setSize(440, 400);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	
		ImageIcon img = null;
		java.net.URL imgURL=this.getClass().getClassLoader().getResource("picture/1.png");
		img=new ImageIcon(imgURL);
		JLabel label = new JLabel(img);
		getContentPane().add(label);
		label.setBounds(0, 0, img.getIconWidth(),img.getIconHeight());
	}
	public static void main(String[] args) {	
		      JOptionPane.showMessageDialog(null, "使用本程序前请确认你的电脑具有以下环境：\nJDK1.8+\ntensoflow--1.7");
	          new Windows();
	}
}