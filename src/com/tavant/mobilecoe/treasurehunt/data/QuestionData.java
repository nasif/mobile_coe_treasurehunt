package com.tavant.mobilecoe.treasurehunt.data;

import java.util.ArrayList;
import java.util.Date;

public class QuestionData {
	
	
	
	private String question=null;
	private ArrayList<String>answer_array=null;
	private String answer=null;
	private String quesImage=null;
	private String hint=null;
	
	public String getQuesImage() {
		return quesImage;
	}
	public void setQuesImage(String quesImage) {
		this.quesImage = quesImage;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public ArrayList<String> getAnswer_array() {
		return answer_array;
	}
	public void setAnswer_array(ArrayList<String> answer_array) {
		this.answer_array = answer_array;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
	

}
