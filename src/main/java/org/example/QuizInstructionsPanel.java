package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizInstructionsPanel extends JPanel {
    public QuizInstructionsPanel(int userId) {
        setLayout(new BorderLayout());

        // Add instructions label
        JLabel instructionsLabel = new JLabel("Welcome to the Quiz!");
        instructionsLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(instructionsLabel, BorderLayout.NORTH);

        // Add instructions text area
        JTextArea instructionsTextArea = new JTextArea("Please select a topic to start the quiz.\n"
                + "You will have 10 minutes to answer 10 questions.\n"
                + "Select an answer for each question and click Next to proceed.\n"
                + "Good luck!");
        instructionsTextArea.setFont(new Font("Dialog", Font.PLAIN, 18));
        instructionsTextArea.setLineWrap(true);
        instructionsTextArea.setWrapStyleWord(true);
        add(new JScrollPane(instructionsTextArea), BorderLayout.CENTER);

        // Add topic selection panel
        JPanel topicSelectionPanel = new JPanel();
        topicSelectionPanel.setLayout(new GridLayout(0, 1, 0, 0));
        add(topicSelectionPanel, BorderLayout.SOUTH);

        // Add topic selection buttons
        String[] topics = {"History", "Science", "Sports", "Technology"};
        for (String topic : topics) {
            JButton topicButton = new JButton(topic);
            topicButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Start the quiz with the selected topic
                    startQuiz(topic, userId);
                }
            });
            topicSelectionPanel.add(topicButton);
        }
    }

    private void startQuiz(String topic, int userId) {
        // Create a new Quiz instance with the selected topic
        Quiz quiz = new Quiz(topic, userId);
        quiz.setVisible(true);
    }
}
