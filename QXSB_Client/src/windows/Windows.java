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
		// ���ñ���
		setTitle("���½");
		// ���Բ���
		setLayout(null);
		// ����һ������
		Container c = getContentPane();
		// ����"�û���:"��ǩ
		JLabel jl1 = new JLabel("�û�����");
		// �����ı���
		final JTextField jtf1 = new JTextField();
		// ����"����:"��ǩ
		JLabel jl2 = new JLabel("����:");
		// ���������
		final JPasswordField jpf1 = new JPasswordField();
		// ���������ַ�Ϊ*
		jpf1.setEchoChar('*');
		// ����"��¼"��ť
		JButton jb1 = new JButton("��¼");
		// ����"ע��"��ť
		JButton jb2 = new JButton("ע��");
		JButton jb3 = new JButton("��������");
		jb1.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
				      Class.forName("com.mysql.cj.jdbc.Driver");     //����MYSQL JDBC��������   
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
				           //����URL*******				 
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
							JOptionPane.showMessageDialog(null, "��½�ɹ���");
							OperationFrame of=new OperationFrame();
							of.setVisible(true);
							thisJF.dispose();
							}
							else {
								JOptionPane.showMessageDialog(null, "�������");
								jtf1.setText("");
								jpf1.setText("");
							}
						} else  {
							JOptionPane.showMessageDialog(null, "���û������ڣ�");
							// ����
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
		// ʵ��"ע��"��ť����
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
		// ���������ӵ�������
		c.add(jl1);
		c.add(jtf1);
		c.add(jl2);
		c.add(jpf1);
		c.add(jb1);
		c.add(jb2);
		c.add(jb3);
		// ���ø������λ���Լ���С
		jl1.setBounds(60, 160, 90, 30);
		jtf1.setBounds(110, 160, 210, 30);
		jl2.setBounds(75, 200, 90, 30);
		jpf1.setBounds(110, 200, 210, 30);
		jb1.setBounds(90, 240, 60, 40);
		jb2.setBounds(160, 240, 60, 40);
		jb3.setBounds(230, 240, 100, 40);
		// ���ô����С���رշ�ʽ����������
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
		      JOptionPane.showMessageDialog(null, "ʹ�ñ�����ǰ��ȷ����ĵ��Ծ������»�����\nJDK1.8+\ntensoflow--1.7");
	          new Windows();
	}
}