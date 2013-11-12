package com.tavant.mobilecoe.treasurehunt.camera;

import android.util.Log;


public class CameraNotAvilableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6831511540943332075L;
	private String error="";
	public CameraNotAvilableException(String error){
		Log.d("CameraPreviewProxy",error);
		this.error=error;
	}
	
	public String getError()
	{
		return error;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	

}
