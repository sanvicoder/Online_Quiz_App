package org.example;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.sql.Date;
import java.util.List;

import org.sqlite.SQLiteDataSource;

public class DataBase {
//	declaring connection and dataSource variables
	private static Connection conn;
	private static SQLiteDataSource ds;
	
//	initialize method to initialize the database with all the tables
	public static void dbInit() {
		ds = new SQLiteDataSource();
		
		try {
            ds = new SQLiteDataSource();
            ds.setUrl("jdbc:sqlite:QuizDB.db");
        } catch ( Exception e ) {
            e.printStackTrace();
            
            System.exit(0);
        }
        try {
        	 conn = ds.getConnection();
        	 
        	 Statement statement = conn.createStatement();
             statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (\n"
             		+ "  userID INTEGER PRIMARY KEY,\n"
             		+ "  username TEXT NOT NULL,\n"
             		+ "  email TEXT NOT NULL,\n"
             		+ "  password TEXT NOT NULL\n"
             		+ ");\n"
             		);
            
             statement.executeUpdate("CREATE TABLE IF NOT EXISTS question (\n"
             		+ "  QuestionID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
             		+ "  Question TEXT,\n"
             		+ "  Option1 TEXT,\n"
             		+ "  Option2 TEXT,\n"
             		+ "  Option3 TEXT,\n"
             		+ "  Option4 TEXT,\n"
             		+ "  Answer TEXT,\n"
					+ "  Topic TEXT);");

			statement.executeUpdate("CREATE TABLE IF NOT EXISTS quiz_attempts (\n"
					+ "  AttemptID INTEGER PRIMARY KEY AUTOINCREMENT,\n"
					+ "  userID INTEGER,\n"
					+ "  Topic TEXT,\n"
					+ "  Score INTEGER,\n"
					+ "  AttemptDate DATE,\n"
					+ "  FOREIGN KEY (userID) REFERENCES users(userID)\n"
					+ ");");

//			statement.executeUpdate("INSERT INTO users (userID, username, email, password) VALUES "
//					+ "(1, 'johnDoe', 'johndoe@example.com', 'DLX3xW+1kWXHNXpnUr3bOw==:in+RvcOissGINZ2xIwCPnfPAXKEUb6fj9EUT7MSRB/k=')");
//			statement.executeUpdate("INSERT INTO users (userID, username, email, password) VALUES "
//					+ "(2, 'janeDoe', 'janedoe@example.com', 'password456')");
//
//			statement.executeUpdate("INSERT INTO question (QuestionID, Question, Option1, Option2, Option3, Option4, Answer, Topic) VALUES "
//					+ "(1, 'What is the capital of France?', 'Paris', 'London', 'Berlin', 'Rome', 'Paris', 'History')");
//			statement.executeUpdate("INSERT INTO question (QuestionID, Question, Option1, Option2, Option3, Option4, Answer, Topic) VALUES "
//					+ "(2, 'What is the largest planet in our solar system?', 'Earth', 'Saturn', 'Jupiter', 'Uranus', 'Jupiter', 'Science')");
//			statement.executeUpdate("INSERT INTO question (QuestionID, Question, Option1, Option2, Option3, Option4, Answer, Topic) VALUES "
//					+ "(3, 'Who was the first president of the United States?', 'George Washington', 'Abraham Lincoln', 'Thomas Jefferson', 'Theodore Roosevelt', 'George Washington', 'History')");
//
//			statement.executeUpdate("INSERT INTO quiz_attempts (AttemptID, userID, Topic, Score, AttemptDate) VALUES "
//					+ "(1, 1, 'History', 80, '1721498425')");
//			statement.executeUpdate("INSERT INTO quiz_attempts (AttemptID, userID, Topic, Score, AttemptDate) VALUES "
//					+ "(2, 1, 'Science', 90, '1721498425')");
//			statement.executeUpdate("INSERT INTO quiz_attempts (AttemptID, userID, Topic, Score, AttemptDate) VALUES "
//					+ "(3, 2, 'History', 70, '1721498425')");
             
//           Closing statement and connection  
             statement.close();
        	 conn.close();
        	 
        }catch ( SQLException e ) {
            e.printStackTrace();
            System.exit( 0 );
        }
        finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            }catch (SQLException e) {
                System.err.println(e);
              }
        
        }
	

	}
	
