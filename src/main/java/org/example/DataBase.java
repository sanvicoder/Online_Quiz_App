package org.projectgurukul;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
             		+ "  Answer TEXT);");
             
             
             
             
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
		ps.setInt(1, userID);
		ps.setString(2, username);
		ps.setString(3, email);
		ps.setString(4, password);
		
		
		ps.executeUpdate();
		ps.close();
		conn.close();
	}
	
//Method to validata the user id and password
	public static boolean validatePassword(String id, String password) throws SQLException {
		conn = ds.getConnection();
		String sql = "SELECT userID,password FROM users WHERE userID = ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1,id );
		ResultSet rs =  ps.executeQuery();
		
		
		if (id.equals(rs.getString("userID"))  && password.equals(rs.getString("password"))) {
			rs.close();
			ps.close();
			conn.close();
			return true;
		}
		
		rs.close();
		ps.close();
		conn.close();
		return false;		
	}
	
//	Method to add the Question,Answer into the database
	public static void addQuestion(String question,String[] options,String answer) throws SQLException {
		conn = ds.getConnection();
		PreparedStatement ps =conn.prepareStatement("INSERT INTO "
													+ "question(Question,Option1,Option2,Option3,Option4,Answer)"
													+ "VALUES(?,?,?,?,?,?)");
		ps.setString(1, question);
		ps.setString(2, options[0]);
		ps.setString(3, options[1]);
		ps.setString(4, options[2]);
		ps.setString(5, options[3]);
		ps.setString(6, answer);
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
			String que = rs.getString("Question");
			String op1 = rs.getString("Option1");
			String op2 = rs.getString("Option2");
			String op3 = rs.getString("Option3");
			String op4 = rs.getString("Option4");
			String ans = rs.getString("Answer");
			questions.add(new Question(que, op1, op2, op3, op4, ans));
		}
		
		
		rs.close();
		ps.close();
		conn.close();
		
		return questions;
	}
	

}

