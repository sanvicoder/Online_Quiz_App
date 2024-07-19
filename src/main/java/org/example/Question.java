package org.example;
public class Question {

	private int questionId;
	private String question;
	private String op1;
	private String op2;
	private String op3;
	private String op4;
	private String ans;

	private String topic;
	
	public Question(int questionId, String question, String op1, String op2, String op3, String op4, String ans, String topic) {
		super();
		this.questionId = questionId;
		this.question = question;
		this.op1 = op1;
		this.op2 = op2;
		this.op3 = op3;
		this.op4 = op4;
		this.ans = ans;
		this.topic = topic;
	}

	public int getQuestionID() {
		return questionId;
	}

	public String getQuestion() {
		return question;
	}

	public String getOp1() {
		return op1;
	}

	public String getOp2() {
		return op2;
	}

	public String getOp3() {
		return op3;
	}

	public String getOp4() {
		return op4;
	}

	public String getAns() {
		return ans;
	}

	public String getTopic() {
		return topic;
	}

}
