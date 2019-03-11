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
public class OtherWindows extends JFrame {
	JFrame jf;
	public OtherWindows() {
	jf=this;
	setTitle("��ע��");
	// ���Բ���
	setLayout(null);
	// ����һ������
	Container d = getContentPane();
	// ����"�û���:"��ǩ
	JLabel jl3 = new JLabel("���û�����");
	// �����ı���
	final JTextField jtf3 = new JTextField();
	// ����"����:"��ǩ
	JLabel jl4 = new JLabel("�����룺");
	// ���������
	final JPasswordField jpf4 = new JPasswordField();
	// ���������ַ�Ϊ*
//	jpf4.setEchoChar('*');
	JLabel mail = new JLabel("���䣺");
	// �����ı���
	final JTextField email = new JTextField();
	// ����"ע��"��ť
	JButton jb3 = new JButton("ע��");
	// ����"����"��ť
	JButton jb4 = new JButton("����");
	
	// ���������ӵ�������
		//�û���
		d.add(jl3);
		d.add(jtf3);
		//����
		d.add(jl4);
		d.add(jpf4);
		//����
		d.add(mail);
		d.add(email);
		//ע�᷵��
		d.add(jb3);
		d.add(jb4);
		// ���ø������λ���Լ���С
		jl3.setBounds(50, 120, 90, 30);
		jtf3.setBounds(110, 120, 210, 30);
		jl4.setBounds(50, 160, 90, 30);
		jpf4.setBounds(110, 160, 210, 30);
		
		mail.setBounds(65, 200, 90, 30);
		email.setBounds(110, 200, 210, 30);
		
		jb3.setBounds(130, 240, 60, 40);
		jb4.setBounds(230, 240, 60, 40);
	
	//ע�ᰴťʵ��
	jb3.addActionListener(new ActionListener() { 
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
			      String newnamedata=jtf3.getText();
					char[] npd=jpf4.getPassword();
					String pdata = new String(npd);
					String s="select * from user where name=?";
					PreparedStatement preStatement1 =connect.prepareStatement(s);
					preStatement1.setString(1,newnamedata);
					ResultSet rs = preStatement1.executeQuery();
					if  (rs.next()) {
						JOptionPane.showMessageDialog(null, "�û��Ѵ��ڣ�");
					} else  {
						int m=pdata.length();
						if(m<6) {
							 JOptionPane.showMessageDialog(null, "���볤�Ȳ���6λ����������������");
							 jpf4.setText("");
						}
						else{
							String nemail = email.getText();
							char a[]= nemail.toCharArray();
							int num=0;
							for(int i=0;i<nemail.length();i++) {
								if(a[i]=='@') {
									num++;
								}
							}
							if(num==1) {
							String s1="insert into user (name,password,mail) values(?,?,?)";
						    PreparedStatement preStatement2 =connect.prepareStatement(s1);
						    preStatement2.setString(1,newnamedata);
						    preStatement2.setString(2,pdata);
						    preStatement2.setString(3,nemail);
						    preStatement2.executeUpdate();
						    JOptionPane.showMessageDialog(null, "ע��ɹ�");
						    // ����
						    jtf3.setText("");
						    jpf4.setText("");
						    email.setText("");
						}
							else {
								JOptionPane.showMessageDialog(null, "�����ʽ����");
							    // ����
							    email.setText("");
							}
					}
					}
			    }
			    catch (Exception E) {
			      System.out.print("get data error!");
			      E.printStackTrace();
			    }
		}
	});
	// ʵ��"����"��ť����
	jb4.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			new Windows();
			jf.dispose();
		}
	});
	
	// ���ô����С���رշ�ʽ����������
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
