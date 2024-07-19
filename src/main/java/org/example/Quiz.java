package org.projectgurukul;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class Quiz extends JFrame{
	ArrayList<Question> questions = null;
	private int count = 0;
	private int score = 0;
	public Quiz() {
		setTitle("Welcom to Quiz");
		setSize(600, 500);
		setVisible(true);
		
		startTimer(600);
		
		JPanel quePanel = new JPanel();
		getContentPane().add(quePanel, BorderLayout.NORTH);
		quePanel.setLayout(new BoxLayout(quePanel, BoxLayout.X_AXIS));
		
		
		JTextArea queTextArea = new JTextArea();
		queTextArea.setFont(new Font("Dialog", Font.BOLD, 20));
		queTextArea.setLineWrap(true);
		queTextArea.setWrapStyleWord(true);
		quePanel.add(queTextArea);
		
		JPanel optionsPanel = new JPanel();
		getContentPane().add(optionsPanel, BorderLayout.CENTER);
		optionsPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		optionsPanel.setLayout(new GridLayout(0, 2, 0, 0));
		JRadioButton rdbtnOp1 = new JRadioButton("Option 1");
		rdbtnOp1.setFont(new Font("Dialog", Font.BOLD, 20));
		rdbtnOp1.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnOp1.setVerticalAlignment(SwingConstants.CENTER);
		optionsPanel.add(rdbtnOp1);
		
		JRadioButton rdbtnOp2 = new JRadioButton("Option 2");
		rdbtnOp2.setFont(new Font("Dialog", Font.BOLD, 20));
		rdbtnOp2.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnOp2.setVerticalAlignment(SwingConstants.CENTER);
		optionsPanel.add(rdbtnOp2);
		
		JRadioButton rdbtnOp3 = new JRadioButton("Option 3");
		rdbtnOp3.setFont(new Font("Dialog", Font.BOLD, 20));
		rdbtnOp3.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnOp3.setVerticalAlignment(SwingConstants.CENTER);
		optionsPanel.add(rdbtnOp3);
		
		JRadioButton rdbtnOp4 = new JRadioButton("Option 4");
		rdbtnOp4.setFont(new Font("Dialog", Font.BOLD, 20));
		rdbtnOp4.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnOp4.setVerticalAlignment(SwingConstants.CENTER);
		optionsPanel.add(rdbtnOp4);
		
		ButtonGroup bg = new ButtonGroup();
		
		bg.add(rdbtnOp1);
		bg.add(rdbtnOp2);
		bg.add(rdbtnOp3);
		bg.add(rdbtnOp4);
		
		JPanel buttonsPanel = new JPanel();
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		try {
			questions =DataBase.getQuestionAns();
			queTextArea.setText(questions.get(count).getQuestion());
			rdbtnOp1.setText(questions.get(count).getOp1());
			rdbtnOp2.setText(questions.get(count).getOp2());
			rdbtnOp3.setText(questions.get(count).getOp3());
			rdbtnOp4.setText(questions.get(count).getOp4());
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(bg.getSelection() == null) {JOptionPane.showMessageDialog(quePanel,"Please select an Answer" );}
				else {
						checkAnswer(count,bg);
						count++;
					if (questions.size() > count ) {
						queTextArea.setText(questions.get(count).getQuestion());
						rdbtnOp1.setText(questions.get(count).getOp1());
						rdbtnOp2.setText(questions.get(count).getOp2());
						rdbtnOp3.setText(questions.get(count).getOp3());
						rdbtnOp4.setText(questions.get(count).getOp4());

					}else {
						displayScore();
					}
					
				}
				
			}
		});
		buttonsPanel.add(btnNext);
	}
	
//	Method which matches the answer with the selected options and increases the score
	private void checkAnswer(int count,ButtonGroup bg) {
		for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected() && button.getText().equals(questions.get(count).getAns())) {
            	score++;
            }
        }
	}
	
//	Method to display the score at the end of the game
	private void displayScore() {
		
		dispose();
		JOptionPane.showMessageDialog(this, "Thanks for playing the Quiz by ProjectGurukul\n Your Score was: "+score,"Quiz by ProjectGurukul",JOptionPane.PLAIN_MESSAGE);
	}
	
//	Method to add and start the timer for the entire quiz
	private void startTimer(int timeInSecs) {
	    JLabel timerLabel = new JLabel(String.format("%02d:%02d", timeInSecs / 60, timeInSecs % 60));
	    timerLabel.setFont(new Font("Dialog", Font.BOLD, 20));
	    timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    getContentPane().add(timerLabel, BorderLayout.EAST);

	    Timer timer = new Timer(1000, new ActionListener() {
	        int timeLeft = timeInSecs;

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (timeLeft > 0) {
	                timeLeft--;
	                timerLabel.setText(String.format("%02d:%02d", timeLeft / 60, timeLeft % 60));
	            } else {
	                ((Timer) e.getSource()).stop();
	                displayScore();
	                
	            }
	        }
	    });

	    timer.start();
	}

}
