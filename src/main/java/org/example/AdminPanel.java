package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class AdminPanel extends JFrame {

	public AdminPanel() {
		setTitle("Admin Panel");
		setSize(635, 500);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(new Color(53, 132, 228));
		getContentPane().add(buttonsPanel, BorderLayout.CENTER);
		buttonsPanel.setLayout(new GridLayout(0, 1, 0, 10));

		JButton btnAddQuestion = new JButton("Add Question");
		btnAddQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addQuestions();
			}
		});
		buttonsPanel.add(btnAddQuestion);
		
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

		JButton btnLeaderboard = new JButton("Leaderboard");
		btnLeaderboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showLeaderBoard();
			}
		});
		buttonsPanel.add(btnLeaderboard);
		setVisible(true);
	}

	private static void showLeaderBoard() {

		DefaultTableModel tableModel = new DefaultTableModel(
				new Object[][]{},
				new Object[]{"Username", "Total Score", "Num Attempts", "Average Score"}
		);
		JTable leaderboardTable = new JTable(tableModel);

		// retrieve leaderboard data from database
		List<LeaderboardEntry> leaderboardEntries = DataBase.getOverallLeaderboard();

		// populate the table model
		for (LeaderboardEntry entry : leaderboardEntries) {
			tableModel.addRow(new Object[]{
					entry.getUsername(),
					entry.getTotalScore(),
					entry.getNumAttempts(),
					entry.getAverageScore()
			});
		}

		// add the table to a scroll pane and display it
		JScrollPane scrollPane = new JScrollPane(leaderboardTable);
		JFrame leaderboardFrame = new JFrame("Leaderboard");
		leaderboardFrame.getContentPane().add(scrollPane);
		leaderboardFrame.pack();
		leaderboardFrame.setVisible(true);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(0, 2, 0, 10));
		leaderboardFrame.getContentPane().add(inputPanel, BorderLayout.SOUTH);

		JLabel lblTopic = new JLabel("Topic:");
		inputPanel.add(lblTopic);

		JComboBox<String> topicComboBox = new JComboBox<>();
		topicComboBox.addItem("Select Topic");
		topicComboBox.addItem("History");
		topicComboBox.addItem("Science");
		topicComboBox.addItem("Technology");
		topicComboBox.addItem("Sports");
		inputPanel.add(topicComboBox);

		topicComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (topicComboBox.getSelectedItem() != "Select Topic") {
					String selectedTopic = topicComboBox.getSelectedItem().toString();
					List<LeaderboardEntry> topicLeaderboardEntries = DataBase.getTopicLeaderboard(selectedTopic);

					// clear the table model
					tableModel.setRowCount(0);

					// populate the table model with topic leaderboard data
					for (LeaderboardEntry entry : topicLeaderboardEntries) {
						tableModel.addRow(new Object[]{
								entry.getUsername(),
								entry.getTotalScore(),
								entry.getNumAttempts(),
								entry.getAverageScore()
						});
					}
				}
			}
		});
	}

	private void addQuestions() {
		JFrame inputFrame = new JFrame("Add Question");
		inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inputFrame.setSize(400, 300);

		JPanel inputPanel = new JPanel();
		inputPanel.setBackground(new Color(53, 132, 228));
		inputFrame.getContentPane().add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(new GridLayout(0, 2, 0, 10));

		JLabel lblTopic = new JLabel("Topic:");
		inputPanel.add(lblTopic);

		JComboBox<String> topicComboBox = new JComboBox<>();
		topicComboBox.addItem("Select Topic");
		topicComboBox.addItem("History");
		topicComboBox.addItem("Science");
		topicComboBox.addItem("Technology");
		topicComboBox.addItem("Sports");
		inputPanel.add(topicComboBox);

		JLabel lblQuestion = new JLabel("Question:");
		inputPanel.add(lblQuestion);

		JTextArea queTextArea = new JTextArea();
		inputPanel.add(queTextArea);

		JLabel lblOption = new JLabel("Option 1:");
		inputPanel.add(lblOption);

		JTextField option1Field = new JTextField();
		option1Field.setColumns(10);
		inputPanel.add(option1Field);

		JLabel lblOption2 = new JLabel("Option 2:");
		inputPanel.add(lblOption2);

		JTextField option2Field = new JTextField();
		option2Field.setColumns(10);
		inputPanel.add(option2Field);

		JLabel lblOption3 = new JLabel("Option 3:");
		inputPanel.add(lblOption3);

		JTextField option3Field = new JTextField();
		option3Field.setColumns(10);
		inputPanel.add(option3Field);

		JLabel lblOption4 = new JLabel("Option 4:");
		inputPanel.add(lblOption4);

		JTextField option4Field = new JTextField();
		option4Field.setColumns(10);
		inputPanel.add(option4Field);

		JLabel lblAnswer = new JLabel("Answer:");
		inputPanel.add(lblAnswer);

		JTextField answerField = new JTextField();
		answerField.setColumns(10);
		inputPanel.add(answerField);

		JButton btnAddQue = new JButton("Add Question");
		btnAddQue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(Objects.equals(topicComboBox.getSelectedItem(), "Select Topic"))
						throw new SQLException("Select a topic");
					if(queTextArea.getText().isEmpty() || option1Field.getText().isEmpty() || option2Field.getText().isEmpty() ||
							option3Field.getText().isEmpty() || option4Field.getText().isEmpty() || answerField.getText().isEmpty())
						throw new SQLException("Invalid Question");
					String[] options =  {option1Field.getText(),option2Field.getText(),option3Field.getText(),option4Field.getText()};
					DataBase.addQuestion((String) topicComboBox.getSelectedItem(), queTextArea.getText(), options, answerField.getText());
					JOptionPane.showMessageDialog(inputFrame,"Question Added Sucessfully", "Success", JOptionPane.INFORMATION_MESSAGE);
					inputFrame.dispose();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(inputFrame,"Can't add Question\n"+e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		inputFrame.getContentPane().add(btnAddQue, BorderLayout.SOUTH);

		inputFrame.setVisible(true);
	}

	protected void showAllQuestions() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		try {
			ArrayList<Question> questions = DataBase.getQuestionAns();
			String[] columnNames = {"Topic", "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Answer", "Edit", "Delete"};
			Object[][] data = new Object[questions.size()][columnNames.length];
			ArrayList<Integer> questionIds = new ArrayList<>();

			for (int i = 0; i < questions.size(); i++) {
				Question question = questions.get(i);
				data[i][0] = question.getTopic();
				data[i][1] = question.getQuestion();
				data[i][2] = question.getOp1();
				data[i][3] = question.getOp2();
				data[i][4] = question.getOp3();
				data[i][5] = question.getOp4();
				data[i][6] = question.getAns();
				data[i][7] = "Edit"; // Button text for edit
				data[i][8] = "Delete"; // Button text for delete
				questionIds.add(question.getQuestionID());
			}

			DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
			JTable table = new JTable(tableModel) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false; // This causes all cells to be not editable
				}

				@Override
				public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
					Component component = super.prepareRenderer(renderer, row, column);
					if (component instanceof JTextArea) {
						JTextArea textArea = (JTextArea) component;
						textArea.setLineWrap(true);
						textArea.setWrapStyleWord(true);
					}
					return component;
				}
			};

			// Set custom renderer for each column
			for (int i = 0; i < table.getColumnCount() - 2; i++) {
				table.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
			}

			// Set button renderer for edit and delete columns
			ButtonRenderer buttonRenderer = new ButtonRenderer();
			table.getColumnModel().getColumn(7).setCellRenderer(buttonRenderer);
			table.getColumnModel().getColumn(8).setCellRenderer(buttonRenderer);

			table.setPreferredScrollableViewportSize(new Dimension(500, 300));
			table.setFillsViewportHeight(true);

			JScrollPane scroll = new JScrollPane(table);
			frame.add(scroll);

			frame.setSize(500, 300);
			frame.setTitle("Question List");
			frame.setVisible(true);

			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 1) {
						JTable target = (JTable) e.getSource();
						int row = target.getSelectedRow();
						int column = target.getSelectedColumn();
						if (column == 8) { // Delete button column
							deleteQuestion(target, row, questionIds, frame, tableModel);
						} else if (column == 7) { // Edit button column
							editQuestion(row, questionIds, tableModel);
						}
					}
				}
			});


		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void editQuestion(int row, ArrayList<Integer> questionIds, DefaultTableModel tableModel) {
		int questionId = questionIds.get(row);
		Question question = null;
		try {
			question = DataBase.getQuestion(questionId);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error fetching question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Create a new JFrame for editing the question
		JFrame editFrame = new JFrame("Edit Question");
		editFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Create text fields for editing the question and options
		JTextField questionField = new JTextField(question.getQuestion());
		JTextField op1Field = new JTextField(question.getOp1());
		JTextField op2Field = new JTextField(question.getOp2());
		JTextField op3Field = new JTextField(question.getOp3());
		JTextField op4Field = new JTextField(question.getOp4());
		JTextField ansField = new JTextField(question.getAns());

		// Create a panel to hold the text fields
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new GridLayout(6, 2));
		editPanel.add(new JLabel("Question:"));
		editPanel.add(questionField);
		editPanel.add(new JLabel("Option 1:"));
		editPanel.add(op1Field);
		editPanel.add(new JLabel("Option 2:"));
		editPanel.add(op2Field);
		editPanel.add(new JLabel("Option 3:"));
		editPanel.add(op3Field);
		editPanel.add(new JLabel("Option 4:"));
		editPanel.add(op4Field);
		editPanel.add(new JLabel("Answer:"));
		editPanel.add(ansField);

		// Create a button to save the changes
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(event -> {
			String newQuestion = questionField.getText();
			String newOp1 = op1Field.getText();
			String newOp2 = op2Field.getText();
			String newOp3 = op3Field.getText();
			String newOp4 = op4Field.getText();
			String newAns = ansField.getText();

			try {
				DataBase.updateQuestion(questionId, newQuestion, new String[]{newOp1, newOp2, newOp3, newOp4}, newAns);
				JOptionPane.showMessageDialog(editFrame, "Question updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

				// Refresh the table model
				tableModel.setRowCount(0); // Clear the table
				ArrayList<Question> questions = DataBase.getQuestionAns();
				for (Question q : questions) {
					tableModel.addRow(new Object[]{q.getTopic(), q.getQuestion(), q.getOp1(), q.getOp2(), q.getOp3(), q.getOp4(), q.getAns(), "Edit", "Delete"});
				}
				editFrame.dispose();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(editFrame, "Error updating question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Add the panel and button to the frame
		editFrame.add(editPanel, BorderLayout.CENTER);
		editFrame.add(saveButton, BorderLayout.SOUTH);

		// Set the size and visibility of the frame
		editFrame.setSize(400, 200);
		editFrame.setVisible(true);
	}

	private static void deleteQuestion(JTable target, int row, ArrayList<Integer> questionIds, JFrame frame, DefaultTableModel tableModel) {
		try {
			int questionId = questionIds.get(row);
			DataBase.delete(String.valueOf(questionId), "question");
			JOptionPane.showMessageDialog(frame, "Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
			tableModel.removeRow(target.getSelectedRow());
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(frame, "Delete Question\n" + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	static class TextAreaRenderer extends JTextArea implements TableCellRenderer {
		public TextAreaRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setEditable(false);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText((String) value);
			return this;
		}
	}

	static class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setMargin(new Insets(2, 2, 2, 2));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText((String) value);
			return this;
		}
	}
	
}
