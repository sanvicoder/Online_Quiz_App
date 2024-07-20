package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserPanel extends JFrame {

    private JLabel lblUsername;
    private JButton btnViewAttempts;
    private JTable tableAttempts;
    private JScrollPane scrollPane;
    private int userID;
    private JButton btnStartQuiz;

    public UserPanel(int userID) {
        this.userID = userID;
        initUI();
    }

    private void initUI() {
        setTitle("User Panel");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout());
        lblUsername = new JLabel("UserID: " + userID);
        headerPanel.add(lblUsername);
        add(headerPanel, BorderLayout.NORTH);

        // Create table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tableAttempts = new JTable();
        scrollPane = new JScrollPane(tableAttempts);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        btnViewAttempts = new JButton("View Attempts");
        btnViewAttempts.addActionListener(new ViewAttemptsListener());
        buttonPanel.add(btnViewAttempts);
        add(buttonPanel, BorderLayout.WEST);

        // Create logout panel
        JPanel logoutPanel = new JPanel();
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserLogin userLogin = new UserLogin();
            }
        });
        logoutPanel.add(btnLogout);
        add(logoutPanel, BorderLayout.SOUTH);

        // Create button panel
        JPanel instructionButtonPanel = new JPanel();
        instructionButtonPanel.setLayout(new FlowLayout());
        btnStartQuiz = new JButton("Start Quiz");
        btnStartQuiz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Instructions instructions = new Instructions(userID);
            }
        });
        instructionButtonPanel.add(btnStartQuiz);
        add(instructionButtonPanel, BorderLayout.EAST);
    }

    private class ViewAttemptsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                List<QuizAttempt> attempts = DataBase.getAttemptsTableModel(userID);

                String[] columns = {"Attempt ID", "Topic", "Score", "Attempt Date"};
                Object[][] data = new Object[attempts.size()][columns.length];
                for (int i = 0; i < attempts.size(); i++) {
                    QuizAttempt attempt = attempts.get(i);
                    data[i][0] = attempt.getAttemptID();
                    data[i][1] = attempt.getTopic();
                    data[i][2] = attempt.getScore();
                    data[i][3] = attempt.getAttemptDate();
                }

                tableAttempts.setModel(new DefaultTableModel(data, columns));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static class QuizAttempt {
        private int attemptID;
        private String topic;
        private int score;
        private Date attemptDate;

        public int getAttemptID() {
            return attemptID;
        }

        public void setAttemptID(int attemptID) {
            this.attemptID = attemptID;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Date getAttemptDate() {
            return attemptDate;
        }

        public void setAttemptDate(Date attemptDate) {
            this.attemptDate = attemptDate;
        }
    }
}