package com.tavant.mobilecoe.treasurehunt.prefs;


/**
 * Author  Tavant Technologies
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tavant.mobilecoe.treasurehunt.util.Constant;

public class CommonDailyPreferences {


	private boolean isscanbuttonshowed_12=false;
	private boolean iscaptured_12=false;
	private boolean isubmitted_12=false;
	private String ansString_12=null;



	private boolean isscanbuttonshowed_13=false;
	private boolean iscaptured_13=false;
	private boolean isubmitted_13=false;
	private String ansString_13=null;


	private boolean isscanbuttonshowed_14=false;
	private boolean iscaptured_14=false;
	private boolean isubmitted_14=false;
	private String ansString_14=null;


	private boolean isscanbuttonshowed_15=false;
	private boolean iscaptured_15=false;
	private boolean isubmitted_15=false;
	private String ansString_15=null;




	private Context mContext;
	private SharedPreferences pref;
	private Editor editor;



	private static CommonDailyPreferences instance;

	public CommonDailyPreferences() {

	}

	public static CommonDailyPreferences getInstance(){

		if(instance == null )
			instance = new CommonDailyPreferences();

		return instance;
	}
	/**
	 * 
	 * To retrive username, password, session id, latitude,longitude stored in the application prefernces 
	 * called each  time when application launches, can access through a singleton object
	 * 
	 */
	public void load(Context context) {		
		this.mContext = context;
		pref = mContext.getSharedPreferences(Constant.PREF_NAME,
				Activity.MODE_PRIVATE);
		editor = pref.edit();

		isscanbuttonshowed_12=pref.getBoolean(Constant.isscanbuttonshowed_12, false);
		iscaptured_12=pref.getBoolean(Constant.iscaptured_12, false);
		isubmitted_12=pref.getBoolean(Constant.isubmitted_12, false);
		ansString_12=pref.getString(Constant.ansString_12,null);

		isscanbuttonshowed_13=pref.getBoolean(Constant.isscanbuttonshowed_13, false);
		iscaptured_13=pref.getBoolean(Constant.iscaptured_13, false);
		isubmitted_13=pref.getBoolean(Constant.isubmitted_13, false);
		ansString_13=pref.getString(Constant.ansString_13,null);

		isscanbuttonshowed_14=pref.getBoolean(Constant.isscanbuttonshowed_14, false);
		iscaptured_14=pref.getBoolean(Constant.iscaptured_14, false);
		isubmitted_14=pref.getBoolean(Constant.isubmitted_14, false);
		ansString_14=pref.getString(Constant.ansString_14,null);


		isscanbuttonshowed_15=pref.getBoolean(Constant.isscanbuttonshowed_15, false);
		iscaptured_15=pref.getBoolean(Constant.iscaptured_15, false);
		isubmitted_15=pref.getBoolean(Constant.isubmitted_15, false);
		ansString_15=pref.getString(Constant.ansString_15,null);
	}


	/**
	 * Setter getter for 12 th
	 * @return
	 */
	public boolean isIsscanbuttonshowed_12() {
		return isscanbuttonshowed_12;
	}

	public void setIsscanbuttonshowed_12(boolean isscanbuttonshowed_12) {
		editor.putBoolean(Constant.isscanbuttonshowed_12, isscanbuttonshowed_12);
		editor.commit();
		this.isscanbuttonshowed_12 = isscanbuttonshowed_12;
	}

	public boolean isIscaptured_12() {
		return iscaptured_12;
	}

	public void setIscaptured_12(boolean iscaptured_12) {
		editor.putBoolean(Constant.iscaptured_12, iscaptured_12);
		editor.commit();
		this.iscaptured_12 = iscaptured_12;
	}

	public boolean isIsubmitted_12() {
		return isubmitted_12;
	}

	public void setIsubmitted_12(boolean isubmitted_12) {
		editor.putBoolean(Constant.isubmitted_12, isubmitted_12);
		editor.commit();
		this.isubmitted_12 = isubmitted_12;
	}

	public String getAnsString_12() {
		return ansString_12;
	}

	public void setAnsString_12(String ansString_12) {
		editor.putString(Constant.ansString_12, ansString_12);
		editor.commit();
		this.ansString_12 = ansString_12;
	}


	/**
	 * Setter getter for 13 th
	 * @return
	 */
	public boolean isIsscanbuttonshowed_13() {
		return isscanbuttonshowed_13;
	}

	public void setIsscanbuttonshowed_13(boolean isscanbuttonshowed_13) {
		editor.putBoolean(Constant.isscanbuttonshowed_13, isscanbuttonshowed_12);
		editor.commit();
		this.isscanbuttonshowed_13 = isscanbuttonshowed_13;
	}

	public boolean isIscaptured_13() {
		return iscaptured_13;
	}

	public void setIscaptured_13(boolean iscaptured_13) {
		editor.putBoolean(Constant.isscanbuttonshowed_13, isscanbuttonshowed_13);
		editor.commit();
		this.iscaptured_13 = iscaptured_13;
	}

	public boolean isIsubmitted_13() {
		return isubmitted_13;
	}

	public void setIsubmitted_13(boolean isubmitted_13) {
		editor.putBoolean(Constant.isscanbuttonshowed_13, isscanbuttonshowed_13);
		editor.commit();
		this.isubmitted_13 = isubmitted_13;
	}

	public String getAnsString_13() {
		return ansString_13;
	}

	public void setAnsString_13(String ansString_13) {
		editor.putString(Constant.isscanbuttonshowed_13, ansString_13);
		editor.commit();
		this.ansString_13 = ansString_13;
	}





	/**
	 * Setter getter for 14 th
	 * @return
	 */
	public boolean isIsscanbuttonshowed_14() {
		return isscanbuttonshowed_14;
	}

	public void setIsscanbuttonshowed_14(boolean isscanbuttonshowed_14) {
		editor.putBoolean(Constant.isscanbuttonshowed_14, isscanbuttonshowed_14);
		editor.commit();

		this.isscanbuttonshowed_14 = isscanbuttonshowed_14;
	}

	public boolean isIscaptured_14() {
		return iscaptured_14;
	}

	public void setIscaptured_14(boolean iscaptured_14) {
		editor.putBoolean(Constant.isscanbuttonshowed_14, iscaptured_14);
		editor.commit();

		this.iscaptured_14 = iscaptured_14;
	}

	public boolean isIsubmitted_14() {
		return isubmitted_14;
	}

	public void setIsubmitted_14(boolean isubmitted_14) {
		editor.putBoolean(Constant.isscanbuttonshowed_14, isubmitted_14);
		editor.commit();

		this.isubmitted_14 = isubmitted_14;
	}

	public String getAnsString_14() {
		return ansString_14;
	}

	public void setAnsString_14(String ansString_14) {
		editor.putString(Constant.isscanbuttonshowed_14, ansString_14);
		editor.commit();
		this.ansString_14 = ansString_14;
	}



	/**
	 * Setter getter for 15 th
	 * @return
	 */
	public boolean isIsscanbuttonshowed_15() {
		return isscanbuttonshowed_15;
	}

	public void setIsscanbuttonshowed_15(boolean isscanbuttonshowed_15) {
		editor.putBoolean(Constant.isscanbuttonshowed_15, isscanbuttonshowed_15);
		editor.commit();

		this.isscanbuttonshowed_15 = isscanbuttonshowed_15;
	}

	public boolean isIscaptured_15() {
		return iscaptured_15;
	}

	public void setIscaptured_15(boolean iscaptured_15) {
		editor.putBoolean(Constant.isscanbuttonshowed_15, iscaptured_15);
		editor.commit();

		this.iscaptured_15 = iscaptured_15;
	}

	public boolean isIsubmitted_15() {
		return isubmitted_15;
	}

	public void setIsubmitted_15(boolean isubmitted_15) {
		editor.putBoolean(Constant.isscanbuttonshowed_15, isubmitted_15);
		editor.commit();

		this.isubmitted_15 = isubmitted_15;
	}

	public String getAnsString_15() {
		return ansString_15;
	}

	public void setAnsString_15(String ansString_15) {
		editor.putString(Constant.isscanbuttonshowed_15, ansString_15);
		editor.commit();
		this.ansString_15= ansString_15;
	}












}
