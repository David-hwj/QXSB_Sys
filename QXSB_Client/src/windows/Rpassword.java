package windows;
import java.sql.*;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
public class Rpassword extends JFrame {
	JFrame jf;
	public Rpassword() {
		jf=this;
	setTitle("找回密码");
	// 绝对布局
	setLayout(null);
	// 定义一个容器
	Container d = getContentPane();
	// 创建"用户名:"标签
	JLabel jl3 = new JLabel("用户名：");
	// 创建文本框
	final JTextField jtf3 = new JTextField();
	JLabel mail = new JLabel("邮箱：");
	// 创建文本框
	final JTextField email = new JTextField();
	// 创建"密码:"标签
	JLabel jl4 = new JLabel("新密码：");
	// 创建密码框
	final JPasswordField jpf4 = new JPasswordField();
	// 设置密码字符为*
	jpf4.setEchoChar('*');
	// 创建"修改密码"按钮
	JButton jb3 = new JButton("修改密码");
	// 创建"返回"按钮
	JButton jb4 = new JButton("返回");
	jb3.addActionListener(new ActionListener() { 
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
			      String newnamedata=jtf3.getText();
					char[] npd=jpf4.getPassword();
					String pdata = new String(npd);
					String s="select * from user where name=?";
					PreparedStatement preStatement1 =connect.prepareStatement(s);
					preStatement1.setString(1,newnamedata);
					ResultSet rs = preStatement1.executeQuery();
					if  (rs.next()) {
						String maildata =email.getText();
						char a[]= maildata.toCharArray();
						int num=0;
						for(int i=0;i<maildata.length();i++) {
							if(a[i]=='@') {
								num++;
							}
						}
						if(num==1) {
						String s1="select * from user where  mail=?";
						PreparedStatement preStatement2 =connect.prepareStatement(s1);
						preStatement2.setString(1,maildata);
						ResultSet rs1 = preStatement1.executeQuery();
						if(rs1.next()) {
							int m=pdata.length();
							if(m<6) {
								 JOptionPane.showMessageDialog(null, "新密码长度不足6位，请重新设置密码");
								 jpf4.setText("");
							}
							else{
								String s2="update user set password=? where name=? ";
							    PreparedStatement preStatement3 =connect.prepareStatement(s2);
							    preStatement3.setString(1,pdata);
							    preStatement3.setString(2, newnamedata);
							    preStatement3.executeUpdate();
							    JOptionPane.showMessageDialog(null, "密码修改成功");
							
						}
					} else  {
						JOptionPane.showMessageDialog(null, "邮箱错误！");
						    // 清零
						    email.setText("");
						}
					}
						else {
							JOptionPane.showMessageDialog(null, "邮箱格式错误！");
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "用户不存在！");
						jtf3.setText("");
						jpf4.setText("");
					}
			    }
			    catch (Exception E) {
			      System.out.print("get data error!");
			      E.printStackTrace();
			    }
		}
	});
	// 实现"返回"按钮功能
	jb4.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			new Windows();
			jf.dispose();
		}
	});
	// 将各组件添加到容器中
	d.add(jl3);
	d.add(jtf3);
	d.add(jl4);
	d.add(jpf4);
	d.add(mail);
	d.add(email);
	d.add(jb3);
	d.add(jb4);
	// 设置各组件的位置以及大小
	jl3.setBounds(65, 160, 90, 30);
	jtf3.setBounds(110, 160, 210, 30);
	jl4.setBounds(65, 200, 90, 30);
	jpf4.setBounds(110, 200, 210, 30);
	mail.setBounds(70, 240, 90, 30);
	email.setBounds(110, 240, 210, 30);
	jb3.setBounds(120, 280, 90, 40);
	jb4.setBounds(240, 280, 60, 40);
	// 设置窗体大小、关闭方式、不可拉伸
	setSize(440, 400);
	setVisible(true);
	setResizable(false);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	BufferedImage img = null;
	try {
	      img = ImageIO.read(new File("./3.jpg"));
	    }
	    catch (Exception E) {
	      E.printStackTrace();
	    }
	JLabel label = new JLabel(new ImageIcon(img));
	getContentPane().add(label);
	label.setBounds(0, 0, img.getWidth(),img.getHeight());
	}
}