package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Instructions extends JFrame {
    public Instructions(int userId) {
        setTitle("Quiz Instructions");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        QuizInstructionsPanel instructionsPanel = new QuizInstructionsPanel(userId);
        add(instructionsPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}

