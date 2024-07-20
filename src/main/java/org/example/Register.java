package org.example;

import javax.swing.JFrame;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Register {
	private JFrame frame;
	private JTextField userIdField;
	private JLabel lblName;
	private JTextField nameField;
	private JLabel lblemail;
	private JTextField emailField;
	private JLabel lblPassword;
	private JPasswordField passwordField;
	private JButton btnBack;
	private JButton btnRegister;
	
	public Register() {
		initialilze();
	}

	private void initialilze() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 598, 372);
		frame.setTitle("User Registration");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 10, 10));
		
		JLabel lblId = new JLabel("User ID:");
//		lblId.setBounds(50, 79, 173, 36);
		lblId.setFont(new Font("Jua", Font.BOLD, 13));
		frame.getContentPane().add(lblId);
		
		userIdField = new JTextField();
		frame.getContentPane().add(userIdField);
		userIdField.setColumns(10);
		
		lblName = new JLabel("Name:");
		lblName.setFont(new Font("Jua", Font.BOLD, 13));
		frame.getContentPane().add(lblName);
		
		nameField = new JTextField();
		nameField.setColumns(10);
		frame.getContentPane().add(nameField);
		
		lblemail = new JLabel("Email:");
		lblemail.setFont(new Font("Jua", Font.BOLD, 13));
		frame.getContentPane().add(lblemail);
		
		emailField = new JTextField();
		emailField.setColumns(10);
		frame.getContentPane().add(emailField);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Jua", Font.BOLD, 13));
		frame.getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		frame.getContentPane().add(passwordField);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				UserLogin userLogin = new UserLogin();
			}
		});
		frame.getContentPane().add(btnBack);
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//check all fields
					if(userIdField.getText().isEmpty() || emailField.getText().isEmpty() || nameField.getText().isEmpty() ||
							passwordField.getText().isEmpty())
						throw new SQLException("All fields are mandatory");
					DataBase.addUser(Integer.parseInt(userIdField.getText()), nameField.getText(), emailField.getText(), new String(passwordField.getPassword()));
					JOptionPane.showMessageDialog(btnRegister, "User Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(btnRegister, "Can't Add User\n"+e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}  catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(btnRegister, "Invalid User ID\nUser ID must be numeric\n"+e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e2.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(btnRegister);
		frame.setVisible(true);
	}
}
