package com.tavant.mobilecoe.treasurehunt.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Class to manage the creation and modification of database structure.
 * It is also used to manage connection to the SQLite database (hence the OpenHelper in the name)
 * Note that Android SDK will create DB once. Once created it's structure won't change until 
 * version number is changed.
 * 
 *
 */
public class QuestionDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "treasure.db";
	private static final int DATABASE_VERSION = 2;
	
	public QuestionDatabase(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);    		
	}
	
	
	/*
	 * 
	 *      public static final String ID = BaseColumns._ID; // convention
			public static final String NUMBER = "questionno";
			public static final String QUESTION="question";
			public static final String ANSWER = "answer";
			public static final String HINT  = "hint";
			public static final String STATUS  = "status";
			public static final String TIME  = "time";
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	/**
	 * What to do when the database is created the first time
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + ContentDescriptor.TreasureHuntQuestion.NAME_TABLE+ " ( " +
				ContentDescriptor.TreasureHuntQuestion.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.NUMBER + " TEXT NOT NULL, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.QUESTION 	+ " TEXT NOT NULL, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.ANSWER + " TEXT NOT NULL, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.HINT + " TEXT NOT NULL, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.STATUS + " INTEGER, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.TIME + " TEXT, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.ERRORHINT_ONE + " TEXT NOT NULL, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.ERRORHINT_TWO + " TEXT NOT NULL, " +
				ContentDescriptor.TreasureHuntQuestion.Cols.ERRORHINT_THREE + " TEXT NOT NULL, " +
				"UNIQUE (" + 
					ContentDescriptor.TreasureHuntQuestion.Cols.ID + 
				") ON CONFLICT REPLACE)"
			);	
	}

	/**
	 * What to do when the database version changes: drop table and recreate
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
