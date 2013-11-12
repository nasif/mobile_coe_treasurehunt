package com.tavant.mobilecoe.treasurehunt.provider;

import java.util.HashMap;

import android.app.SearchManager;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author rakesh
 */
public class ContentDescriptor {
	// utility variables
	public static final String AUTHORITY = "com.tavant.mobilecoe.treasurehunt.provider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
	public static final UriMatcher URI_MATCHER = buildUriMatcher();
	

	private ContentDescriptor(){};

	// register identifying URIs for Restaurant entity
	// the TOKEN value is associated with each URI registered
	private static  UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = AUTHORITY;
		matcher.addURI(authority, TreasureHuntQuestion.PATH, TreasureHuntQuestion.PATH_TOKEN);
		matcher.addURI(authority, TreasureHuntQuestion.PATH_FOR_ID, TreasureHuntQuestion.PATH_FOR_ID_TOKEN);
		return matcher;
	}
	




	// Define a static class that represents description of stored content entity.
	// Here we define contacts
	public static class TreasureHuntQuestion {
		// an identifying name for entity
		public static final String NAME_TABLE = "treasurequestion";
		public static final String PATH = "treasurequestion";
		public static final int PATH_TOKEN = 100;
		public static final String PATH_FOR_ID = "treasurequestion/*";
		public static final int PATH_FOR_ID_TOKEN = 200;
		

		// URI for all content stored as WSContact entity
		// BASE_URI + PATH ==> "com.tavant.droid.womensecurity.database.contentprovider";
		public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();

	

		// a static class to store columns in entity
		public static class Cols {
			public static final String ID = BaseColumns._ID; // convention
			public static final String NUMBER = "questionno";
			public static final String QUESTION="question";
			public static final String ANSWER = "answer";
			public static final String HINT  = "hint";
			public static final String STATUS  = "status";
			public static final String TIME  = "time";
		}

	}
}
