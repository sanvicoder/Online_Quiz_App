package org.projectgurukul;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Color;

public class ProfileChooser {

	private JFrame frmQuizApplication;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProfileChooser window = new ProfileChooser();
					window.frmQuizApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ProfileChooser() {
		DataBase.dbInit();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmQuizApplication = new JFrame();
		frmQuizApplication.getContentPane().setBackground(new Color(246, 245, 244));
		frmQuizApplication.setTitle("Quiz Application By ProjectGurukul");
		frmQuizApplication.setBounds(100, 100, 440, 202);
		frmQuizApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmQuizApplication.getContentPane().setLayout(null);
		
		JLabel lblWelcome = new JLabel("Welcome to Quiz Application");
		lblWelcome.setForeground(new Color(26, 95, 180));
		lblWelcome.setBounds(0, 0, 440, 35);
		lblWelcome.setFont(new Font("Jua", Font.BOLD, 20));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		frmQuizApplication.getContentPane().add(lblWelcome);
		
		JLabel lblInstruction = new JLabel("Please Select Login profile:");
		lblInstruction.setForeground(new Color(26, 95, 180));
		lblInstruction.setBounds(113, 12, 218, 52);
		frmQuizApplication.getContentPane().add(lblInstruction);
		
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.setBounds(161, 50, 117, 50);
		btnAdmin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frmQuizApplication.dispose();
				AdminLogin adminLogin= new AdminLogin();
			}
			
		});
		frmQuizApplication.getContentPane().add(btnAdmin);
		
		JButton btnUser = new JButton("User");
		btnUser.setBounds(161, 105, 117, 50);
		btnUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frmQuizApplication.dispose();
				UserLogin userLogin = new UserLogin();
				
			}
		});
		frmQuizApplication.getContentPane().add(btnUser);
	}
}
