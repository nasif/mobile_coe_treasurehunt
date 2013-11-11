package com.tavant.mobilecoe.treasurehunt.parser;

import java.io.InputStream;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.tavant.mobilecoe.treasurehunt.data.QuestionData;

/**
 * 
 * @author tavant
 *
 * This class is super class for all the parsers.
 *  
 */
public abstract class BaseParser {

	protected XmlPullParser pullParser;
	private InputStream resStream;

	public BaseParser(InputStream inputStream) {
		this.resStream = inputStream;
		parser();
	}

	private void parser() {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			pullParser = factory.newPullParser();
			pullParser.setInput(resStream, "UTF-8");
			int eventType = pullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				doProcessTagByTag(eventType);
				eventType = pullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void doProcessTagByTag(int eventType) throws Exception;

	public abstract void parseText();
	
	public abstract HashMap<String, QuestionData> getData();

}