//	Method to add user into the database
	public static void addUser(int userID, String username, String email, String password) throws SQLException {
		conn = ds.getConnection();
		PreparedStatement ps =conn.prepareStatement("INSERT INTO "
													+ "users(userID,username,email,password)"
													+ "VALUES(?,?,?,?)");

		// Hash the password with the salt
		String hashedPassword = null;
		try {
			hashedPassword = hashPassword(password);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		ps.setInt(1, userID);
		ps.setString(2, username);
		ps.setString(3, email);
		ps.setString(4, hashedPassword);
		
		
		ps.executeUpdate();
		ps.close();
		conn.close();
	}

	public static String hashPassword(String password) throws NoSuchAlgorithmException {
		SecureRandom random = new SecureRandom();
		byte[] saltBytes = new byte[16];
		random.nextBytes(saltBytes);

		String salt = Base64.getEncoder().encodeToString(saltBytes);

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] passwordBytes = (password + salt).getBytes();
		byte[] hashBytes = md.digest(passwordBytes);

		String hashedPassword = Base64.getEncoder().encodeToString(hashBytes);

		return salt + ":" + hashedPassword;
	}

	public static boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException {
		String[] parts = storedHash.split(":");
		String salt = parts[0];
		String storedHashValue = parts[1];

		String hashedPassword = hashPasswordWithSalt(password, salt);

		return hashedPassword.equals(storedHashValue);
	}

	private static String hashPasswordWithSalt(String password, String salt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] passwordBytes = (password + salt).getBytes();
		byte[] hashBytes = md.digest(passwordBytes);

		return Base64.getEncoder().encodeToString(hashBytes);
	}

	//Method to validata the user id and password
	public static boolean validatePassword(String id, String password) throws SQLException {
		conn = ds.getConnection();
		String sql = "SELECT userID,password FROM users WHERE userID = ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1,id );
		ResultSet rs =  ps.executeQuery();

		if (rs.next()) {
			String storedPassword = rs.getString("password");

			try {
				if (verifyPassword(password, storedPassword)) {
					rs.close();
					ps.close();
					conn.close();
					return true;
				}
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		
		rs.close();
		ps.close();
		conn.close();
		return false;		
	}
	
//	Method to add the Question,Answer into the database
	public static void addQuestion(String topic, String question,String[] options,String answer) throws SQLException {
		conn = ds.getConnection();
		PreparedStatement ps =conn.prepareStatement("INSERT INTO "
													+ "question(Question,Option1,Option2,Option3,Option4,Answer,Topic)"
													+ "VALUES(?,?,?,?,?,?,?)");
		ps.setString(1, question);
		ps.setString(2, options[0]);
		ps.setString(3, options[1]);
		ps.setString(4, options[2]);
		ps.setString(5, options[3]);
		ps.setString(6, answer);
		ps.setString(7, topic);



		ps.executeUpdate();
		ps.close();
		conn.close();
	}

//	Method to remove any record from the user or the questions tables using the id and the given tableName
	public static void delete(String id,String tableName) throws SQLException {
		conn = ds.getConnection();
		String sql  ="DELETE FROM "+tableName+" WHERE QuestionID = ?";
		if(tableName.equals("users")) {
			sql = "DELETE FROM users WHERE userID = ?";
		}
		PreparedStatement ps =conn.prepareStatement(sql);

		ps.setInt(1, Integer.valueOf(id));

		ps.executeUpdate();
		ps.close();
		conn.close();
		
	}
	
//	Method to get question their option and their answer form the database
	public static ArrayList<Question> getQuestionAns() throws SQLException {
		conn = ds.getConnection();
		PreparedStatement ps =conn.prepareStatement("SELECT * FROM question");
		ResultSet rs = ps.executeQuery();
		ArrayList<Question> questions = new ArrayList<>();
		while (rs.next()) {
			int quNo = Integer.parseInt(rs.getString("QuestionID"));
			String topic = rs.getString("Topic");
			String que = rs.getString("Question");
			String op1 = rs.getString("Option1");
			String op2 = rs.getString("Option2");
			String op3 = rs.getString("Option3");
			String op4 = rs.getString("Option4");
			String ans = rs.getString("Answer");
			questions.add(new Question(quNo, que, op1, op2, op3, op4, ans, topic));
		}
		
		
		rs.close();
		ps.close();
		conn.close();
		
		return questions;
	}

	// Method to get a question by its ID
	public static Question getQuestion(int questionId) throws SQLException {
		conn = ds.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM question WHERE QuestionID = ?");
		ps.setInt(1, questionId);
		ResultSet rs = ps.executeQuery();
		Question question = null;
		if (rs.next()) {
			int quNo = Integer.parseInt(rs.getString("QuestionID"));
			String que = rs.getString("Question");
			String op1 = rs.getString("Option1");
			String op2 = rs.getString("Option2");
			String op3 = rs.getString("Option3");
			String op4 = rs.getString("Option4");
			String ans = rs.getString("Answer");
			String topic = rs.getString("Topic");
			question = new Question(quNo, que, op1, op2, op3, op4, ans, topic);
		}
		rs.close();
		ps.close();
		conn.close();
		return question;
	}

	// Method to update a question
	public static void updateQuestion(int questionId, String question, String[] options, String answer) throws SQLException {
		conn = ds.getConnection();
		PreparedStatement ps = conn.prepareStatement("UPDATE question SET Question = ?, Option1 = ?, Option2 = ?, Option3 = ?, Option4 = ?, Answer = ? WHERE QuestionID = ?");
		ps.setString(1, question);
		ps.setString(2, options[0]);
		ps.setString(3, options[1]);
		ps.setString(4, options[2]);
		ps.setString(5, options[3]);
		ps.setString(6, answer);
		ps.setInt(7, questionId);
		ps.executeUpdate();
		ps.close();
		conn.close();
	}

	public static ArrayList<Question> getQuestionAnsByTopic(String topic) throws SQLException {
		ArrayList<Question> questions = new ArrayList<>();
		conn = ds.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM question WHERE Topic = ?");
		pstmt.setString(1, topic);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			int quNo = Integer.parseInt(rs.getString("QuestionID"));
			String que = rs.getString("Question");
			String op1 = rs.getString("Option1");
			String op2 = rs.getString("Option2");
			String op3 = rs.getString("Option3");
			String op4 = rs.getString("Option4");
			String ans = rs.getString("Answer");
			Question q = new Question(quNo, que, op1, op2, op3, op4, ans, topic);
			questions.add(q);
		}
		return questions;
	}

	public static void saveAttemptToDatabase(int userId, String topic, int score) {
		try {
			conn = ds.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO quiz_attempts (userID, Topic, Score, AttemptDate) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, userId);
			pstmt.setString(2, topic);
			pstmt.setInt(3, score);
			pstmt.setDate(4, new Date(System.currentTimeMillis()));
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("Error saving attempt: " + e.getMessage());
		}
	}

	public static List<LeaderboardEntry> getOverallLeaderboard() {
		String query = "SELECT u.username, SUM(qa.Score) AS total_score, COUNT(qa.AttemptID) AS num_attempts, AVG(qa.Score) AS average_score " +
				"FROM quiz_attempts qa " +
				"JOIN users u ON qa.userID = u.userID " +
				"GROUP BY u.username " +
				"ORDER BY total_score DESC";
		return executeQuery(query);
	}

	public static List<LeaderboardEntry> getTopicLeaderboard(String topic) {
		String query = "SELECT u.username, SUM(qa.Score) AS total_score, COUNT(qa.AttemptID) AS num_attempts, AVG(qa.Score) AS average_score " +
				"FROM quiz_attempts qa " +
				"JOIN users u ON qa.userID = u.userID " +
				"WHERE qa.Topic = ? " +
				"GROUP BY u.username " +
				"ORDER BY total_score DESC";
		return executeQuery(query, topic);
	}

	private static List<LeaderboardEntry> executeQuery(String query, Object... params) {
		List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
		try (Connection conn = ds.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(query)) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					LeaderboardEntry entry = new LeaderboardEntry();
					entry.setUsername(rs.getString("username"));
					entry.setTotalScore(rs.getInt("total_score"));
					entry.setNumAttempts(rs.getInt("num_attempts"));
					entry.setAverageScore(rs.getDouble("average_score"));
					leaderboardEntries.add(entry);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return leaderboardEntries;
	}

	public static List<UserPanel.QuizAttempt> getAttemptsTableModel(int userId) {
		List<UserPanel.QuizAttempt> attempts = new ArrayList<>();
		try {
			conn = ds.getConnection();
			String query = "SELECT * FROM quiz_attempts WHERE userID = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				UserPanel.QuizAttempt attempt = new UserPanel.QuizAttempt();
				attempt.setAttemptID(rs.getInt("AttemptID"));
				attempt.setTopic(rs.getString("Topic"));
				attempt.setScore(rs.getInt("Score"));
				attempt.setAttemptDate(rs.getDate("AttemptDate"));
				attempts.add(attempt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attempts;
	}
}

