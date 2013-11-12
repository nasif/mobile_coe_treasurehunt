package com.tavant.mobilecoe.treasurehunt.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

import com.tavant.mobilecoe.treasurehunt.data.QuestionData;
import com.tavant.mobilecoe.treasurehunt.util.ParserTags;

/**
 * 
 * @author tavant technologies
 * 
 */
public class DailyQuestionParser extends BaseParser {

	
	
	
	private static final String TAG = DailyQuestionParser.class.getSimpleName();
	private String mstartTagName;
	private String mEndTagName;
	private boolean isStart;
	private ArrayList<QuestionData>mdata;
	private QuestionData eachQuestion;
	private String date=null;
	private ArrayList<String>options=null;
	private String temptoptions=null;
	
	
	public DailyQuestionParser(InputStream inputStream) {
		super(inputStream);
	}

	@Override
	public void doProcessTagByTag(int eventType) throws Exception {
		switch (eventType) {
		case XmlPullParser.START_DOCUMENT:

			break;
		case XmlPullParser.END_DOCUMENT:

			break;
		case XmlPullParser.START_TAG:
			String localName = pullParser.getName();
			if (localName.equals(ParserTags.TAG_QUESTIONS)) {
				mdata=new ArrayList<QuestionData>();
			}
			else if (localName.equals(ParserTags.TAG_DATE)) {
				eachQuestion = new QuestionData();
				date=pullParser.getAttributeValue(null, "value");
			} else if(localName.equals(ParserTags.TAG_OPTION)){
				options=new ArrayList<String>();
			}
			mstartTagName = localName;
			isStart = true;
			break;
		case XmlPullParser.END_TAG:
			
			String localName1 = pullParser.getName();
			if (localName1.equals(ParserTags.TAG_OPTION1)) {
				options.add(temptoptions);
			}else if(localName1.equals(ParserTags.TAG_OPTION2)){
				options.add(temptoptions);
			}else if(localName1.equals(ParserTags.TAG_OPTION3)){
				options.add(temptoptions);
			}else if (localName1.equals(ParserTags.TAG_OPTION)) {
				eachQuestion.setAnswer_array(options);
			}else if(localName1.equals(ParserTags.TAG_DATE)){
				mdata.add(eachQuestion);
			}
			mEndTagName=localName1;
			isStart = false;
			break;
		case XmlPullParser.TEXT:
			if (isStart) {
				parseText();
			}
			break;	
		}
		
	}

	@Override
	public void parseText() {
         if (isStart) {
			
			if (mstartTagName.equalsIgnoreCase(ParserTags.TAG_QUESTION)) {
				eachQuestion.setQuestion(pullParser.getText());
			}else if (mstartTagName.equalsIgnoreCase(ParserTags.TAG_QIMAGE)) {
				eachQuestion.setQuesImage(pullParser.getText());
			}else if (mstartTagName.equalsIgnoreCase(ParserTags.TAG_HINT)) {
				eachQuestion.setHint(pullParser.getText());
			}else if (mstartTagName.equalsIgnoreCase(ParserTags.TAG_OPTION1)) {
				temptoptions=pullParser.getText();
			}else if (mstartTagName.equalsIgnoreCase(ParserTags.TAG_OPTION2)) {
				temptoptions=pullParser.getText();
			}else if (mstartTagName.equalsIgnoreCase(ParserTags.TAG_OPTION3)) {
				temptoptions=pullParser.getText();
			}else if (mstartTagName.equalsIgnoreCase(ParserTags.TAG_ANSWER)) {
				eachQuestion.setAnswer(pullParser.getText());
			}
		}
		
	}

	@Override
	public  ArrayList<QuestionData> getData() {
		return mdata;
	}
}
