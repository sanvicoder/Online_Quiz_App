package org.example;

public class LeaderboardEntry {
    private String username;
    private int totalScore;
    private int numAttempts;
    private double averageScore;

    // getters
    public String getUsername() {
        return username;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getNumAttempts() {
        return numAttempts;
    }

    public double getAverageScore() {
        return averageScore;
    }

    // setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setNumAttempts(int numAttempts) {
        this.numAttempts = numAttempts;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}