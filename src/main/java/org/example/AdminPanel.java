package org.projectgurukul;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class AdminPanel extends JFrame {
	private JTextField option1Field;
	private JTextField option2Field;
	private JTextField option3Field;
	private JTextField option4Field;
	private JTextField answerField;
	private JTextField remIDfield;
	
	public AdminPanel() {
		setTitle("Admin Panel");
		setSize(635, 500);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel deletePanel = new JPanel();
		deletePanel.setBackground(new Color(53, 132, 228));
		getContentPane().add(deletePanel, BorderLayout.NORTH);
		deletePanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("users");
		comboBox.addItem("question");
		deletePanel.add(comboBox);
		
		remIDfield = new JTextField();
		deletePanel.add(remIDfield);
		remIDfield.setColumns(10);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(new Color(53, 132, 228));
		getContentPane().add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(new GridLayout(0, 2, 0, 10));
		
		JLabel lblQuestion = new JLabel("Question:");
		inputPanel.add(lblQuestion);
		
		JTextArea queTextArea = new JTextArea();
		inputPanel.add(queTextArea);
		
		JLabel lblOption = new JLabel("Option 1:");
		inputPanel.add(lblOption);
		
		option1Field = new JTextField();
		inputPanel.add(option1Field);
		option1Field.setColumns(10);
		
		JLabel lblOption2 = new JLabel("Option 2:");
		inputPanel.add(lblOption2);
		
		option2Field = new JTextField();
		option2Field.setColumns(10);
		inputPanel.add(option2Field);
		
		JLabel lblOption3 = new JLabel("Option 3:");
		inputPanel.add(lblOption3);
		
		option3Field = new JTextField();
		option3Field.setColumns(10);
		inputPanel.add(option3Field);
		
		JLabel lblOption4 = new JLabel("Option 4:");
		inputPanel.add(lblOption4);
		
		option4Field = new JTextField();
		option4Field.setColumns(10);
		inputPanel.add(option4Field);
		
		JLabel lblAnswer = new JLabel("Answer:");
		inputPanel.add(lblAnswer);
		
		answerField = new JTextField();
		answerField.setColumns(10);
		inputPanel.add(answerField);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(new Color(53, 132, 228));
		getContentPane().add(buttonsPanel, BorderLayout.EAST);
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 10));
		
		JButton btnAddQue = new JButton("Add Question");
		btnAddQue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String[] options =  {option1Field.getText(),option2Field.getText(),option3Field.getText(),option4Field.getText()};
					DataBase.addQuestion(queTextArea.getText(),options, answerField.getText());
					JOptionPane.showMessageDialog(btnAddQue,"Question Added Sucessfully", "Success", JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(btnAddQue,"Can't add Question\n"+e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		buttonsPanel.add(btnAddQue);
		
		JButton btnRemove = new JButton("Remove ");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					DataBase.delete(remIDfield.getText(), (String)comboBox.getSelectedItem());
					JOptionPane.showMessageDialog(btnAddQue,"Deleted Sucessfully", "Success", JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(btnAddQue,"Delete Question\n"+e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		buttonsPanel.add(btnRemove);
		
		JButton btnViewAllQuestions = new JButton("View All Questions");
		btnViewAllQuestions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAllQuestions();
			}
		});
		buttonsPanel.add(btnViewAllQuestions);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonsPanel.add(btnExit);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				AdminLogin adminLogin = new AdminLogin();
			}
		});
		buttonsPanel.add(btnLogout);
		setVisible(true);
		
		
	}

	protected void showAllQuestions() {
		JFrame frame =  new JFrame();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		try {
			ArrayList<Question> questions = DataBase.getQuestionAns();
			JTextArea qTextArea = new JTextArea();
			qTextArea.setLineWrap(true);
			qTextArea.setWrapStyleWord(true);
			qTextArea.setEditable(false);
			JScrollPane scroll = new JScrollPane (qTextArea, 
					   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

					frame.add(scroll);
			for (Iterator iterator = questions.iterator(); iterator.hasNext();) {
				Question question = (Question) iterator.next();
				qTextArea.append("\nQ."+question.getQuestion()+"\n"+
								"1."+question.getOp1()+"\n"+
								"2."+question.getOp2()+"\n"+
								"3."+question.getOp3()+"\n"+
								"4."+question.getOp4()+"\n"+
								"Ans."+question.getAns()+"\n"+
								"---------------------------------------------"
						);
				
			}
			frame.setSize(300, 300);
			frame.setTitle("Question List");
			frame.setVisible(true);			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
